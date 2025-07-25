package com.example.server.jetpack.livecycle;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.List;

public class MyLocationObserver implements DefaultLifecycleObserver {
    private Context context;
    private LocationManager service;
    private MyLocationListener mListener;
    public MyLocationObserver(Context context) {
        this.context = context;
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    @Override
    public void onStart(@NonNull LifecycleOwner owner){
        if (service == null) {
            service = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        if (mListener == null) {
            mListener = new MyLocationListener();
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            service.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, mListener);
        } else {
            Log.e("MyLocationObserver", "定位权限未授权");
        }
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner){
        service.removeUpdates(mListener);
    }
    
    static class MyLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(@NonNull Location location) {
            Log.d("zjy","ddasfd"+ location.toString());
        }

        @Override
        public void onLocationChanged(@NonNull List<Location> locations) {
            LocationListener.super.onLocationChanged(locations);
            Log.d("zjy","ddd");
        }
    }
}
