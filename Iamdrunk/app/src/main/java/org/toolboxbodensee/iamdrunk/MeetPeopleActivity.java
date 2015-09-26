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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
                startGeoDiscovery();
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

        private void startGeoDiscovery() {
            KitClient.getInstance(this).getMessageServices().addListener(mMessageListener);

            KitClient.getInstance(this).getDiscoveryServices().addListener(mGeoDiscoveryListener);
        }

        private void stopGeoDiscovery() {
            KitClient.getInstance(this).getMessageServices().removeListener(mMessageListener);
            Log.d("p", "MessageListener removed");

            KitClient.getInstance(this).getDiscoveryServices().removeListener(mGeoDiscoveryListener);
            Log.d("p", "GeoListener removed");
        }

    private final P2pListener mP2pDiscoveryListener = new P2pListener() {

        @Override
        public void onStateChanged(final int state) {
            Log.d("p", "P2pListener | State changed: " + state);
        }

        @Override
        public void onPeerDiscovered(final Peer peer) {
            byte[] colorBytes = peer.getDiscoveryInfo();
            String s="";
            for(int i = 0; i < colorBytes.length; i ++)
            {
                s += (char)colorBytes[i];
            }
            if (colorBytes != null ) {
                Log.d("p", "P2pListener | Peer discovered: " + peer.getNodeId() + " with color: " + s);
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
            }
        }
    };

    private final GeoListener mGeoDiscoveryListener = new GeoListener() {

        @Override
        public void onStateChanged(final int state) {
            Log.d("p","GeoListener | State changed: " + state);
        }

        @Override
        public void onPeerDiscovered(final UUID nodeId) {
            Log.d("p","GeoListener | Peer discovered: " + nodeId);

            // sending a message to the peer
            KitClient.getInstance(MeetPeopleActivity.this).getMessageServices().sendMessage(nodeId, "SimpleChatMessage", "From Android: Hello GEO!".getBytes());
        }

        @Override
        public void onPeerLost(final UUID nodeId) {
            Log.d("p","GeoListener | Peer lost: " + nodeId);
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
}
