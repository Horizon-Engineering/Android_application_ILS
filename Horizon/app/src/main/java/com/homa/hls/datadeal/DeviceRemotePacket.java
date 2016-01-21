package com.homa.hls.datadeal;

import android.support.v4.view.MotionEventCompat;
import com.allin.activity.action.SysApplication;

public class DeviceRemotePacket {
    static final /* synthetic */ boolean $assertionsDisabled;
    private byte[] MacAddress;
    private byte checkSum;
    private byte commandsequence;
    private byte[] data;
    private byte dataType;
    private byte datalenght;
    private byte[] password;

    static {
        $assertionsDisabled = !DeviceRemotePacket.class.desiredAssertionStatus();
    }

    public DeviceRemotePacket() {
        this.dataType = (byte) 0;
        this.MacAddress = null;
        this.password = null;
        this.datalenght = (byte) 0;
        this.commandsequence = (byte) 0;
        this.data = null;
        this.checkSum = (byte) 0;
    }

    public static DeviceRemotePacket createRemtoPacket(byte dataType, byte[] MacAddress, byte[] password, byte datalenght, byte[] data, byte sequence) {
        DeviceRemotePacket packet = new DeviceRemotePacket();
        if ($assertionsDisabled || packet != null) {
            int i;
            packet.dataType = dataType;
            short checkSum = (short) (packet.dataType + (short) 0);
            if (MacAddress != null) {
                packet.MacAddress = new byte[MacAddress.length];
                for (i = 0; i < MacAddress.length; i++) {
                    packet.MacAddress[i] = MacAddress[i];
                    checkSum = (short) (MacAddress[i] + checkSum);
                }
            }
            if (password != null) {
                packet.password = new byte[password.length];
                for (i = 0; i < password.length; i++) {
                    packet.password[i] = password[i];
                    checkSum = (short) (password[i] + checkSum);
                }
            }
            packet.datalenght = datalenght;
            checkSum = (short) (checkSum + datalenght);
            if (new Byte(sequence) == null) {
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
                packet.commandsequence = sequence;
            }
            checkSum = (short) (packet.commandsequence + checkSum);
            if (data != null) {
                packet.data = new byte[data.length];
                for (i = 0; i < data.length; i++) {
                    packet.data[i] = data[i];
                    checkSum = (short) (data[i] + checkSum);
                }
            }
            packet.checkSum = (byte) (checkSum & MotionEventCompat.ACTION_MASK);
            return packet;
        }
        throw new AssertionError();
    }

    public static DeviceRemotePacket encodePacket(byte[] data) {
        if ($assertionsDisabled || data != null) {
            DeviceRemotePacket packet = new DeviceRemotePacket();
            if ($assertionsDisabled || packet != null) {
                if (data.length > 0) {
                    short index = (short) 1;
                    packet.dataType = data[(short) 0];
                    packet.MacAddress = new byte[12];
                    System.arraycopy(data, index, packet.MacAddress, 0, 12);
                    short index2 = (short) (packet.MacAddress.length + index);
                    packet.password = new byte[8];
                    System.arraycopy(data, index2, packet.password, 0, 8);
                    index2 = (short) (packet.password.length + index2);
                    index = (short) (index2 + 1);
                    packet.datalenght = data[index2];
                    index2 = (short) (index + 1);
                    packet.commandsequence = data[index];
                    if (packet.datalenght > 0) {
                        packet.data = new byte[packet.datalenght];
                        System.arraycopy(data, index2, packet.data, 0, packet.datalenght);
                        index2 = (short) (packet.datalenght + index2);
                    }
                    index = (short) (index2 + 1);
                    packet.checkSum = data[index2];
                    index2 = index;
                }
                return packet;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static byte[] decodePacket(DeviceRemotePacket packet) {
        if ($assertionsDisabled || packet != null) {
            byte[] data;
            int index;
            if (packet.datalenght > 0) {
                data = new byte[(packet.datalenght + 24)];
            } else {
                data = new byte[24];
            }
            int index2 = 0 + 1;
            data[0] = packet.dataType;
            if (packet.MacAddress == null || packet.MacAddress.length <= 0) {
                index = index2;
            } else {
                System.arraycopy(packet.MacAddress, 0, data, index2, packet.MacAddress.length);
                index = packet.MacAddress.length + 1;
            }
            if (packet.password != null && packet.password.length > 0) {
                System.arraycopy(packet.password, 0, data, index, packet.password.length);
                index += packet.password.length;
            }
            index2 = index + 1;
            data[index] = packet.datalenght;
            index = index2 + 1;
            data[index2] = packet.commandsequence;
            if (packet.datalenght > 0) {
                System.arraycopy(packet.data, 0, data, index, packet.datalenght);
                index += packet.datalenght;
            }
            data[index] = packet.checkSum;
            return data;
        }
        throw new AssertionError();
    }

    public byte getDataType() {
        return this.dataType;
    }

    public void setDataType(byte dataType) {
        this.dataType = dataType;
    }

    public byte[] getMacAddress() {
        return this.MacAddress;
    }

    public void setMacAddress(byte[] macAddress) {
        this.MacAddress = macAddress;
    }

    public byte[] getPassword() {
        return this.password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public byte getDatalenght() {
        return this.datalenght;
    }

    public void setDatalenght(byte datalenght) {
        this.datalenght = datalenght;
    }

    public byte getCommandsequence() {
        return this.commandsequence;
    }

    public void setCommandsequence(byte commandsequence) {
        this.commandsequence = commandsequence;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte getCheckSum() {
        return this.checkSum;
    }

    public void setCheckSum(byte checkSum) {
        this.checkSum = checkSum;
    }
}
