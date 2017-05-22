package com.example.leonard.picbrowser;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by leonard on 17/5/22.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
    }
}
