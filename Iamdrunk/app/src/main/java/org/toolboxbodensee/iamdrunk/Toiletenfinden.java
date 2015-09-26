package org.toolboxbodensee.iamdrunk;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Toiletenfinden extends Activity implements LocationListener{
    ListView listView;
    String[] getrennt = new String[9000];
    String[] koord = new String[3000];

    private LocationManager locationManager;
    private String provider;

    float lat = 0;
    float lon = 0;

    private Location toiletLocation = new Location("point A");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toiletenfinden);

        loadDB();

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        }

        listView = (ListView)findViewById(R.id.listToilette);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView) view).getText().toString();

                int start = getrennt[position*4].indexOf('.')-2;
                if(start == -3)
                    start = 0;
                double lang = Double.parseDouble(getrennt[position * 4].substring(start));
                double longi = Double.parseDouble(getrennt[position * 4 + 1]);


                Log.e("dsds", lang+","+longi);
               // Toast.makeText(getApplicationContext(), y_koord[0]+","+y_koord[1], Toast.LENGTH_SHORT).show();
                String url = "https://www.google.de/maps/@"+koord[position]+",20z?hl=de";
                Log.e("URL", url);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
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

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        //int lat = (int) (location.getLatitude());
        //int lng = (int) (location.getLongitude());
        /*latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));*/
        lat = (float)location.getLatitude();
        lon = (float)location.getLongitude();
        //Toast.makeText(this, Integer.toString(lat)+";"+Integer.toString(lng), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    class DownloadDBTask extends AsyncTask<URL, Integer, String> {

        private Exception exception;

        @Override
        protected String doInBackground(URL... url) {
            try {
                publishProgress((int) 0);
                Scanner s = new Scanner(url[0].openStream(), "ISO-8859-1");
                String text = "";
                publishProgress(2);
                int counter=0, step =0;
                while (s.hasNextLine()) {
                    text += s.nextLine();
                    if(step++>21){
                        counter++;
                        step=0;
                    }

                    publishProgress(counter);
                }
                publishProgress((int) 100);
                return text;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String text) {
            /*String[]*/ getrennt = text.split("[,]");

            String[] name = new String[getrennt.length/4];
            Map<Integer, String> unsortMap = new HashMap<Integer, String>();
            int counter=0;

            int start = 0;
            for (counter=0; counter<getrennt.length/4; counter++){
                start = getrennt[counter*4].indexOf('.')-2;
                if(start == -3)
                    start = 0;
                String lang = getrennt[counter*4].substring(start);

                toiletLocation.setLatitude(Float.parseFloat(lang));
                toiletLocation.setLongitude(Float.parseFloat(getrennt[counter * 4 + 1]));
                Location currentLoc = locationManager.getLastKnownLocation(provider);

                unsortMap.put((int)toiletLocation.distanceTo(currentLoc), getrennt[counter*4 + 2]+";"+lang+","+getrennt[counter * 4 + 1]);

            }
            Map<Integer, String> treeMap = new TreeMap<Integer, String>(
                    new Comparator<Integer>() {

                        @Override
                        public int compare(Integer o1, Integer o2) {
                            return o1.compareTo(o2);
                        }

                    });
            treeMap.putAll(unsortMap);

            counter=0;

            for (Map.Entry<Integer, String> entry : treeMap.entrySet()) {
                String[] tmp = entry.getValue().split(";");
                name[counter] = tmp[0] + ": "+(entry.getKey()/1000) + "km";
                koord[counter++] = tmp[1];
            }






            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.simple_list_item, android.R.id.text1, name);


            listView.setAdapter(adapter);

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressLoad);
            progressBar.setVisibility(View.INVISIBLE);


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

    static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
}
