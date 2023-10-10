import java.nio.ByteBuffer;

public class Header {
    public static byte HEADER_LENGTH = 8;
    private byte packetType;
    private byte payloadLength;
    private byte[] producerIdentifier;
    private byte streamIdentifier;
    private byte[] payloadLabel;

    // Constructor to initialize header fields
    public Header(byte packetType, byte payloadLength, byte[] producerIdentifier, byte streamIdentifier, byte[] payloadLabel) {
        this.packetType = packetType;
        this.payloadLength = payloadLength;
        this.producerIdentifier = producerIdentifier;
        this.streamIdentifier = streamIdentifier;
        this.payloadLabel = payloadLabel;
    }

    // Encode the header into a byte array
    public byte[] encode() {
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_LENGTH + payloadLabel.length);
        buffer.put(packetType);
        buffer.put(payloadLength);
        buffer.put(producerIdentifier);
        buffer.put(streamIdentifier);
        buffer.put(payloadLabel);
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
        byte[] payloadLabel = new byte[data.length - HEADER_LENGTH];
        buffer.get(payloadLabel);

        return new Header(packetType, payloadLength, producerIdentifier, streamIdentifier, payloadLabel);
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

    public byte[] getPayloadLabel() {
        return payloadLabel;
    }
}
