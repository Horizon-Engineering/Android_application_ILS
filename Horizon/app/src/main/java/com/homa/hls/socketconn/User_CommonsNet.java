package com.homa.hls.socketconn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class User_CommonsNet {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static Socket client;
    CommonsNet commonsNet;
    private String mIP;
    private int mPort;
    private DataInputStream recvHandle;
    private DataOutputStream sendHandle;

    static {
        $assertionsDisabled = !User_CommonsNet.class.desiredAssertionStatus();
        client = null;
    }

    public User_CommonsNet() {
        this.commonsNet = null;
        this.sendHandle = null;
        this.recvHandle = null;
        this.mIP = "";
        this.mPort = 0;
    }

    public boolean init(String ip, int port, int timeout) {
        try {
            this.mIP = ip;
            this.mPort = port;
            client = new Socket();
            client.connect(new InetSocketAddress(ip, port), timeout);
            InputStream in = client.getInputStream();
            this.sendHandle = new DataOutputStream(client.getOutputStream());
            this.recvHandle = new DataInputStream(in);
            if (this.recvHandle == null || this.sendHandle == null) {
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean init(Socket socket) throws IOException {
        if ($assertionsDisabled || socket != null) {
            this.mIP = socket.getInetAddress().toString();
            this.mIP = this.mIP.substring(1, this.mIP.length());
            this.mPort = socket.getPort();
            InputStream in = socket.getInputStream();
            this.sendHandle = new DataOutputStream(socket.getOutputStream());
            if (this.sendHandle == null) {
                return false;
            }
            this.recvHandle = new DataInputStream(in);
            if (this.recvHandle != null) {
                return true;
            }
            return false;
        }
        throw new AssertionError();
    }

    public void unInit() throws IOException {
        if (this.sendHandle != null) {
            this.sendHandle.close();
        }
        if (this.recvHandle != null) {
            this.recvHandle.close();
        }
    }

    public boolean send(byte[] data) throws IOException {
        if (this.sendHandle == null) {
            return false;
        }
        this.sendHandle.write(data);
        this.sendHandle.flush();
        return true;
    }

    public int recv(byte[] data) throws IOException {
        if (!$assertionsDisabled && this.recvHandle == null) {
            throw new AssertionError();
        } else if (data != null) {
            return this.recvHandle.read(data);
        } else {
            return -1;
        }
    }

    public int recv(byte[] data, int offset, int length) throws IOException {
        if (this.recvHandle == null || data == null) {
            return -1;
        }
        return this.recvHandle.read(data, offset, length);
    }

    public String getIp() {
        return this.mIP;
    }
}
