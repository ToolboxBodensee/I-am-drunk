package org.toolboxbodensee.iamdrunk;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

import ch.uepaa.p2pkit.ConnectionCallbacks;
import ch.uepaa.p2pkit.ConnectionResult;
import ch.uepaa.p2pkit.ConnectionResultHandling;
import ch.uepaa.p2pkit.KitClient;
import ch.uepaa.p2pkit.discovery.GeoListener;
import ch.uepaa.p2pkit.discovery.InfoTooLongException;
import ch.uepaa.p2pkit.discovery.P2pListener;
import ch.uepaa.p2pkit.discovery.Peer;
import ch.uepaa.p2pkit.messaging.MessageListener;

public class MeetPeopleActivity extends Activity implements LocationListener {

    String APP_KEY = "eyJzaWduYXR1cmUiOiJHaFhPL004bHdTa0JKVXpWc3ZJNFoxbEI2ODBYU2kzK2NYZHh1THFpRForTlBsMVU3eFJrZStKeGxjTFBYelBUY0Y4dmRrWXlPc3JqTy9DRkhydUJmUzBkZWIwdHVOaStZeUwrdU9jWmZlMENOR2VFalUxWm52clBPRkY0RXVNZkk0ZjRLS1ptWE9WQ0Q5Y2FDNU1TR044ZHBKeGQxNEpRK29QRkltUG4vRDg9IiwiYXBwSWQiOjEyNTcsInZhbGlkVW50aWwiOjE2Nzk0LCJhcHBVVVVJRCI6IjRCODRGMzYwLUUwRUItNEU4RC05RTcyLTkwRTAzRjlDNDYzNCJ9";

    private LocationManager locationManager;
    private String provider;
    ListView listView;
    ArrayAdapter<String> adapter;
    String[] values = new String[] { "Android List View",
            "Adapter implementation",
            "Simple List View In Android",
            "Create List View Android",
            "Android Example",
            "List View Source Code",
            "List View Array Adapter",
            "Android Example List View"
    };


    ArrayList<String[]> devices = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_people);
        enableKit();locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        listView = (ListView) findViewById(R.id.meet_people_list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, getDeviceString(devices));

        listView.setAdapter(adapter);

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


    private void enableKit() {

        final int statusCode = KitClient.isP2PServicesAvailable(this);
        if (statusCode == ConnectionResult.SUCCESS) {
            KitClient client = KitClient.getInstance(this);
            client.registerConnectionCallbacks(mConnectionCallbacks);

            if (client.isConnected()) {
                Log.d("p" , "Client already connected");
            } else {
                Log.d("p","Connecting P2PKit client");
                client.connect(APP_KEY);
            }
        } else {
            Log.d("p", "Cannot start P2PKit, status code: " + statusCode);
            ConnectionResultHandling.showAlertDialogForConnectionError(this, statusCode);
        }
    }

    private final ConnectionCallbacks mConnectionCallbacks = new ConnectionCallbacks() {

        @Override
        public void onConnected() {
            Log.d("p", "Successfully connected to P2P Services, with id: " + KitClient.getInstance(MeetPeopleActivity.this).getNodeId().toString());


                startP2pDiscovery();
            }


        @Override
        public void onConnectionSuspended() {
            Log.d("p", "Connection to P2P Services suspended");


        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.d("p","Connection to P2P Services failed with status: " + connectionResult.getStatusCode());
            ConnectionResultHandling.showAlertDialogForConnectionError(MeetPeopleActivity.this, connectionResult.getStatusCode());
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meet_people, menu);
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

        private void startP2pDiscovery() {
            try {
                Location currentLoc = locationManager.getLastKnownLocation(provider);
                String latLonStr = currentLoc.getLatitude() + "," + currentLoc.getLongitude();
                KitClient.getInstance(this).getDiscoveryServices().setP2pDiscoveryInfo(latLonStr.getBytes());
            } catch (InfoTooLongException e) {
                Log.d("p", "P2pListener | The discovery info is too long");
            }
            KitClient.getInstance(this).getDiscoveryServices().addListener(mP2pDiscoveryListener);
        }

        private void stopP2pDiscovery() {
            KitClient.getInstance(this).getDiscoveryServices().removeListener(mP2pDiscoveryListener);
            Log.d("p", "P2pListener removed");
        }


    private final P2pListener mP2pDiscoveryListener = new P2pListener() {

        @Override
        public void onStateChanged(final int state) {
            Log.d("p", "P2pListener | State changed: " + state);
        }

        @Override
        public void onPeerDiscovered(final Peer peer) {
            byte[] colorBytes = peer.getDiscoveryInfo();

            if (colorBytes != null ) {
                Log.d("p", "P2pListener | Peer discovered: " + peer.getNodeId() + " with color: " + new String(colorBytes));
                updateDevices(peer.getNodeId().toString(), colorBytes.toString());
                adapter.notifyDataSetChanged();

            } else {
                Log.d("p", "P2pListener | Peer discovered: " + peer.getNodeId() + " without color");
            }
        }

        @Override
        public void onPeerLost(final Peer peer) {
            Log.d("p", "P2pListener | Peer lost: " + peer.getNodeId());
        }

        @Override
        public void onPeerUpdatedDiscoveryInfo(Peer peer) {
            byte[] colorBytes = peer.getDiscoveryInfo();
            String s="";
            for(int i = 0; i < colorBytes.length; i ++)
            {
                s += (char)colorBytes[i];
            }
            if (colorBytes != null) {
                Log.d("p", "P2pListener | Peer updated: " + peer.getNodeId() + " with new info: " + s);
                updateDevices(peer.getNodeId().toString(), new String(colorBytes));
                adapter.notifyDataSetChanged();
            }
        }
    };



    private final MessageListener mMessageListener = new MessageListener() {

        @Override
        public void onStateChanged(final int state) {
            Log.d("p","MessageListener | State changed: " + state);
        }

        @Override
        public void onMessageReceived(final long timestamp, final UUID origin, final String type, final byte[] message) {
            Log.d("p", "MessageListener | Message received: From=" + origin + " type=" + type + " message=" + new String(message));
        }
    };

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

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

    void updateDevices(String id, String coordinates)
    {
        for(int i = 0; i < devices.size(); i++)
        {
            if(devices.get(i)[0] == "id")
            {
                String[] device = {id, coordinates};
                devices.set(i, device);
                return;
            }
        }
        String[] device = {id, coordinates};
        devices.add(device);

    }

    String[] getDeviceString(ArrayList<String[]> list)
    {
        String[] array = new String[list.size()];
        for (int i = 0; i< list.size(); i++)
        {
            array[i] = list.get(i)[1];
        }
        return array;
    }
}
