import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Executors;

public class Producer extends NNode {

    public Producer() {
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
