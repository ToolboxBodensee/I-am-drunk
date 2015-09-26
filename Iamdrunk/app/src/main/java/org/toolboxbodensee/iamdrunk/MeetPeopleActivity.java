package org.toolboxbodensee.iamdrunk;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import ch.uepaa.p2pkit.ConnectionCallbacks;
import ch.uepaa.p2pkit.ConnectionResult;
import ch.uepaa.p2pkit.ConnectionResultHandling;
import ch.uepaa.p2pkit.KitClient;
import ch.uepaa.p2pkit.discovery.InfoTooLongException;
import ch.uepaa.p2pkit.discovery.P2pListener;
import ch.uepaa.p2pkit.discovery.Peer;

public class MeetPeopleActivity extends Activity {
    String APP_KEY = "eyJzaWduYXR1cmUiOiJHaFhPL004bHdTa0JKVXpWc3ZJNFoxbEI2ODBYU2kzK2NYZHh1THFpRForTlBsMVU3eFJrZStKeGxjTFBYelBUY0Y4dmRrWXlPc3JqTy9DRkhydUJmUzBkZWIwdHVOaStZeUwrdU9jWmZlMENOR2VFalUxWm52clBPRkY0RXVNZkk0ZjRLS1ptWE9WQ0Q5Y2FDNU1TR044ZHBKeGQxNEpRK29QRkltUG4vRDg9IiwiYXBwSWQiOjEyNTcsInZhbGlkVW50aWwiOjE2Nzk0LCJhcHBVVVVJRCI6IjRCODRGMzYwLUUwRUItNEU4RC05RTcyLTkwRTAzRjlDNDYzNCJ9";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_people);
        final int statusCode = KitClient.isP2PServicesAvailable(this);
        if (statusCode == ConnectionResult.SUCCESS) {
            KitClient client = KitClient.getInstance(this);
            client.registerConnectionCallbacks(mConnectionCallbacks);

            if (client.isConnected()) {
                Log.d("P2P", "Client already initialized");
            } else {
                Log.d("P2P", "Connecting P2PKit client");
                client.connect(APP_KEY);
            }

        } else {
            ConnectionResultHandling.showAlertDialogForConnectionError(this, statusCode);
        }
        KitClient.getInstance(this).getDiscoveryServices().addListener(mP2pDiscoveryListener);
    }

    private final ConnectionCallbacks mConnectionCallbacks = new ConnectionCallbacks() {
        @Override
        public void onConnected() {
            //ready to start discovery
            try {
                KitClient.getInstance(getApplicationContext()).getDiscoveryServices().setP2pDiscoveryInfo("Hello p2pkit".getBytes());
            } catch (InfoTooLongException e) {
                Log.d("P2P", "P2pListener | The discovery info is too long");
            }
        }

        @Override
        public void onConnectionSuspended() {
            //p2pkit is now disconnected
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            //connection failed, handle connectionResult
        }
    };


    private final P2pListener mP2pDiscoveryListener = new P2pListener() {
        @Override
        public void onStateChanged(final int state) {
            Log.d("P2P", "P2pListener | State changed: " + state);
        }

        @Override
        public void onPeerDiscovered(final Peer peer) {
            Log.d("P2P", "P2pListener | Peer discovered: " + peer.getNodeId() + " with info: " + new String(peer.getDiscoveryInfo()));
        }

        @Override
        public void onPeerLost(final Peer peer) {
            Log.d("P2P", "P2pListener | Peer lost: " + peer.getNodeId());
        }

        @Override
        public void onPeerUpdatedDiscoveryInfo(Peer peer) {
            Log.d("P2P", "P2pListener | Peer updated: " + peer.getNodeId() + " with new info: " + new String(peer.getDiscoveryInfo()));
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
}
