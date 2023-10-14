import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.concurrent.ExecutorService;
import java.net.InetAddress;

public abstract class NNode {

    public static final int BROKER_PORT = 50_000;
    public static final int THREAD_POOL_SIZE = 10;
    // not necessary since I am connecting the containers to the same network
    //  public static final String BROKER_IP = "172.20.0.2";
    public byte[] nodeId;
    public DatagramSocket serverSocket;
    public ExecutorService threadPool;

    public NNode(byte nodeType) {
        generateNodeId(nodeType);
    }

    public static enum ProducerType {
        VIDEO_STREAMER, AUDIO_STREAMER, TEXT_STREAMER
    }

    public void send(byte type, int length, byte stream, byte[] label, byte[] payload, int port) {

        Header header = new Header(type, length, nodeId, stream, label);

        // Combine the header and payload into a packet
        byte[] headerData = header.encode();
        byte[] packetData = new byte[headerData.length + payload.length];
        System.arraycopy(headerData, 0, packetData, 0, headerData.length);
        System.arraycopy(payload, 0, packetData, headerData.length, payload.length);

        // Create a DatagramPacket and send it to the broker
        try {
            DatagramPacket sendPacket = new DatagramPacket(packetData, packetData.length, InetAddress.getByName("localhost"), port);
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
