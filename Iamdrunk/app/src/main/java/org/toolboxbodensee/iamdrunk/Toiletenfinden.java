package org.toolboxbodensee.iamdrunk;

<<<<<<< HEAD
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
=======
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.io.IOException;
>>>>>>> b8f4e969978645f40677926255101a41b3965cc1
import java.net.URL;
import java.util.Scanner;

public class Toiletenfinden extends ActionBarActivity {
<<<<<<< HEAD
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
=======
>>>>>>> b8f4e969978645f40677926255101a41b3965cc1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toiletenfinden);
<<<<<<< HEAD

        loadDB();
        listView = (ListView)findViewById(R.id.listToilette);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
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
            /*TextView textView = (TextView)findViewById(R.id.testedit);
            textView.setText(text);*/

            String[] getrennt = text.split(",");
            String[] 
            for(int counter=0; counter<getrennt.length-3; counter+=4){

            }
        }

        protected void onProgressUpdate(Integer... progress) {
            ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressLoad);
            progressBar.setProgress(progress[0]);
=======
    }

    private void loadDB(){
        try {
            URL url = new URL("http://www.gratispinkeln.de/loadmarkers.php");
            Scanner s = new Scanner(url.openStream());
            // read from your scanner

            String text  = s.toString();

            

        }
        catch(IOException ex) {
            // there was some connection problem, or the file did not exist on the server,
            // or your URL was not in the right format.
            // think about what to do now, and put it here.
            ex.printStackTrace(); // for now, simply output it.
>>>>>>> b8f4e969978645f40677926255101a41b3965cc1
        }
    }
}
