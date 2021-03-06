package org.toolboxbodensee.iamdrunk;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout toilette = (LinearLayout)findViewById(R.id.toilette);
        LinearLayout finde = (LinearLayout)findViewById(R.id.finde);
        LinearLayout promille = (LinearLayout)findViewById(R.id.promille);
        LinearLayout liegestutzen = (LinearLayout)findViewById(R.id.liegestutzen);
        LinearLayout punktantippen = (LinearLayout) findViewById(R.id.punktantippen);
        LinearLayout geradelaufen = (LinearLayout)findViewById(R.id.geradelaufen);
        LinearLayout bildbeschreiben = (LinearLayout)findViewById(R.id.bildbeschreiben);
        toilette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Toiletenfinden.class);
                startActivity(intent);
            }
        });
        promille.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), PromilleRechnerActivity.class);
                startActivity(intent);

            }
        });
        finde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MeetPeopleActivity.class);
                startActivity(intent);
            }
        });
        liegestutzen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), LiegestutzenActivity.class);
                startActivity(intent);
            }
        });
        punktantippen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), PunktAntippenMinispielActivity.class);
                startActivity(intent);
            }
        });
        geradelaufen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), GeradeLaufenMinispielActivity.class);
                startActivity(intent);
            }
        });
        geradelaufen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), GeradeLaufenMinispielActivity.class);
                startActivity(intent);
            }
        });
        bildbeschreiben.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), DescribeImageActivity.class);
                startActivity(intent);
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if (id == R.id.action_launcher) {
            Intent intent = new Intent(this, LauncherActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
