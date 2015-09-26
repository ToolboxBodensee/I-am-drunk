package org.toolboxbodensee.iamdrunk;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class LiegestutzenActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liegestutzen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
