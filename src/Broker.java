import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Executors;

public class Broker extends NNode {

    public Broker() {
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

    public void send() {

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
