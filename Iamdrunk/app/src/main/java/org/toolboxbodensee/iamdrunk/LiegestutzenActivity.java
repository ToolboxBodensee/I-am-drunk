package org.toolboxbodensee.iamdrunk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LiegestutzenActivity extends Activity implements SensorEventListener {
    TextView txtcount;
    int counter=1;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liegestutzen);
        txtcount = (TextView)findViewById(R.id.textViewCounter);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }


    public void fertigPress(View view){
        SharedPreferences settings = getSharedPreferences("LiegestutzenConfig", 0);
        int limit = settings.getInt("anzahl", 10);
        Button btnFertig = (Button)findViewById(R.id.btnFertig);
        if(counter<limit) {
            btnFertig.setText("Du bist betrunken!");
        }
        else {
            btnFertig.setText("Du bist noch nüchtern!");
            launchAppChooser();
        }


        counter=0;

        txtcount.setText(Integer.toString(counter));

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] == 0) {
            txtcount.setText(Integer.toString(counter++));
        }
    }

    private void launchAppChooser() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
