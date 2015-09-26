package org.toolboxbodensee.iamdrunk;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Toiletenfinden extends Activity {
    ListView listView;
    String[] values = new String[] { "Android List View",
            "Adapter implementation",
            "Simple List View In Android",
            "Create List View Android",
            "Android Example",
            "List View Source Code",
            "List View Array Adapter",
            "Android Example List View"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toiletenfinden);

        loadDB();
        listView = (ListView)findViewById(R.id.listToilette);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.simple_list_item, android.R.id.text1, values);


        listView.setAdapter(adapter);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void loadDB(){
        URL url = null;
        try {
            url = new URL("http://www.gratispinkeln.de/loadmarkers.php");
            new DownloadDBTask().execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    class DownloadDBTask extends AsyncTask<URL, Integer, String> {

        private Exception exception;

        @Override
        protected String doInBackground(URL... url) {
            try {
                publishProgress((int) 0);
                Scanner s = new Scanner(url[0].openStream());
                String text = "";
                publishProgress(2);
                while (s.hasNextLine())
                    text += s.nextLine();
                publishProgress((int) 100);
                return text;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String text) {
            String[] getrennt = text.split("[,]");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.simple_list_item, android.R.id.text1, getrennt);


            listView.setAdapter(adapter);


        }

        protected void onProgressUpdate(Integer... progress) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressLoad);
            progressBar.setProgress(progress[0]);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }
}
