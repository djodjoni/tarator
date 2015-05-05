package org.djodjo.tarator.example;

import android.app.Application;

import org.djodjo.tarator.example.api.MyJus;


public class JusApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        MyJus.init(this);

    }


}
