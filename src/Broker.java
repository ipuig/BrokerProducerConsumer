import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Broker extends NNode {

    public static int BUFFER_SIZE = 30 * 1024;

    private ConcurrentHashMap<String, List<Integer>> subscribers;


    public Broker() {

        super(NODE_TYPE.BROKER);

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
            byte[] receiveData = new byte[BUFFER_SIZE];
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
                String encodedId = encodeId(receivedNodeIdentifier);
                byte[] decodedId;
                switch(PACKET_TYPE.fromValue(receivedPacketType)) {

                    case PUBLISH: 
                        send(PACKET_TYPE.PUBLISH_ACK.getValue(), 0, (byte) 0, PAYLOAD_TYPE.NOTHING.getValue(), (short) 0, new byte[0], receivedPacket.getPort());
                        subscribers.putIfAbsent(encodedId, new ArrayList<Integer>(List.of(receivedPacket.getPort())));

                        List<Integer> forwardPorts = subscribers.get(encodedId);
                        System.out.println(forwardPorts);
                        if(forwardPorts.size() > 1) {

                            Iterator ports = forwardPorts.iterator();
                            ports.next(); // skip the first port, as it is the producers
                            while(ports.hasNext()) {

                                Integer val = (Integer) ports.next();
                                send(PACKET_TYPE.FORWARD.getValue(), receivedPayloadLength, receivedStreamIdentifier, receivedPayloadLabel, receivedFrameNumber, payload, val.intValue());
                                
                            }

                        }
                        break;

                    case LIST_REQUEST:
                        send(PACKET_TYPE.LIST_REQUEST_ACK.getValue(), 0, (byte) 0, PAYLOAD_TYPE.NOTHING.getValue(), (short) 0, new byte[] {}, receivedPacket.getPort()); // ACK

                        // Fetching list of producers
                        StringBuilder sb = new StringBuilder();
                        subscribers.forEach((k, v) ->  sb.append(k + "\n"));
                        String listData = sb.toString();
                        byte[] listDataPayload = listData.getBytes();

                        // Send list
                        send(PACKET_TYPE.LIST_DATA.getValue(), listDataPayload.length, (byte) 0, PAYLOAD_TYPE.NOTHING.getValue(), (short) 0, listDataPayload, receivedPacket.getPort());
                        break;

                    case SUBSCRIBE: 
                        send(PACKET_TYPE.SUBSCRIBE_ACK.getValue(), 0, (byte) 0, PAYLOAD_TYPE.NOTHING.getValue(), (short) 0, new byte[] {}, receivedPacket.getPort()); // ACK
                                                                                                                        
                        if(subscribers.containsKey(new String(payload).trim())) {
                            subscribers.get(new String(payload).trim()).add(receivedPacket.getPort()); // Add to subscribers
                        }

                        break;
                    case UNSUBSCRIBE:
                        send(PACKET_TYPE.UNSUBSCRIBE_ACK.getValue(), 0, (byte) 0, PAYLOAD_TYPE.NOTHING.getValue(), (short) 0, new byte[] {}, receivedPacket.getPort()); // ACK
                        
                        if(subscribers.containsKey(new String(payload).trim())) {
                            subscribers.get(new String(payload).trim()).remove((Integer) receivedPacket.getPort()); // delete from subscribers
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
