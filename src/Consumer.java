import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Executors;
import java.util.Scanner;

public class Consumer extends NNode {

    public Consumer() {

        super((byte) 0xC);

        try {
            this.serverSocket = new DatagramSocket();
            this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {

        Scanner in = new Scanner(System.in);
        byte first, second, third = 0;

        while(true) {

            new Thread(() -> receive()).start(); 

            switch(in.next()) {

                case "list":
                    send((byte) 11, (byte) 0, (byte) 0, new byte[] {0, 0}, new byte[] {}, BROKER_PORT);
                    break;

                case "subscribe":
                    System.out.println("Type the node id to subscribe");
                    System.out.print("First byte: ");
                    first = in.nextByte();
                    System.out.print("\nSecond byte: ");
                    second = in.nextByte();
                    System.out.print("\nThird byte: ");
                    third = in.nextByte();
                    System.out.println();
                    send((byte) 13, (byte) 3, (byte) 0, new byte[] {0, 0}, new byte[] {first, second, third}, BROKER_PORT);
                    break;

                case "unsubscribe":
                    System.out.println("Type the node id to unsubscribe");
                    System.out.print("First byte: ");
                    first = in.nextByte();
                    System.out.print("\nSecond byte: ");
                    second = in.nextByte();
                    System.out.print("\nThird byte: ");
                    third = in.nextByte();
                    System.out.println();
                    send((byte) 14, (byte) 3, (byte) 0, new byte[] {0, 0}, new byte[] {first, second, third}, BROKER_PORT);
                    break;

                default:
                    break;
            }




        }

    }

    public void receive() {

        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            threadPool.submit(new ConsumerHandler(serverSocket, receivePacket));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ConsumerHandler extends Handler implements Runnable {

        public ConsumerHandler(DatagramSocket server, DatagramPacket packet) {
            super(server, packet);
        }
        

        public void run() {

            try {

                unpack();
                printPacketData();
                
            } catch (Exception e) {

            }

        }

    }
    
}
