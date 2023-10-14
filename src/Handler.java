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
    public byte[] receivedNodeIdentifier;
    public byte receivedPayloadLabel;
    public short receivedFrameNumber;
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
            receivedNodeIdentifier = receivedHeader.getNodeIdentifier();
            receivedStreamIdentifier = receivedHeader.getStreamIdentifier();
            receivedPayloadLabel = receivedHeader.getPayloadLabel();
            receivedFrameNumber = receivedHeader.getFrameNumber();

            // Process the payload (e.g., video frame)
            payload = new byte[packetLength - Header.HEADER_LENGTH];
            System.arraycopy(packetData, Header.HEADER_LENGTH, payload, 0, packetLength - Header.HEADER_LENGTH);

    }


    public void printPacketData() {

            switch(NNode.PACKET_TYPE.fromValue(receivedPacketType)) {
                case PUBLISH:
                    System.out.println("Received Packet Type: PUBLISH");
                    break;
                case PUBLISH_ACK:
                    System.out.println("Received ACK from Broker");
                    break;
                case LIST_REQUEST:
                    System.out.println("Received Packet Type: LREQUEST");
                    break;
                case LIST_REQUEST_ACK:
                    System.out.println("Received ACK from Broker");
                    break;
                case LIST_DATA:
                    System.out.println("Received Packet Type: LDATA");
                    break;
                case LIST_DATA_ACK:
                    System.out.println("Received ACK from Consumer");
                    break;
                case SUBSCRIBE:
                    System.out.println("Received Packet Type: SUBSCRIBE");
                    break;
                case SUBSCRIBE_ACK:
                    System.out.println("Received ACK from Broker");
                    break;
                case UNSUBSCRIBE:
                    System.out.println("Received Packet Type: UNSUBSCRIBE");
                    break;
                case UNSUBSCRIBE_ACK:
                    System.out.println("Received ACK from Broker");
                    break;
                case FORWARD:
                    System.out.println("Received Packet Type: FORWARD");
                    break;
                case FORWARD_ACK:
                    System.out.println("Received ACK from Consumer");
                    break;

            }

            switch(receivedNodeIdentifier[0]) {
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
            System.out.printf("%x:%x\n", receivedNodeIdentifier[1], receivedNodeIdentifier[2]);

            if(receivedStreamIdentifier > 0) System.out.printf("Stream identifier %d\n", receivedStreamIdentifier);

            switch(NNode.PAYLOAD_TYPE.fromValue(receivedPayloadLabel)) {
                default:
                NOTHING:
                    break;
                case AUDIO:
                    System.out.printf("Received Payload Label: [Audio, %d]\n", receivedFrameNumber);
                    break;
                case VIDEO:
                    System.out.printf("Received Payload Label: [Video, %d]\n", receivedFrameNumber);
                    break;
                case TEXT:
                    System.out.printf("Received Payload Label: [Text, %d]\n", receivedFrameNumber);
                    break;
                case PRODUCER_LIST:
                    System.out.printf("Received Payload Label: [PList] of size %d\n", receivedFrameNumber);
                    break;
                case PRODUCER_ID:
                    System.out.println("Received Payload Label: [Producer]");
                    break;

            }

            if(receivedPayloadLength > 0) 
                System.out.println("Received Payload: " + new String(payload));

    }
}
