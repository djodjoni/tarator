package org.djodjo.tarator.example.instr;

import android.os.Bundle;

import org.djodjo.tarator.runner.TaratorRunner;

public class ValidateInstr extends TaratorRunner {

    @Override
    public void onCreate(final Bundle bundle) {
        bundle.putString("features", "features/validation");
        bundle.putString("glue", "org.djodjo.tarator.example.gherkin.validation");
        bundle.putString("tags", "@newAndroid--~@skip--~@skipAndroid");

        super.onCreate(bundle);
    }

}
