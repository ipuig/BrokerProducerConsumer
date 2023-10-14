import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Executors;

public class Producer extends NNode implements Stream {

    private ProducerType mode;

    public Producer() {

        super((byte) 0xE); 

        this.mode = ProducerType.TEXT_STREAMER;

        try {
            this.serverSocket = new DatagramSocket();
            this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Producer(ProducerType mode) {

        super((byte) 0xE); 

        this.mode = mode;

        try {
            this.serverSocket = new DatagramSocket();
            this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean streamVideo(int currentVideoFrame) {
        byte[] payload = loadFile(PRODUCER_VIDEO_FRAMES_PATH + String.format("frame%03d.png", currentVideoFrame));
        if (payload.length == 0) return false;
        send((byte) 10, payload.length, (byte) 1, new byte[] {0xF, 1}, payload, BROKER_PORT);
        return true;
    }

    private boolean streamAudio(int currentAudioChunk) {
        byte[] payload = loadFile(PRODUCER_AUDIO_CHUNKS_PATH + String.format("chunk%03d.wav", currentAudioChunk));
        if (payload.length == 0) return false;
        send((byte) 10, payload.length, (byte) 1, new byte[] {0xF, 1}, payload, BROKER_PORT);
        return true;
    }

    private void streamText() {
        byte[] payload = generateRandomString();
        send((byte) 10, payload.length, (byte) 1, new byte[] {0xF, 1}, payload, BROKER_PORT);
    }


    public void start() {

        int currentFrame = 1;

        while(true) {

            try {

                new Thread(() -> receive()).start(); 
                Thread.sleep(2000);

                switch(mode) {
                    case AUDIO_STREAMER:
                        if(streamAudio(currentFrame)) currentFrame++;
                        else currentFrame = 1;
                        break;
                    case VIDEO_STREAMER:
                        if(streamVideo(currentFrame)) currentFrame++;
                        else currentFrame = 1;
                        break;
                    default:
                    case TEXT_STREAMER:
                        streamText();
                        break;
                }
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
