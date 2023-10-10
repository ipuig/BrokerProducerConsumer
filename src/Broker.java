import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Broker extends NNode {


    private ConcurrentHashMap<String, List<Integer>> subscribers;


    public Broker() {

        super((byte) 0xA);

        try {
            this.serverSocket = new DatagramSocket(BROKER_PORT);
            this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        subscribers = new ConcurrentHashMap<>();
    }


    public void start() {

        System.out.println(String.format("Broker Listening on port %d...", BROKER_PORT));

        while(true) {

            receive();

        }

    }

    public void receive() {
        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            threadPool.submit(new BrokerHandler(serverSocket, receivePacket));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class BrokerHandler extends Handler implements Runnable {


        public BrokerHandler(DatagramSocket server, DatagramPacket packet) {
            super(server, packet);
        }


        public void run() {


            try {
                unpack();
                printPacketData();
                String encodedId = encodeId(receivedProducerIdentifier);
                byte[] decodedId;
                switch(receivedPacketType) {

                    case 10: // publish
                        send((byte) 60, (byte) 0, (byte) 0, new byte[] {0,0}, new byte[] {}, receivedPacket.getPort());
                        subscribers.putIfAbsent(encodedId, new ArrayList<Integer>(List.of(receivedPacket.getPort())));
                        // TODO: send FORWARD to subscribers
                        List<Integer> forwardPorts = subscribers.get(encodedId);
                        System.out.println(forwardPorts);
                        if(forwardPorts.size() > 1) {

                            Iterator ports = forwardPorts.iterator();
                            System.out.println("It has subscribers");
                            ports.next(); // skip the first port, as it is the producers
                            while(ports.hasNext()) {

                                Integer val = (Integer) ports.next();
                                send((byte) 15, receivedPayloadLength, receivedStreamIdentifier, receivedPayloadLabel, payload, val.intValue());
                                
                            }

                        }
                        break;

                    case 11: // list request
                        send((byte) 61, (byte) 0, (byte) 0, new byte[] {0,0}, new byte[] {}, receivedPacket.getPort()); // ACK

                        // Fetching list of producers
                        StringBuilder sb = new StringBuilder();
                        subscribers.forEach((k, v) ->  sb.append(k + "\n"));
                        String listData = sb.toString();
                        byte[] listDataPayload = listData.getBytes();

                        // Send list
                        send((byte) 12, (byte) listDataPayload.length, (byte) 0, new byte[] {0,0}, listDataPayload, receivedPacket.getPort());
                        break;

                    case 13: // subscribe request
                        send((byte) 63, (byte) 0, (byte) 0, new byte[] {0,0}, new byte[] {}, receivedPacket.getPort()); // ACK
                                                                                                                        
                        if(subscribers.containsKey(new String(payload).trim())) {
                            subscribers.get(new String(payload).trim()).add(receivedPacket.getPort()); // Add to subscribers
                        }

                        break;
                    case 14: // unsubscribe request
                        send((byte) 64, (byte) 0, (byte) 0, new byte[] {0,0}, new byte[] {}, receivedPacket.getPort()); // ACK
                        
                        if(subscribers.containsKey(new String(payload).trim())) {
                            subscribers.get(new String(payload).trim()).remove(receivedPacket.getPort()); // delete from subscribers
                        }

                        break;
                    default:
                        break;


                }

            } catch (Exception e) {

            }

        }

    }

}
