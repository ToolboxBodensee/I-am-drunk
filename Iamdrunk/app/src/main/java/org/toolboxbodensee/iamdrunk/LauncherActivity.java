package org.toolboxbodensee.iamdrunk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LauncherActivity extends Activity {
    ListView appList;
    String[] apps = new String[]{
            "Telefon",
            "DB-Navigator",
            "Browser"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        appList = (ListView)findViewById(R.id.appList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.simple_list_item, android.R.id.text1, apps);


        appList.setAdapter(adapter);

        appList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView) view).getText().toString();

                switch (item){
                    case "Telefon":
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        startActivity(intent);
                        break;
                    case "DB-Navigator":
                        break;
                    case "Browser":
                        break;
                }
            }
        });
    }

}
