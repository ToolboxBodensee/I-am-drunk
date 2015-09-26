package org.toolboxbodensee.iamdrunk;

import android.app.Activity;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class GeradeLaufenMinispielActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    float ValueX;
    float ValueY;
    float ValueZ;
    float AccelerationXtreshold;
    int measurements;
    int invalidMeasurements;
    TextView score;

    public void onSensorChanged(SensorEvent event) {
        ValueX = event.values[0];
        ValueY = event.values[1];
        ValueZ = event.values[2];


    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerade_laufen_minispiel);
        Button startbutton = (Button) findViewById(R.id.button_start_geradelaufen);
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startgame();
            }
        });
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        SharedPreferences settings = getSharedPreferences("GeradeLaufenConfig", 0);
        float AccelerationXtreshold = settings.getFloat("AccelerationXtreshold", (float)5.0);
        score = (TextView) findViewById(R.id.score);
    }

    void startgame()
    {
        final CountDownTimer countDownTimer = new CountDownTimer(5000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                measure();
            }

            @Override
            public void onFinish() {
                score.setText(correctMeasurementsPercent() + "");
                Log.d("game", correctMeasurementsPercent() + "");
            }
        };
        countDownTimer.start();

    }


    void measure()
    {
        Log.d("Game", "X: " + ValueX);
        Log.d("Game", "Y: " + ValueY);
        Log.d("Game", "Z: " + ValueZ);
        if(ValueX > AccelerationXtreshold || ValueX < AccelerationXtreshold * (-1))
        {
            invalidMeasurements++;
        }


    }

    Float correctMeasurementsPercent()
    {
        return (float) invalidMeasurements/measurements;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gerade_laufen_minispiel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
