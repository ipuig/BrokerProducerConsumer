import java.net.DatagramSocket;
import java.net.DatagramPacket;

public abstract class Handler {

    public DatagramSocket listenerSocket;
    public DatagramPacket receivedPacket;

    public Handler(DatagramSocket listenerSocket, DatagramPacket receivedPacket) {
        this.listenerSocket = listenerSocket;
        this.receivedPacket = receivedPacket;
    }

    public byte[] packetData;
    public int packetLength;
    public Header receivedHeader;
    public byte receivedPacketType;
    public int receivedPayloadLength;
    public byte receivedStreamIdentifier;
    public byte[] receivedProducerIdentifier;
    public byte[] receivedPayloadLabel;
    public byte[] payload;

    public void unpack() {
            // Extract the received packet data
            packetData = receivedPacket.getData();
            packetLength = receivedPacket.getLength();

            // Extract the header from the packet data
            receivedHeader = Header.decode(packetData);

            // Process the received packet from the receiver's point of view
            receivedPacketType = receivedHeader.getPacketType();
            receivedPayloadLength = receivedHeader.getPayloadLength();
            receivedProducerIdentifier = receivedHeader.getProducerIdentifier();
            receivedStreamIdentifier = receivedHeader.getStreamIdentifier();
            receivedPayloadLabel = receivedHeader.getPayloadLabel();

            // Process the payload (e.g., video frame)
            payload = new byte[packetLength - Header.HEADER_LENGTH];
            System.arraycopy(packetData, Header.HEADER_LENGTH, payload, 0, packetLength - Header.HEADER_LENGTH);

    }


    public void printPacketData() {

            switch(receivedPacketType) {
                case 10:
                    System.out.println("Received Packet Type: PUBLISH");
                    break;
                case 60:
                    System.out.println("Received ACK from Broker");
                    break;
                case 11:
                    System.out.println("Received Packet Type: LREQUEST");
                    break;
                case 61:
                    System.out.println("Received ACK from Broker");
                    break;
                case 12:
                    System.out.println("Received Packet Type: LDATA");
                    break;
                case 62:
                    System.out.println("Received ACK from Consumer");
                    break;
                case 13:
                    System.out.println("Received Packet Type: SUBSCRIBE");
                    break;
                case 63:
                    System.out.println("Received ACK from Broker");
                    break;
                case 14:
                    System.out.println("Received Packet Type: UNSUBSCRIBE");
                    break;
                case 64:
                    System.out.println("Received ACK from Broker");
                    break;
                case 15:
                    System.out.println("Received Packet Type: FORWARD");
                    break;
                case 65:
                    System.out.println("Received ACK from Consumer");
                    break;

            }

            switch(receivedProducerIdentifier[0]) {
                default:
                    break;
                case 0xA: 
                    System.out.print("Received Broker Id: 0xA:");
                    break;
                case 0xC: 
                    System.out.print("Received Consumer Id: 0xC:");
                    break;
                case 0xE: 
                    System.out.print("Received Producer Id: 0xE:");
                    break;

            }
            System.out.printf("%x:%x\n", receivedProducerIdentifier[1], receivedProducerIdentifier[2]);

            if(receivedStreamIdentifier > 0) System.out.printf("Stream identifier %d\n", receivedStreamIdentifier);

            switch(receivedPayloadLabel[0]) {
                default:
                    break;
                case 0xA:
                    System.out.printf("Received Payload Label: [Audio, %x]\n", receivedPayloadLabel[2]);
                    break;
                case 0xF:
                    System.out.printf("Received Payload Label: [Frame, %x]\n", receivedPayloadLabel[2]);
                    break;
                case 0xE:
                    System.out.printf("Received Payload Label: [List] of size %x\n", receivedPayloadLabel[2]);
                    break;
                case 0x9:
                    System.out.println("Received Payload Label: [Producer]");
                    break;

            }

            if(receivedPayloadLength > 0) 
                System.out.println("Received Payload: " + new String(payload));

    }
}
