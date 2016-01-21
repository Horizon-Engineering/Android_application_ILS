package com.homa.hls.datadeal;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class UserMessage {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final int MAX_ID = 255;
    private static final int MAX_SEND = 2;
    private static AtomicInteger MSG_ID = null;
    private static final int TIME_INTERVAL = 4000;
    private short id;
    private byte[] lock;
    private Object packet;
    private byte reserved;
    private int sendCounter;
    private Object source;
    private boolean state;
    private Timer timer;
    private byte[] waitEvent;

    static {
        $assertionsDisabled = !UserMessage.class.desiredAssertionStatus() ? true : false;
        MSG_ID = new AtomicInteger(0);
    }

    private UserMessage() {
        this.waitEvent = null;
        this.waitEvent = new byte[0];
        this.reserved = (byte) 0;
        this.packet = null;
        this.timer = null;
        this.lock = new byte[0];
        this.state = $assertionsDisabled;
    }

    public static UserMessage createMessage(Object packet, Object source) {
        UserMessage message = new UserMessage();
        message.packet = packet;
        message.source = source;
        message.id = (byte) MSG_ID.get();
        return message;
    }

    public static byte registerMsg() {
        if ($assertionsDisabled || MSG_ID != null) {
            MSG_ID.compareAndSet(MAX_ID, 0);
            MSG_ID.incrementAndGet();
            return (byte) (MSG_ID.get() & MAX_ID);
        }
        throw new AssertionError();
    }

    public short getId() {
        return this.id;
    }

    public Object getPacket() {
        return this.packet;
    }

    public Object getSource() {
        return this.source;
    }

    public void sended() {
        synchronized (this.lock) {
            this.sendCounter++;
        }
    }

    public int getSendCounter() {
        int res;
        synchronized (this.lock) {
            res = this.sendCounter;
        }
        return res;
    }

    public boolean isValid() {
        boolean res = true;
        synchronized (this.lock) {
            if (this.sendCounter >= MAX_SEND) {
                res = $assertionsDisabled;
            }
        }
        return res;
    }

    public void startTimer(TimerTask timerProc) {
        this.timer = new Timer();
        this.timer.schedule(timerProc, 4000);
    }

    public boolean waitDone() throws InterruptedException {
        boolean z;
        synchronized (this.waitEvent) {
            this.waitEvent.wait();
            z = this.state;
        }
        return z;
    }

    public void done(boolean state) {
        synchronized (this.waitEvent) {
            if ($assertionsDisabled || this.timer != null) {
                this.timer.cancel();
                this.state = state;
                this.waitEvent.notify();
            } else {
                throw new AssertionError();
            }
        }
    }
}
