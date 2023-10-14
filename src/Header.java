import java.nio.ByteBuffer;

public class Header {
    public static byte HEADER_LENGTH = 12;
    private byte packetType;
    private int payloadLength;
    private byte[] producerIdentifier;
    private byte streamIdentifier;
    private byte payloadLabel;
    private short frameNumber;

    // Constructor to initialize header fields
    public Header(byte packetType, int payloadLength, byte[] producerIdentifier, byte streamIdentifier, byte payloadLabel, short frameNumber) {
        this.packetType = packetType;
        this.payloadLength = payloadLength;
        this.producerIdentifier = producerIdentifier;
        this.streamIdentifier = streamIdentifier;
        this.payloadLabel = payloadLabel;
        this.frameNumber = frameNumber;
    }

    // Encode the header into a byte array
    public byte[] encode() {
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_LENGTH);
        buffer.put(packetType);
        buffer.putInt(payloadLength);
        buffer.put(producerIdentifier);
        buffer.put(streamIdentifier);
        buffer.put(payloadLabel);
        buffer.putShort(frameNumber);
        return buffer.array();
    }

    // Decode a byte array into a header object
    public static Header decode(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte packetType = buffer.get();
        int payloadLength = buffer.getInt();
        byte[] producerIdentifier = new byte[3];
        buffer.get(producerIdentifier);
        byte streamIdentifier = buffer.get();
        byte payloadLabel = buffer.get();
        short frameNumber = buffer.getShort();

        return new Header(packetType, payloadLength, producerIdentifier, streamIdentifier, payloadLabel, frameNumber);
    }
    
    // Getter methods for header fields
    public byte getPacketType() {
        return packetType;
    }

    public int getPayloadLength() {
        return payloadLength;
    }

    public byte[] getNodeIdentifier() {
        return producerIdentifier;
    }

    public byte getStreamIdentifier() {
        return streamIdentifier;
    }

    public byte getPayloadLabel() {
        return payloadLabel;
    }

    public short getFrameNumber() {
        return frameNumber;
    }
}
