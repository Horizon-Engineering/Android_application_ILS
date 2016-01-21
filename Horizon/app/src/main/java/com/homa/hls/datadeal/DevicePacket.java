package com.homa.hls.datadeal;

import android.support.v4.view.MotionEventCompat;
import com.allin.activity.action.SysApplication;

public class DevicePacket {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final byte HEADERSIZE = (byte) 5;
    private short address;
    private byte checkSum;
    private byte command;
    private byte commandsequence;
    private byte[] data;
    private byte dataType;
    private short datalenght;

    static {
        $assertionsDisabled = !DevicePacket.class.desiredAssertionStatus() ? true : false;
    }

    public DevicePacket() {
        this.dataType = (byte) 0;
        this.address = (short) 0;
        this.commandsequence = (byte) 0;
        this.command = (byte) 0;
        this.data = null;
        this.checkSum = (byte) 0;
        this.datalenght = (short) 0;
    }

    public static DevicePacket createPacket(byte dataType, short address, short commandsequence, byte[] data) {
        DevicePacket packet = new DevicePacket();
        if ($assertionsDisabled || packet != null) {
            packet.dataType = dataType;
            short checkSum = (short) (packet.dataType + (short) 0);
            byte hight = (byte) (address >> 8);
            byte low = (byte) (address & MotionEventCompat.ACTION_MASK);
            packet.address = address;
            checkSum = (short) (((short) (checkSum + hight)) + low);
            SysApplication.getInstance();
            SysApplication.getInstance();
            SysApplication.commandsequence++;
            SysApplication.getInstance();
            if (SysApplication.commandsequence == MotionEventCompat.ACTION_MASK) {
                SysApplication.getInstance();
                SysApplication.commandsequence = 1;
            }
            SysApplication.getInstance();
            packet.commandsequence = (byte) SysApplication.commandsequence;
            checkSum = (short) (packet.commandsequence + checkSum);
            packet.data = new byte[data.length];
            for (int i = 0; i < data.length; i++) {
                packet.data[i] = data[i];
                checkSum = (short) (data[i] + checkSum);
            }
            packet.checkSum = (byte) (checkSum & MotionEventCompat.ACTION_MASK);
            return packet;
        }
        throw new AssertionError();
    }

    public static DevicePacket createGatewayPacket(byte dataType, byte command, short datalenght, short commandsequence, byte[] data) {
        DevicePacket packet = new DevicePacket();
        packet.data = new byte[datalenght];
        if ($assertionsDisabled || packet != null) {
            packet.dataType = dataType;
            short checkSum = (short) (packet.dataType + (short) 0);
            packet.command = command;
            checkSum = (short) (packet.command + checkSum);
            packet.datalenght = datalenght;
            checkSum = (short) (packet.datalenght + checkSum);
            if (commandsequence == (short) 0) {
                SysApplication.getInstance();
                SysApplication.getInstance();
                SysApplication.commandsequence++;
                SysApplication.getInstance();
                if (SysApplication.commandsequence == MotionEventCompat.ACTION_MASK) {
                    SysApplication.getInstance();
                    SysApplication.commandsequence = 1;
                }
                SysApplication.getInstance();
                packet.commandsequence = (byte) SysApplication.commandsequence;
            } else {
                packet.commandsequence = (byte) commandsequence;
            }
            checkSum = (short) (packet.commandsequence + checkSum);
            if (datalenght > (short) 0) {
                for (int i = 0; i < data.length; i++) {
                    packet.data[i] = data[i];
                    checkSum = (short) (data[i] + checkSum);
                }
            }
            packet.checkSum = (byte) (checkSum & MotionEventCompat.ACTION_MASK);
            return packet;
        }
        throw new AssertionError();
    }

    public static DevicePacket encodePacket(byte[] data) {
        if ($assertionsDisabled || data != null) {
            DevicePacket packet = new DevicePacket();
            if ($assertionsDisabled || packet != null) {
                if (data.length > 0) {
                    short index;
                    short index2;
                    if (data[0] == 8) {
                        index = (short) 1;
                        packet.dataType = data[0];
                        index2 = (short) (index + 1);
                        packet.address = (short) ((data[index] & MotionEventCompat.ACTION_MASK) << 8);
                        index = (short) (index2 + 1);
                        packet.address = (short) (packet.address | (data[index2] & MotionEventCompat.ACTION_MASK));
                        index2 = (short) (index + 1);
                        packet.commandsequence = data[index];
                        packet.data = new byte[5];
                        System.arraycopy(data, index2, packet.data, 0, 5);
                        packet.checkSum = data[index2];
                    } else {
                        index = (short) 1;
                        packet.dataType = data[0];
                        index2 = (short) (index + 1);
                        packet.command = data[index];
                        index = (short) (index2 + 1);
                        packet.datalenght = (short) (data[index2] & MotionEventCompat.ACTION_MASK);
                        index2 = (short) (index + 1);
                        packet.commandsequence = data[index];
                        if (packet.datalenght > (short) 0) {
                            packet.data = new byte[packet.datalenght];
                            System.arraycopy(data, index2, packet.data, 0, packet.datalenght);
                            index2 = (short) (packet.datalenght + index2);
                        }
                        packet.checkSum = data[index2];
                    }
                }
                return packet;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static byte[] decodePacket(DevicePacket packet) {
        if ($assertionsDisabled || packet != null) {
            byte[] data;
            if (packet.data.length > 0) {
                data = new byte[(packet.data.length + 5)];
            } else {
                data = new byte[5];
            }
            if ($assertionsDisabled || data != null) {
                int index = 0 + 1;
                data[0] = packet.dataType;
                int i;
                if (packet.dataType == 1) {
                    i = index + 1;
                    data[index] = packet.command;
                    index = i + 1;
                    data[i] = (byte) packet.datalenght;
                    i = index + 1;
                    data[index] = packet.commandsequence;
                    if (packet.data.length > 0) {
                        System.arraycopy(packet.data, 0, data, i, packet.data.length);
                        i = packet.data.length + 4;
                    }
                    data[i] = packet.checkSum;
                } else if (packet.dataType == 4) {
                    i = index + 1;
                    data[index] = (byte) (packet.address >> 8);
                    index = i + 1;
                    data[i] = (byte) packet.address;
                    i = index + 1;
                    data[index] = packet.commandsequence;
                    if (packet.data.length > 0) {
                        System.arraycopy(packet.data, 0, data, i, packet.data.length);
                        i = packet.data.length + 4;
                    }
                    data[i] = packet.checkSum;
                }
                return data;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public byte getCommandsequence() {
        return this.commandsequence;
    }

    public void setCommandsequence(byte commandsequence) {
        this.commandsequence = commandsequence;
    }

    public byte getDataType() {
        return this.dataType;
    }

    public short getAddress() {
        return this.address;
    }

    public byte[] getData() {
        return this.data;
    }

    public byte getCommand() {
        return this.command;
    }

    public short getDatalenght() {
        return this.datalenght;
    }
}
