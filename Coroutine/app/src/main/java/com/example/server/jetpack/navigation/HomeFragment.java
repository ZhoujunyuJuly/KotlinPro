package com.example.server.jetpack.navigation;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coroutine.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private int notificationId;
    private @NonNull Bundle mArgs;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //jumpToDetail(view);
                usePending();
            }
        });
    }

    /**
     * 跳转到详情页
     */
    private void jumpToDetail(View view){
        mArgs = new HomeFragmentArgs.Builder()
                .setName("Jack")
                .setAge(16)
                .build().toBundle();
        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_homeFragment_to_detailFragment, mArgs);

    }


    /**
     * 发送通知
     * @return
     */
    private void usePending(){
        NotificationChannel channel = new NotificationChannel(getActivity().getPackageName(),
                "channel", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("My NotificationChannel");
        NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
        Notification notification = new NotificationCompat.Builder(getActivity(),getActivity().getPackageName())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("点我")
                .setContentText("说不定有惊喜")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(getPendingIntent())
                .build();

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        NotificationManagerCompat.from(getActivity()).notify(notificationId++,notification);

    }


    private PendingIntent getPendingIntent() {
        return Navigation.findNavController(getActivity(),R.id.fragmentContainerView)
                .createDeepLink()
                .setGraph(R.navigation.my_nav_graph)
                .setDestination(R.id.detailFragment)
                .setArguments(mArgs)
                .createPendingIntent();
    }
}