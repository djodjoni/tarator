package org.djodjo.tarator.example.instr;

import android.os.Bundle;

import org.djodjo.tarator.runner.TaratorRunner;

public class UnitInstr extends TaratorRunner {

    @Override
    public void onCreate(final Bundle bundle) {
        bundle.putString("features", "features/unit");
       // bundle.putString("glue", "org.djodjo.tarator.example.gherkin.unit");
        bundle.putString("glue", "org.djodjo.tarator.cucumber");

        super.onCreate(bundle);
    }

}
