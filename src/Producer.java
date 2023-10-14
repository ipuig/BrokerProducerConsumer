import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Executors;

public class Producer extends NNode {

    private Mode producerType;

    public Producer() {

        super((byte) 0xE); 

        producerType = Mode.TEXT_STREAMER;

        try {
            this.serverSocket = new DatagramSocket();
            this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Producer(int n) {

        super((byte) 0xE); 

        if (n == 1) producerType = Mode.VIDEO_STREAMER;
        else if (n == 2) producerType = Mode.AUDIO_STREAMER;

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

                new Thread(() -> receive()).start(); 
                Thread.sleep(2000);

                switch(producerType) {
                    case AUDIO_STREAMER:
                        break;
                    case VIDEO_STREAMER:
                        break;
                    case TEXT_STREAMER:
                        break;
                }

                send((byte) 10, (byte) 9, (byte) 1, new byte[] {0xF, 1}, "Text video frame".getBytes(), BROKER_PORT);

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

    private static enum Mode {
        VIDEO_STREAMER, AUDIO_STREAMER, TEXT_STREAMER
    }
    
}
