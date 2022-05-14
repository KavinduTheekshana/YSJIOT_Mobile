package com.example.ysjiot;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class notofication extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do your jobs here
        return super.onStartCommand(intent, flags, startId);
    }
}
