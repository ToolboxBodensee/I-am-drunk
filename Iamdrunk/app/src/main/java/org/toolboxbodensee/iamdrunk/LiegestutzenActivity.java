package org.toolboxbodensee.iamdrunk;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class LiegestutzenActivity extends Activity {
    Button btn;
    int counter=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liegestutzen);
        btn = (Button)findViewById(R.id.btnLiegestutz);
    }

    public void buttonPress(View view){

        btn.setText(Integer.toString(counter++)+" Liegestützen geschafft!");
    }

    public void fertigPress(View view){
        SharedPreferences settings = getSharedPreferences("LiegestutzenConfig", 0);
        int limit = settings.getInt("anzahl", 10);
        Button btnFertig = (Button)findViewById(R.id.btnFertig);
        if(counter<limit)
            btnFertig.setText("Du bist betrunken!");
        else
            btnFertig.setText("Du bist noch nüchtern!");

        btn.setText("Mach soviele Liegestützen wie du kannst und versuche dabei jedes mal mit der Nase den Bilschirm zu berühren!");
        counter=0;

    }

}
