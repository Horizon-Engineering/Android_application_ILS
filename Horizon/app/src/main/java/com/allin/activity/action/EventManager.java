package com.allin.activity.action;

import android.os.Handler;
import android.os.Message;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class EventManager {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static EventManager mInstance;
    private HashMap<String, ArrayList<EventEntry>> mEventTable;
    private byte[] mLock;

    private class EventEntry {
        private int mEvent;
        private Handler mReceiver;

        public EventEntry(int event, Handler receiver) {
            this.mEvent = event;
            this.mReceiver = receiver;
        }

        public int getEvent() {
            return this.mEvent;
        }

        public Handler getReceiver() {
            return this.mReceiver;
        }

        public void setEvent(int event) {
            this.mEvent = event;
        }

        public void setReceiver(Handler receiver) {
            this.mReceiver = receiver;
        }
    }

    static {
        $assertionsDisabled = !EventManager.class.desiredAssertionStatus();
    }

    private EventManager() {
        this.mLock = new byte[0];
        this.mEventTable = new HashMap();
    }

    public static EventManager getInstance() {
        if (mInstance == null) {
            mInstance = new EventManager();
        }
        return mInstance;
    }

    public void subscibeEvent(String owner, int event, Handler receiver) {
        synchronized (this.mLock) {
            if (!$assertionsDisabled && this.mEventTable == null) {
                throw new AssertionError();
            } else if ($assertionsDisabled || receiver != null) {
                EventEntry entry = new EventEntry(event, receiver);
                ArrayList<EventEntry> list;
                if (this.mEventTable.containsKey(owner)) {
                    list = (ArrayList) this.mEventTable.get(owner);
                    boolean exist = false;
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        EventEntry eventEntry = (EventEntry) it.next();
                        if (eventEntry.getEvent() == event) {
                            eventEntry.setReceiver(receiver);
                            exist = true;
                        }
                    }
                    if (!exist) {
                        list.add(entry);
                    }
                } else {
                    list = new ArrayList();
                    list.add(entry);
                    this.mEventTable.put(owner, list);
                }
            } else {
                throw new AssertionError();
            }
        }
    }

    public void removeEvent(String owner, int event) {
        synchronized (this.mLock) {
            if ($assertionsDisabled || this.mEventTable != null) {
                if (this.mEventTable.containsKey(owner)) {
                    Iterator<EventEntry> eeyIter = ((ArrayList) this.mEventTable.get(owner)).iterator();
                    while (eeyIter.hasNext()) {
                        if (((EventEntry) eeyIter.next()).getEvent() == event) {
                            eeyIter.remove();
                        }
                    }
                }
            } else {
                throw new AssertionError();
            }
        }
    }

    public void broadcastEvent(int event, int arg1, Object data) {
        synchronized (this.mLock) {
            for (Entry entry : this.mEventTable.entrySet()) {
                Iterator it = ((ArrayList) entry.getValue()).iterator();
                while (it.hasNext()) {
                    EventEntry eventEntry = (EventEntry) it.next();
                    if (eventEntry != null && eventEntry.getEvent() == event) {
                        Message msg = eventEntry.getReceiver().obtainMessage();
                        msg.what = event;
                        msg.obj = data;
                        msg.arg1 = arg1;
                        eventEntry.getReceiver().sendMessage(msg);
                    }
                }
            }
        }
    }
}
