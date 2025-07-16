package com.example.coroutine.framework;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.coroutine.R;
import com.example.coroutine.IAddValue;

public class AIDLActivity extends AppCompatActivity {

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //‼️记住最重要的是把整个文件夹包括包名都复制过来！
            IAddValue anInterface = IAddValue.Stub.asInterface(service);
            try {
                int result = anInterface.add(1,2);
                Toast.makeText(AIDLActivity.this,"等于" + result,Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aidlactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }

    private void startService(){
        Intent intent = new Intent();
        intent.setAction("com.example.coroutine.RemoteService");
        intent.setPackage("com.example.coroutine");
        intent.setComponent(new ComponentName("com.example.coroutine", "com.example.coroutine.RemoteService"));
        boolean success = bindService(intent,connection,BIND_AUTO_CREATE);
        Log.d("zjy", "startService: " + success);
    }

    private void stopService(){
        unbindService(connection);
    }
}