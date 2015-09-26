package org.toolboxbodensee.iamdrunk;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Toiletenfinden extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toiletenfinden);
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
        }
    }
}
