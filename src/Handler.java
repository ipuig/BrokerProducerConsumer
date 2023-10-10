import java.net.DatagramSocket;
import java.net.DatagramPacket;

public abstract class Handler {

    public DatagramSocket listenerSocket;
    public DatagramPacket receivedPacket;

    public Handler(DatagramSocket listenerSocket, DatagramPacket receivedPacket) {
        this.listenerSocket = listenerSocket;
        this.receivedPacket = receivedPacket;
    }


    public void printPacketData(byte receivedPacketType, byte[] receivedProducerIdentifier,
            byte receivedStreamIdentifier, byte[] receivedPayloadLabel, byte[] payload) {

            switch(receivedPacketType) {
                default:
                    System.out.println("Received Packet Type: PUBLISH");
                    break;

            }

            System.out.printf("Received Producer Identifier: %x_%x_%x \n",
                    receivedProducerIdentifier[0],
                    receivedProducerIdentifier[1],
                    receivedProducerIdentifier[2]);

            System.out.printf("Stream identifier %x\n", receivedStreamIdentifier);

            switch(receivedPayloadLabel[0]) {
                default:
                    System.out.printf("Received Payload Label: [Audio, %x]\n", receivedPayloadLabel[2]);
                    break;

            }

            System.out.println("Received Payload: " + new String(payload));

    }
}
