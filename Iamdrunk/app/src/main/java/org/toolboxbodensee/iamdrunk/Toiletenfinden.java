package org.toolboxbodensee.iamdrunk;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Toiletenfinden extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toiletenfinden);

        loadDB();
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
                publishProgress((int)0);
                Scanner s = new Scanner(url[0].openStream());
                String text = "";
                publishProgress(2);
                while(s.hasNextLine())
                    text += s.nextLine();
                publishProgress((int)100);
                return text;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String text) {
            TextView textView = (TextView)findViewById(R.id.testedit);
            textView.setText(text);
        }

        protected void onProgressUpdate(Integer... progress) {
            ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressLoad);
            progressBar.setProgress(progress[0]);
        }
    }
}
