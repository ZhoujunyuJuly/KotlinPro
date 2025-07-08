package com.example.coroutine.jetpack.databinding.model.field;

import android.util.Log;

import androidx.databinding.ObservableField;

import com.example.coroutine.jetpack.databinding.model.observable.User;

public class UserViewModelField {
    User user;
    ObservableField<User> userObservableField;

    public UserViewModelField() {
        user = new User("Andrew");
        userObservableField = new ObservableField<>();
        userObservableField.set(user);
    }

    public String getUserName(){
        return userObservableField.get().name;
    }

    public void setName(String userName){
        Log.d("zjy","field:" + userName);
        userObservableField.get().name = userName;
    }
}
