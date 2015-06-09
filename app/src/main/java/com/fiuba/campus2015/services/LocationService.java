package com.fiuba.campus2015.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.fiuba.campus2015.session.SessionManager;

import java.util.Timer;
import java.util.TimerTask;

public class LocationService extends Service {

    private IBinder mBinder = new SocketServerBinder();
    private Timer mTimer;
    private boolean mRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                if (mRunning) {
                    SessionManager sessionManager = new SessionManager(getApplicationContext());
                    LocationSender locationSender =new LocationSender();
                    locationSender.send(getApplicationContext(),sessionManager.getToken(),sessionManager.getUserid());
                }
            }
        }, 300000, 300000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mRunning = true;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        mRunning = true;
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mRunning = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        mRunning = false;
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        LocationSender locationSender =new LocationSender();
        locationSender.stop(sessionManager.getToken(),sessionManager.getUserid());
        super.onDestroy();
    }

    public class SocketServerBinder extends Binder {

        public LocationService getService() {
            return LocationService.this;
        }

    }
}
