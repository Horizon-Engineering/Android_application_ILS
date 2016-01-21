package com.homa.hls.datadeal;

import android.support.v4.view.MotionEventCompat;
import android.util.Log;

public class UserPacket {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static byte HEADERSIZE;
    private int checkSum;
    private byte command;
    private byte[] data;
    private short dataLength;
    private byte dataType;
    private byte endFlag;
    private byte reserved1;
    private byte reserved2;
    private byte reserved3;
    private byte response;
    private byte startFlag;

    static {
        boolean z;
        if (UserPacket.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
        HEADERSIZE = (byte) 12;
    }

    private UserPacket() {
        this.startFlag = (byte) 28;
        this.dataType = (byte) 32;
        this.dataLength = (short) 0;
        this.command = (byte) 0;
        this.response = (byte) 0;
        this.reserved1 = (byte) 0;
        this.reserved2 = (byte) 0;
        this.data = null;
        this.checkSum = -1;
        this.reserved3 = (byte) 0;
        this.endFlag = (byte) 28;
    }

    public static UserPacket createPacket(byte dataType, byte command, byte reserved1, byte reserved2, byte[] data) {
        UserPacket packet = new UserPacket();
        if ($assertionsDisabled || packet != null) {
            packet.command = command;
            packet.reserved1 = reserved1;
            packet.reserved2 = reserved2;
            if (data != null) {
                packet.data = data;
                packet.dataLength = (short) (data.length & 65535);
            }
            packet.dataType = dataType;
            packet.checkSum = (((((((packet.startFlag + packet.dataType) + packet.dataLength) + packet.command) + packet.response) + packet.reserved1) + packet.reserved2) + packet.reserved3) + packet.endFlag;
            Log.i("packet.checkSum:", new StringBuilder(String.valueOf(packet.checkSum)).toString());
            return packet;
        }
        throw new AssertionError();
    }

    public static byte[] decodePacket(UserPacket packet) {
        if ($assertionsDisabled || packet != null) {
            byte[] data = new byte[(((short) (packet.dataLength & 65535)) + 12)];
            if ($assertionsDisabled || data != null) {
                int i = 0 + 1;
                data[0] = packet.startFlag;
                int i2 = i + 1;
                data[i] = packet.dataType;
                i = i2 + 1;
                data[i2] = (byte) packet.dataLength;
                i2 = i + 1;
                data[i] = (byte) (packet.dataLength >> 8);
                i = i2 + 1;
                data[i2] = packet.command;
                i2 = i + 1;
                data[i] = packet.response;
                i = i2 + 1;
                data[i2] = packet.reserved1;
                i2 = i + 1;
                data[i] = packet.reserved2;
                if (packet.data != null) {
                    System.arraycopy(packet.data, 0, data, i2, packet.data.length);
                    i2 = packet.data.length + 8;
                }
                i = i2 + 1;
                data[i2] = (byte) ((packet.checkSum >> 8) & MotionEventCompat.ACTION_MASK);
                i2 = i + 1;
                data[i] = (byte) (packet.checkSum & MotionEventCompat.ACTION_MASK);
                i = i2 + 1;
                data[i2] = packet.reserved3;
                i2 = i + 1;
                data[i] = packet.endFlag;
                return data;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static UserPacket encodePacket(byte[] data) {
        if (!$assertionsDisabled && data == null) {
            throw new AssertionError();
        } else if ($assertionsDisabled || data.length > 0) {
            UserPacket packet = new UserPacket();
            int index = 0 + 1;
            packet.startFlag = data[0];
            int index2 = index + 1;
            packet.dataType = data[index];
            index = index2 + 1;
            packet.dataLength = (short) (data[index2] & MotionEventCompat.ACTION_MASK);
            index2 = index + 1;
            packet.dataLength = (short) (packet.dataLength | (data[index] << 8));
            index = index2 + 1;
            packet.command = data[index2];
            index2 = index + 1;
            packet.response = data[index];
            index = index2 + 1;
            packet.reserved1 = data[index2];
            index2 = index + 1;
            packet.reserved2 = data[index];
            packet.data = new byte[packet.dataLength];
            System.arraycopy(data, index2, packet.data, 0, packet.dataLength);
            index = index2 + 1;
            packet.checkSum = (byte) (data[index2] << 8);
            index2 = index + 1;
            packet.checkSum = (byte) (packet.checkSum | (data[index] & MotionEventCompat.ACTION_MASK));
            index = index2 + 1;
            packet.reserved3 = data[index2];
            index2 = index + 1;
            packet.endFlag = data[index];
            return packet;
        } else {
            throw new AssertionError();
        }
    }

    public byte getStartFlag() {
        return this.startFlag;
    }

    public byte getDataType() {
        return this.dataType;
    }

    public short getDataLength() {
        return this.dataLength;
    }

    public byte getCommand() {
        return this.command;
    }

    public byte getResponse() {
        return this.response;
    }

    public byte getReserved1() {
        return this.reserved1;
    }

    public byte getReserved2() {
        return this.reserved2;
    }

    public byte[] getData() {
        return this.data;
    }

    public int getCheckSum() {
        return this.checkSum;
    }

    public byte getReserved3() {
        return this.reserved3;
    }

    public byte getEndFlag() {
        return this.endFlag;
    }
}
