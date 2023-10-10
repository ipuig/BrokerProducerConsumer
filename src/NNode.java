import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.net.InetAddress;

public abstract class NNode {

    public static final int BROKER_PORT = 50_000;
    public static final int THREAD_POOL_SIZE = 10;
    public static final String BROKER_IP = "172.20.0.2";
    public byte[] nodeId;
    public DatagramSocket serverSocket;
    public ExecutorService threadPool;

    public NNode(byte nodeType) {
        generateNodeId(nodeType);
    }

    abstract void send(byte type, byte length, byte stream, byte[] label, byte[] payload);
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
