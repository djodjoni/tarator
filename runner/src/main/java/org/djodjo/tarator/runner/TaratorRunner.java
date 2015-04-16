package org.djodjo.tarator.runner;

import android.os.Bundle;
import android.support.test.runner.MonitoringInstrumentation;
import android.util.Log;

import cucumber.api.android.CucumberInstrumentationCore;


public class TaratorRunner extends MonitoringInstrumentation
{
    //AndroidJUnitRunner
    private CucumberInstrumentationCore cucumberInstrumentationCore ;

    private static final String TAG="TaratorRunner";

    public TaratorRunner() {}

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        Log.d(TAG, "tarator-runner on Create");
        cucumberInstrumentationCore = new CucumberInstrumentationCore(this);
        // try {
        this.cucumberInstrumentationCore.create(bundle);
        // } catch (Exception ex) { ex.printStackTrace(); }
        start();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Log.d(TAG, "tarator-runner on Start");
        waitForIdleSync();
        this.cucumberInstrumentationCore.start();

    }

    @Override
    public void finish(int resultCode, Bundle results) {
        Log.d(TAG, "tarator-runner finishing, resultCode: " + resultCode);
        Log.d(TAG, "tarator-runner finishing, results: " + results.toString());
        //todo smth
        super.finish(resultCode, results);


    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "tarator-runner on Destroy");
        super.onDestroy();
    }




}
