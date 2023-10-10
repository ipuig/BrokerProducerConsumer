import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Executors;

public class Producer extends NNode {

    public Producer() {

        super((byte) 0xE); 

        try {
            this.serverSocket = new DatagramSocket();
            this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void start() {


        while(true) {

            try {

                Thread.sleep(2000);
                send((byte) 10, (byte) 9, (byte) 1, new byte[] {0xF, 1}, "VideoFrame".getBytes());

            }
            catch(Exception e) {

            }

        }

    }

    public void send(byte packetType, byte payloadLength, byte stream, byte[] payloadLabel, byte[] payload) {
        
        Header header = new Header(packetType, payloadLength, nodeId, stream, payloadLabel);

        // Combine the header and payload into a packet
        byte[] headerData = header.encode();
        byte[] packetData = new byte[headerData.length + payload.length];
        System.arraycopy(headerData, 0, packetData, 0, headerData.length);
        System.arraycopy(payload, 0, packetData, headerData.length, payload.length);

        // Create a DatagramPacket and send it to the broker
        try {
            DatagramPacket sendPacket = new DatagramPacket(packetData, packetData.length, InetAddress.getByName("localhost"), BROKER_PORT);
            serverSocket.send(sendPacket);

        }
        catch(Exception e) {

        }

    }

    public void receive() {

    }

    private static class ProducerHandler extends Handler {

        public ProducerHandler(DatagramSocket server, DatagramPacket packet) {
            super(server, packet);
        }
        

        public void run() {

            try {

                // TODO: process request
                
                // TODO: extract binary header and encoded data
                
                // TODO: decode and handle the binary header as needed
                
                // TODO: Handle responses according to header
                
                // TODO: send an acknowledgement back to the sender
                
            } catch (Exception e) {

            }

        }

    }
    
}
