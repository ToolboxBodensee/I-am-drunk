package org.toolboxbodensee.iamdrunk;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class PromilleRechnerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promille_rechner);
        Button berechnen = (Button)findViewById(R.id.promille_berechnen);
        berechnen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView ergebnis = (TextView)findViewById(R.id.promille_ergebnis);
                Switch geschlecht = (Switch)findViewById(R.id.geschlecht);
                EditText korpergewicht = (EditText)findViewById(R.id.korpergewicht_input);
                EditText zeit = (EditText)findViewById(R.id.zeit_input);
                EditText e [] [] = new EditText[3][5];
                e[0][0] = (EditText)findViewById(R.id.alkoholgehalt_input);
                e[0][1] = (EditText)findViewById(R.id.alkoholgehalt_input2);
                e[0][2] = (EditText)findViewById(R.id.alkoholgehalt_input3);
                e[0][3] = (EditText)findViewById(R.id.alkoholgehalt_input4);
                e[0][4] = (EditText)findViewById(R.id.alkoholgehalt_input5);
                e[1][0] = (EditText)findViewById(R.id.menge_input);
                e[1][1] = (EditText)findViewById(R.id.menge_input2);
                e[1][2] = (EditText)findViewById(R.id.menge_input3);
                e[1][3] = (EditText)findViewById(R.id.menge_input4);
                e[1][4] = (EditText)findViewById(R.id.menge_input5);
                e[2][0] = (EditText)findViewById(R.id.anzahl_input);
                e[2][1] = (EditText)findViewById(R.id.anzahl_input2);
                e[2][2] = (EditText)findViewById(R.id.anzahl_input3);
                e[2][3] = (EditText)findViewById(R.id.anzahl_input4);
                e[2][4] = (EditText)findViewById(R.id.anzahl_input5);
                double alkoholmenge = 0;
                for(int i = 0; i < 5; i++)
                    alkoholmenge += 0.8 * Double.parseDouble(e[0][i].getText().toString())*Double.parseDouble(e[1][i].getText().toString())* Integer.parseInt(e[2][i].getText().toString())*10;   //Blutalkohol(ml)

                //Toast.makeText(getApplicationContext(), alkoholmenge +"", Toast.LENGTH_LONG).show();
                double promille_ergebnis;
                if(geschlecht.isChecked())
                    promille_ergebnis = alkoholmenge/(Double.parseDouble(korpergewicht.getText().toString())*0.55) - Double.parseDouble(zeit.getText().toString())*0.15;
                else
                    promille_ergebnis = alkoholmenge/(Double.parseDouble(korpergewicht.getText().toString())*0.68) - Double.parseDouble(zeit.getText().toString())*0.15;

                    ergebnis.setText("Blutalkohol: "+promille_ergebnis+"‰");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_promille_rechner, menu);
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
