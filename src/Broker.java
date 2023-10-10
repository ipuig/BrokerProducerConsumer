import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Executors;

public class Broker extends NNode {


    public Broker() {

        super((byte) 0xA);

        try {
            this.serverSocket = new DatagramSocket(BROKER_PORT);
            this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void start() {

        System.out.println(String.format("Broker Listening on port %d...", BROKER_PORT));

        while(true) {

            receive();

        }

    }

    public void receive() {
        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            threadPool.submit(new BrokerHandler(serverSocket, receivePacket));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class BrokerHandler extends Handler implements Runnable {


        public BrokerHandler(DatagramSocket server, DatagramPacket packet) {
            super(server, packet);
        }


        public void run() {


            try {
                unpack();
                printPacketData();
                switch(receivedPacketType) {

                    case 10: // publish
                        send((byte) 60, (byte) 0, (byte) 0, new byte[] {0,0}, new byte[] {}, receivedPacket.getPort());
                        // TODO: send FORWARD to subscribers
                        break;
                    case 11: // list request
                        break;
                    case 62: // after sending list data
                        break;
                    case 13: // subscribe request
                        break;
                    case 14: // unsubscribe request
                        break;
                    case 65: // ACK from forward
                        break;


                }

                // TODO: send an acknowledgement back to the sender

            } catch (Exception e) {

            }

        }

    }

}
