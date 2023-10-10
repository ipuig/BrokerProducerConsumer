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
                receive();

            }
            catch(Exception e) {

            }

        }

    }

    public void receive() {

        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            threadPool.submit(new ProducerHandler(serverSocket, receivePacket));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static class ProducerHandler extends Handler implements Runnable {

        public ProducerHandler(DatagramSocket server, DatagramPacket packet) {
            super(server, packet);
        }
        

        public void run() {

            try {

                unpack();
                printPacketData();
                
            } catch (Exception e) {

            }

        }

    }
    
}
