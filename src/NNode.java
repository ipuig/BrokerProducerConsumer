import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;

public abstract class NNode {

    public static final int BROKER_PORT = 50_000;
    public static final int THREAD_POOL_SIZE = 10;
    public static final String BROKER_IP = "172.20.0.2";
    public DatagramSocket serverSocket;
    public ExecutorService threadPool;

    abstract void send();
    abstract void receive();
    abstract void start();
}
