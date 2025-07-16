package com.example.coroutine;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

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
        return bind;
    }
}