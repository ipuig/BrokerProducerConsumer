import java.net.DatagramSocket;

public abstract class Handler {

    private DatagramSocket serverSocket;
    private DatagramSocket receiveSocket;

    public Handler(DatagramSocket serverSocket, DatagramSocket receiveSocket) {
        this.serverSocket = serverSocket;
        this.receiveSocket = receiveSocket;
    }
}
