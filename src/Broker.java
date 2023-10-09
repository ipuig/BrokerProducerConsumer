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

        }

    }

    public void send() {

    }

    public void receive() {

    }

    private static class BrokerHandler extends Handler {

        public BrokerHandler(DatagramSocket server, DatagramSocket receive) {
            super(server, receive);
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
