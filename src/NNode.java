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

    public void send(byte type, byte length, byte stream, byte[] label, byte[] payload, int port) {

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
}
