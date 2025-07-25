package com.example.server.framework;

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
import com.example.server.IAddValue;

/**
 * ‼️真的想鲨人啊，
 *
 * 🧠必须添加这个：
 * <manifest>
 *     <queries>
 *         <package android:name="com.example.server" />
 *     </queries>
 * </manifest>
 *
 * 🌟<queries> 是 Android 11 (API 30) 引入的一项权限控制机制
 *  - 用于声明你的应用有权“查询”哪些其他应用的组件信息。
 *  - 为了防止应用随便扫描设备上安装的其他应用。
 */
public class AIDLActivity extends AppCompatActivity {

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("zjy", "Service connected: " + name);
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
            Log.d("zjy", "Service disconnected: " + name);
            Toast.makeText(AIDLActivity.this,"Service disconnected: " + name,Toast.LENGTH_SHORT).show();
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
        intent.setComponent(new ComponentName(
                "com.example.server",
                "com.example.server.RemoteService"
        ));
        try {
//            Intent intent = new Intent("com.example.server.RemoteService");
//            intent.setPackage("com.example.server");
            boolean success = bindService(intent, connection, BIND_AUTO_CREATE);
            Toast.makeText(this,"绑定结果:" + success,Toast.LENGTH_SHORT).show();
            Log.d("zjy", "startService: " + success);
        }catch (Exception e){
            Log.d("zjy", "error: " + e);
        }

    }

    private void stopService(){
        unbindService(connection);
    }
}