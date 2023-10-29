import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.concurrent.ExecutorService;
import java.net.InetAddress;

public abstract class NNode {

    public static final int BROKER_PORT = 50_000;
    public static final int THREAD_POOL_SIZE = 10;
    public static String BROKER_IP = "172.17.0.2";
    public byte[] nodeId;
    public DatagramSocket serverSocket;
    public ExecutorService threadPool;

    public NNode(NODE_TYPE type) {
        generateNodeId(type.getValue());
    }

    public static enum PRODUCER_TYPE {
        VIDEO_STREAMER, AUDIO_STREAMER, TEXT_STREAMER;
    }

    public static enum PAYLOAD_TYPE {
        NOTHING(0),
        AUDIO(0xA),
        VIDEO(0xB),
        TEXT(0xC),
        PRODUCER_LIST(0xD),
        PRODUCER_ID(0xE);

        private byte value;

        PAYLOAD_TYPE(int value) {
            this.value = (byte) value;
        }

        public byte getValue() {
            return value;
        }

        public static PAYLOAD_TYPE fromValue(byte value) {
            for (PAYLOAD_TYPE type : values()) {
                if (type.value == value) return type;
            }
            return PAYLOAD_TYPE.NOTHING;

        }
    }

    public static enum NODE_TYPE {
        PRODUCER(0xE), CONSUMER(0xC), BROKER(0xA);

        private byte value;

        NODE_TYPE(int value) {
            this.value = (byte) value;
        }

        public byte getValue() {
            return value;
        }

    }

    public static enum PACKET_TYPE {

        PUBLISH(10),
        PUBLISH_ACK(60),
        LIST_REQUEST(11), 
        LIST_REQUEST_ACK(61),
        LIST_DATA(12),
        LIST_DATA_ACK(62),
        SUBSCRIBE(13),
        SUBSCRIBE_ACK(63),
        UNSUBSCRIBE(14),
        UNSUBSCRIBE_ACK(64),
        FORWARD(15),
        FORWARD_ACK(65),
        ERROR(-1);

        private byte value;

        PACKET_TYPE(int value) {
            this.value = (byte) value;
        }

        public byte getValue() {
            return value;
        }

        public static PACKET_TYPE fromValue(byte value) {
            for (PACKET_TYPE type : values()) {
                if (type.value == value) return type;
            }
            return PACKET_TYPE.ERROR;

        }

    }

    public void send(byte type, int length, byte stream, byte label, short frameNumber, byte[] payload, String ip,  int port) {

        Header header = new Header(type, length, nodeId, stream, label, frameNumber);

        // Combine the header and payload into a packet
        byte[] headerData = header.encode();
        byte[] packetData = new byte[headerData.length + payload.length];
        System.arraycopy(headerData, 0, packetData, 0, headerData.length);
        System.arraycopy(payload, 0, packetData, headerData.length, payload.length);

        // Create a DatagramPacket and send it to the broker
        try {
            // DatagramPacket sendPacket = new DatagramPacket(packetData, packetData.length, InetAddress.getByName(ip), port);
            DatagramPacket sendPacket = new DatagramPacket(packetData, packetData.length, InetAddress.getLocalHost(), port);
            serverSocket.send(sendPacket);
        }
        catch(Exception e) {

        }
    }

    abstract void receive();
    abstract void start();

    public void generateNodeId(byte nodeType) {

        try {
            // I am getting the last octal from the ip as UID for the nodes
            int octal = Integer.parseInt(InetAddress
                    .getLocalHost()
                    .getHostAddress()
                    .split("\\D")[3]);

            this.nodeId = new byte[] {nodeType,
                (byte) (octal >= 127 ? 127 : octal),
                (byte) (octal > 127 ? octal - 127 : 0)
            };
        }
        catch(Exception e) {

        }

    }

    public String encodeId(byte[] id) {
        return String.format("%d:%d:%d", id[0], id[1], id[2]);
    }

    public byte[] decodeId(String id) {
        String[] d = id.split(":");
        return new byte[] {Byte.parseByte(d[0]), Byte.parseByte(d[1]), Byte.parseByte(d[2])};
    }
}
