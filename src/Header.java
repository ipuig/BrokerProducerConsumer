import java.nio.ByteBuffer;

public class Header {
    private byte packetType;
    private byte payloadLength;
    private byte[] producerIdentifier;
    private byte streamIdentifier;
    private byte[] subscriberInformation;

    // Constructor to initialize header fields
    public Header(byte packetType, byte payloadLength, byte[] producerIdentifier, byte streamIdentifier, byte[] subscriberInformation) {
        this.packetType = packetType;
        this.payloadLength = payloadLength;
        this.producerIdentifier = producerIdentifier;
        this.streamIdentifier = streamIdentifier;
        this.subscriberInformation = subscriberInformation;
    }

    // Encode the header into a byte array
    public byte[] encode() {
        ByteBuffer buffer = ByteBuffer.allocate(8 + subscriberInformation.length);
        buffer.put(packetType);
        buffer.put(payloadLength);
        buffer.put(producerIdentifier);
        buffer.put(streamIdentifier);
        buffer.put(subscriberInformation);
        return buffer.array();
    }

    // Decode a byte array into a header object
    public static Header decode(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte packetType = buffer.get();
        byte payloadLength = buffer.get();
        byte[] producerIdentifier = new byte[3];
        buffer.get(producerIdentifier);
        byte streamIdentifier = buffer.get();
        byte[] subscriberInformation = new byte[data.length - 8];
        buffer.get(subscriberInformation);

        return new Header(packetType, payloadLength, producerIdentifier, streamIdentifier, subscriberInformation);
    }
    
    // Getter methods for header fields
    public byte getPacketType() {
        return packetType;
    }

    public byte getPayloadLength() {
        return payloadLength;
    }

    public byte[] getProducerIdentifier() {
        return producerIdentifier;
    }

    public byte getStreamIdentifier() {
        return streamIdentifier;
    }

    public byte[] getSubscriberInformation() {
        return subscriberInformation;
    }
}
