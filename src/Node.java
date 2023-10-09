public abstract class Node {

    private static final int BROKER_PORT = 50_000;
    private static final String BROKER_IP = "172.20.0.2";
    private DatagramSocket serverSocket;
    private ExecutorService threadPool;

    abstract void send();
    abstract void receive();
    abstract void start();
}
