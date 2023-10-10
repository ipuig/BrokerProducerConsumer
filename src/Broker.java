import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Executors;

public class Broker extends NNode {


    public Broker() {

        super((byte) 0xA);

        try {
            this.serverSocket = new DatagramSocket(BROKER_PORT);
            this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void start() {

        System.out.println(String.format("Broker Listening on port %d...", BROKER_PORT));

        while(true) {

            receive();

        }

    }

    public void send(byte packetType, byte payloadLength, byte stream, byte[] payloadLabel, byte[] payload) {

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

    private static class BrokerHandler extends Handler implements Runnable {


        public BrokerHandler(DatagramSocket server, DatagramPacket packet) {
            super(server, packet);
        }
        

        public void run() {


            try {

            
            // Extract the received packet data
            byte[] packetData = receivedPacket.getData();
            int packetLength = receivedPacket.getLength();

            // Extract the header from the packet data
            Header receivedHeader = Header.decode(packetData);

            // Process the received packet from the receiver's point of view
            byte receivedPacketType = receivedHeader.getPacketType();
            byte[] receivedProducerIdentifier = receivedHeader.getProducerIdentifier();
            byte receivedStreamIdentifier = receivedHeader.getStreamIdentifier();
            byte[] receivedPayloadLabel = receivedHeader.getPayloadLabel();

            // Process the payload (e.g., video frame)
            byte[] payload = new byte[packetLength - Header.HEADER_LENGTH];
            System.arraycopy(packetData, Header.HEADER_LENGTH, payload, 0, packetLength - Header.HEADER_LENGTH);

            // Print received information
            printPacketData(receivedPacketType, receivedProducerIdentifier,
                    receivedStreamIdentifier, receivedPayloadLabel, payload);
            

                // TODO: Handle responses according to header
                
                // TODO: send an acknowledgement back to the sender
                
            } catch (Exception e) {

            }

        }

    }
    
}
