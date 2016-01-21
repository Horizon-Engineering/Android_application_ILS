package com.allin.activity.action;

import android.app.Application;

public class ApplicationContext extends Application {
    private static ApplicationContext instance;

    static {
        instance = null;
    }

    public static ApplicationContext getInstance() {
        if (instance == null) {
            synchronized (ApplicationContext.class) {
                if (instance == null) {
                    instance = new ApplicationContext();
                }
            }
        }
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
