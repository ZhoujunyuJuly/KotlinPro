package com.example.server.dagger;

import android.app.Activity;

import dagger.Component;

@Component
public interface ApplicationComponent {

    void inject(Activity activity);
}
