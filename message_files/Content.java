package message_files;

import java.util.Arrays;
import input_processing.GeneralConfig;

public class Content {

    public int typeID;
    public int pieceIndex;
    public byte[] payload;

    public static final int CHOKE = 0;
    public static final int UNCHOKE = 1;
    public static final int INTERESTED = 2;
    public static final int NOT_INTERESTED = 3;
    public static final int HAVE = 4;
    public static final int BITFIELD = 5;
    public static final int REQUEST = 6;
    public static final int PIECE = 7;

    /**
     * Constructs a raw protocol message for the given type and payload.
     * Format:
     * 4 bytes: length (not counting these 4 bytes)
     * 1 byte: type
     * Variable: payload, format by message type
     */
    public static byte[] make(int type, byte[] payload, int pieceIndex) {
        byte[] mess;
        switch (type) {
            case CHOKE:
            case UNCHOKE:
            case INTERESTED:
            case NOT_INTERESTED:
                // 4 (len) + 1 (type) = 5 bytes
                mess = new byte[5];
                GeneralConfig.insertIntIntoByteArray(mess, 1, 0); // length = 1 (type)
                mess[4] = (byte) type;
                return mess;
            case HAVE:
            case REQUEST:
                // 4 (len) + 1 (type) + 4 (pieceIndex) = 9 bytes
                mess = new byte[9];
                GeneralConfig.insertIntIntoByteArray(mess, 5, 0); // length = 1 (type) + 4 (index)
                mess[4] = (byte) type;
                GeneralConfig.insertIntIntoByteArray(mess, pieceIndex, 5);
                return mess;
            case BITFIELD:
                // 4 (len) + 1 (type) + N (payload) = 5+N bytes
                mess = new byte[5 + payload.length];
                GeneralConfig.insertIntIntoByteArray(mess, 1 + payload.length, 0); // length: 1 (type) + N
                mess[4] = (byte) type;
                System.arraycopy(payload, 0, mess, 5, payload.length);
                return mess;
            case PIECE:
                // 4 (len) + 1 (type) + 4 (pieceIndex) + N (payload) = 9+N bytes
                mess = new byte[9 + payload.length];
                GeneralConfig.insertIntIntoByteArray(mess, 5 + payload.length, 0); // length: 1+4+N
                mess[4] = (byte) type;
                GeneralConfig.insertIntIntoByteArray(mess, pieceIndex, 5);
                System.arraycopy(payload, 0, mess, 9, payload.length);
                return mess;
            default:
                return new byte[0];
        }
    }

    /**
     * Parse a protocol message from bytes.
     * Fills in typeID, pieceIndex, and payload as appropriate.
     */
    public void read(byte[] message) {
        int len = message.length;
        if (len == 0) {
            this.typeID = -1;
            return;
        }
        // Protocol type byte is always at offset 0 for message[] here
        int type = message[0] & 0xFF;
        if (type >= 0 && type <= 7) {
            this.typeID = type;
            switch (type) {
                case CHOKE:
                case UNCHOKE:
                case INTERESTED:
                case NOT_INTERESTED:
                    // No payload.
                    break;
                case HAVE:
                case REQUEST:
                    if (len >= 5) {
                        this.pieceIndex = GeneralConfig.byteArrayToInt(Arrays.copyOfRange(message, 1, 5));
                    }
                    break;
                case BITFIELD:
                    if (len > 1) {
                        this.payload = new byte[len - 1];
                        System.arraycopy(message, 1, this.payload, 0, len - 1);
                    }
                    break;
                case PIECE:
                    if (len >= 9) {
                        this.pieceIndex = GeneralConfig.byteArrayToInt(Arrays.copyOfRange(message, 1, 5));
                        this.payload = new byte[len - 5];
                        System.arraycopy(message, 5, this.payload, 0, len - 5);
                    }
                    break;
            }
        } else {
            this.typeID = -1; // Unknown type
        }
    }
}