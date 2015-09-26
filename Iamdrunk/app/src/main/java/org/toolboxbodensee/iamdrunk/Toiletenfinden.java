package org.toolboxbodensee.iamdrunk;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

public class Toiletenfinden extends ActionBarActivity {
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

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toiletenfinden);

        loadDB();
        listView = (ListView)findViewById(R.id.listToilette);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        listView.setAdapter(adapter);
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
            /*TextView textView = (TextView)findViewById(R.id.testedit);
            textView.setText(text);*/

            String[] getrennt = text.split(",");
            adapter.clear();
            for(int counter=0; counter<getrennt.length-3; counter+=4)
                adapter.add(getrennt[counter]+getrennt[counter+1]+getrennt[counter+2]+getrennt[counter+3]);


        }

        protected void onProgressUpdate(Integer... progress) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressLoad);
            progressBar.setProgress(progress[0]);
        }
    }
}
