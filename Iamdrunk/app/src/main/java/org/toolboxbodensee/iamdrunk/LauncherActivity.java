package org.toolboxbodensee.iamdrunk;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class LauncherActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
