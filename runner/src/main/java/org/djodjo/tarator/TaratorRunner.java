package org.djodjo.tarator;

import android.os.Bundle;
import android.support.test.runner.MonitoringInstrumentation;

import cucumber.api.android.CucumberInstrumentationCore;


public class TaratorRunner extends MonitoringInstrumentation
{
    private CucumberInstrumentationCore cucumberInstrumentationCore ;

    public TaratorRunner() {}

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
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
        waitForIdleSync();
        this.cucumberInstrumentationCore.start();

    }
}
