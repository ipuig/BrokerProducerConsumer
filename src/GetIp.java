import java.net.InetAddress;
import java.net.UnknownHostException;

public class GetIp {
    public static void getIp() throws UnknownHostException {
        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }
}
