import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Executors;

public class Consumer extends NNode {

    public Consumer() {
        try {
            this.serverSocket = new DatagramSocket();
            this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void start() {


        while(true) {


        }

    }

    public void send() {

    }

    public void receive() {

    }

    private static class ConsumerHandler extends Handler {

        public ConsumerHandler(DatagramSocket server, DatagramPacket packet) {
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
