package org.djodjo.tarator.example.instr;

import android.os.Bundle;

import org.djodjo.tarator.runner.TaratorRunner;

public class RegressionInstr extends TaratorRunner {

    @Override
    public void onCreate(final Bundle bundle) {
        bundle.putString("features", "features");
        bundle.putString("glue", "org.djodjo.tarator.example.gherkin");
        
        super.onCreate(bundle);
    }

}
