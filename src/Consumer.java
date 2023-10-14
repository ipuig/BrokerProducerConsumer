import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Executors;
import java.util.Scanner;

public class Consumer extends NNode {

    public static int BUFFER_SIZE = 30 * 1024;

    public Consumer() {

        super(NODE_TYPE.CONSUMER);

        try {
            this.serverSocket = new DatagramSocket();
            this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {

        Scanner in = new Scanner(System.in);
        String input = "";
        String[] inputs;
        byte first, second, third = 0;
        byte[] selectedProducer;
        byte[] encodedProducerId;

        while(true) {

            new Thread(() -> {

                while(true)
                    receive();

            }).start(); 

            switch(in.next()) {

                case "list":
                    send(PACKET_TYPE.LIST_REQUEST.getValue(), 0, (byte) 0, PAYLOAD_TYPE.NOTHING.getValue(), (short) 0, new byte[0], BROKER_PORT);
                    break;

                case "subscribe":
                    System.out.println("Type the node id to subscribe ie: <20:21:48>");
                    input = in.next();
                    inputs = input.split(":");
                    first = Byte.parseByte(inputs[0]);
                    second = Byte.parseByte(inputs[1]);
                    third = Byte.parseByte(inputs[2]);
                    selectedProducer = new byte[] {first, second, third};
                    encodedProducerId = encodeId(selectedProducer).getBytes();
                    send(PACKET_TYPE.SUBSCRIBE.getValue(), encodedProducerId.length, (byte) 0, PAYLOAD_TYPE.PRODUCER_ID.getValue(), (short) 0, encodedProducerId, BROKER_PORT);
                    break;

                case "unsubscribe":
                    System.out.println("Type the node id to unsubscribe ie: <20:21:48>");
                    input = in.next();
                    inputs = input.split(":");
                    first = Byte.parseByte(inputs[0]);
                    second = Byte.parseByte(inputs[1]);
                    third = Byte.parseByte(inputs[2]);
                    selectedProducer = new byte[] {first, second, third};
                    encodedProducerId = encodeId(selectedProducer).getBytes();
                    send(PACKET_TYPE.UNSUBSCRIBE.getValue(), encodedProducerId.length, (byte) 0, PAYLOAD_TYPE.PRODUCER_ID.getValue(), (short) 0, encodedProducerId, BROKER_PORT);
                    break;

                default:
                    break;
            }
        }

    }

    public void receive() {

        try {
            byte[] receiveData = new byte[BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            threadPool.submit(new ConsumerHandler(serverSocket, receivePacket));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ConsumerHandler extends Handler implements Runnable, Stream {

        public ConsumerHandler(DatagramSocket server, DatagramPacket packet) {
            super(server, packet);
        }
        

        public void run() {

            try {

                unpack();
                printPacketData();

                // store video, audio, text
                if (PACKET_TYPE.fromValue(receivedPacketType) == PACKET_TYPE.FORWARD) {


                    switch(PAYLOAD_TYPE.fromValue(receivedPayloadLabel)) {
                        case VIDEO:
                            storeFile(CONSUMER_DATA_OUTPUT + "video/" + String.format("receivedFrame%03d.png", receivedFrameNumber), payload);
                            break;
                        case AUDIO:
                            storeFile(CONSUMER_DATA_OUTPUT + "audio/" + String.format("receivedAudio%03d.wav", receivedFrameNumber), payload);
                            break;
                        case TEXT:
                            storeFile(CONSUMER_DATA_OUTPUT + "text/" + String.format("receivedText%03d.txt", receivedFrameNumber), payload);
                            break;
                        default:
                        case NOTHING:
                            break;
                    }
                }
                
            } catch (Exception e) {

            }

        }

    }
    
}
