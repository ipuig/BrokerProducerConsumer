import java.net.DatagramSocket;
import java.net.DatagramPacket;

public abstract class Handler {

    public DatagramSocket listenerSocket;
    public DatagramPacket receivedPacket;

    public Handler(DatagramSocket listenerSocket, DatagramPacket receivedPacket) {
        this.listenerSocket = listenerSocket;
        this.receivedPacket = receivedPacket;
    }
}
