package com.example.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.server.IAddValue;

public class RemoteService extends Service {


    private final IAddValue.Stub bind = new IAddValue.Stub() {
        @Override
        public int add(int a, int b) throws RemoteException {
            return a + b;
        }
    };


    public RemoteService() {

    }



    @Override
    public IBinder onBind(Intent intent) {
        Log.d("zjy", "onBind: ");
        return bind;
    }
}