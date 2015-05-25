package com.fiuba.campus2015.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LocationService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO mandar a la api la posicion del usuario
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO ver para que puede servir
        return null;
    }
}
