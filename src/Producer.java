import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Executors;

public class Producer extends NNode implements Stream {

    private PRODUCER_TYPE mode;

    public Producer() {

        super(NODE_TYPE.PRODUCER); 

        this.mode = PRODUCER_TYPE.TEXT_STREAMER;

        try {
            this.serverSocket = new DatagramSocket();
            this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Producer(PRODUCER_TYPE mode) {

        super(NODE_TYPE.PRODUCER); 

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
        send(PACKET_TYPE.PUBLISH.getValue(), payload.length, (byte) 1, PAYLOAD_TYPE.VIDEO.getValue(), (short) currentVideoFrame, payload, BROKER_PORT);
        return true;
    }

    private boolean streamAudio(int currentAudioChunk) {
        byte[] payload = loadFile(PRODUCER_AUDIO_CHUNKS_PATH + String.format("chunk%03d.wav", currentAudioChunk));
        if (payload.length == 0) return false;
        send(PACKET_TYPE.PUBLISH.getValue(), payload.length, (byte) 1, PAYLOAD_TYPE.AUDIO.getValue(), (short) currentAudioChunk, payload, BROKER_PORT);
        return true;
    }

    private boolean streamText(int currentTextFile) {
        if (currentTextFile == Short.MAX_VALUE) return false;
        byte[] payload = generateRandomString();
        send(PACKET_TYPE.PUBLISH.getValue(), payload.length, (byte) 1, PAYLOAD_TYPE.TEXT.getValue(), (short) currentTextFile, payload, BROKER_PORT);
        return true;
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
                        if(streamText(currentFrame)) currentFrame++;
                        else currentFrame = 1;
                        break;
                }
            }
            catch(Exception e) {

            }

        }

    }

    public void receive() {

        try {
            byte[] receiveData = new byte[16]; // producer only receives acks, hence doesn't need more than that
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
