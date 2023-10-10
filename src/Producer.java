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
                send((byte) 10, (byte) 9, (byte) 1, new byte[] {0xF, 1}, "VideoFrame".getBytes(), BROKER_PORT);

            }
            catch(Exception e) {

            }

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
