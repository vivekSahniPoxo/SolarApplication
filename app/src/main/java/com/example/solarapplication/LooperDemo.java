package com.example.solarapplication;

import android.os.Handler;
import android.os.HandlerThread;

public class LooperDemo extends HandlerThread {
    Handler handler;

    public LooperDemo() {
        super("name");
        start();
        handler = new Handler(getLooper());
    }

    public LooperDemo execute(Runnable task) {
        handler.post(task);
        return this;

    }
    
}
