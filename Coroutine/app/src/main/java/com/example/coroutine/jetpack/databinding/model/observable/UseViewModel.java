package com.example.coroutine.jetpack.databinding.model.observable;

import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.coroutine.BR;

public class UseViewModel extends BaseObservable {
    private User user;

    public UseViewModel() {
        this.user = new User("Jack");
    }

    /**
     * 只有在这里 @Bindable 了
     * 下面notifyPropertyChanged(BR.userName); 才可以链接上
     * @return
     */
    @Bindable
    public String getUserName(){
        return user.name;
    }

    public void setUserName(String userName){
        user.name = userName;
        Log.d("zjy","user = " + userName);
        notifyPropertyChanged(BR.userName);
    }

}
