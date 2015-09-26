package org.toolboxbodensee.iamdrunk;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;

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

public class MeetPeopleActivity extends Activity {

    String APP_KEY = "eyJzaWduYXR1cmUiOiJHaFhPL004bHdTa0JKVXpWc3ZJNFoxbEI2ODBYU2kzK2NYZHh1THFpRForTlBsMVU3eFJrZStKeGxjTFBYelBUY0Y4dmRrWXlPc3JqTy9DRkhydUJmUzBkZWIwdHVOaStZeUwrdU9jWmZlMENOR2VFalUxWm52clBPRkY0RXVNZkk0ZjRLS1ptWE9WQ0Q5Y2FDNU1TR044ZHBKeGQxNEpRK29QRkltUG4vRDg9IiwiYXBwSWQiOjEyNTcsInZhbGlkVW50aWwiOjE2Nzk0LCJhcHBVVVVJRCI6IjRCODRGMzYwLUUwRUItNEU4RC05RTcyLTkwRTAzRjlDNDYzNCJ9";

    private final P2pListener mP2pDiscoveryListener = new P2pListener() {

        @Override
        public void onStateChanged(final int state) {
            Log.d("P2P","P2pListener | State changed: " + state);
        }

        @Override
        public void onPeerDiscovered(final Peer peer) {
            byte[] info = peer.getDiscoveryInfo();
            if (info != null) {
                Log.d("P2P", "P2pListener | Peer discovered: " + peer.getNodeId() + " with info: " + info.toString());
            } else {
                Log.d("P2P","P2pListener | Peer discovered: " + peer.getNodeId() + " without info");
            }
        }

        @Override
        public void onPeerLost(final Peer peer) {
            Log.d("P2P", "P2pListener | Peer lost: " + peer.getNodeId());
        }

        @Override
        public void onPeerUpdatedDiscoveryInfo(Peer peer) {
            byte[] info = peer.getDiscoveryInfo();
            if (info != null) {
                Log.d("P2P", "P2pListener | Peer updated: " + peer.getNodeId() + " with new info: " + info.toString());
            }
        }
    };

    private final GeoListener mGeoDiscoveryListener = new GeoListener() {

        @Override
        public void onStateChanged(final int state) {
            Log.d("P2P","GeoListener | State changed: " + state);
        }

        @Override
        public void onPeerDiscovered(final UUID nodeId) {
            Log.d("P2P","GeoListener | Peer discovered: " + nodeId);

            // sending a message to the peer
            KitClient.getInstance(MeetPeopleActivity.this).getMessageServices().sendMessage(nodeId, "SimpleChatMessage", "From Android: Hello GEO!".getBytes());
        }

        @Override
        public void onPeerLost(final UUID nodeId) {
            Log.d("P2P","GeoListener | Peer lost: " + nodeId);
        }
    };


    private final ConnectionCallbacks mConnectionCallbacks = new ConnectionCallbacks() {

        @Override
        public void onConnected() {
            Log.d("P2P","Successfully connected to P2P Services, with id: " + KitClient.getInstance(MeetPeopleActivity.this).getNodeId().toString());


        }

        @Override
        public void onConnectionSuspended() {
            Log.d("P2P","Connection to P2P Services suspended");

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.d("P2P","Connection to P2P Services failed with status: " + connectionResult.getStatusCode());
            ConnectionResultHandling.showAlertDialogForConnectionError(MeetPeopleActivity.this, connectionResult.getStatusCode());
        }
    };


    private void enableKit() {

        final int statusCode = KitClient.isP2PServicesAvailable(this);
        if (statusCode == ConnectionResult.SUCCESS) {
            KitClient client = KitClient.getInstance(this);
            client.registerConnectionCallbacks(mConnectionCallbacks);

            if (client.isConnected()) {
                Log.d("P2P", "Client already connected");
            } else {
                Log.d("P2P", "Connecting P2PKit client");
                client.connect(APP_KEY);
            }
            startP2pDiscovery();
            startGeoDiscovery();
        } else {
            Log.d("P2P", "Cannot start P2PKit, status code: " + statusCode);
            ConnectionResultHandling.showAlertDialogForConnectionError(this, statusCode);
        }
    }

    private void startP2pDiscovery() {
        try {
            KitClient.getInstance(this).getDiscoveryServices().setP2pDiscoveryInfo(hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d"));
        } catch (InfoTooLongException e) {
            Log.d("P2P", "P2pListener | The discovery info is too long");
        }
        KitClient.getInstance(this).getDiscoveryServices().addListener(mP2pDiscoveryListener);
    }

    private void stopP2pDiscovery() {
        KitClient.getInstance(this).getDiscoveryServices().removeListener(mP2pDiscoveryListener);
        Log.d("P2P", "P2pListener removed");
    }

    private void startGeoDiscovery() {
        KitClient.getInstance(this).getMessageServices().addListener(mMessageListener);

        KitClient.getInstance(this).getDiscoveryServices().addListener(mGeoDiscoveryListener);
    }

    private void stopGeoDiscovery() {
        KitClient.getInstance(this).getMessageServices().removeListener(mMessageListener);
        Log.d("P2P", "MessageListener removed");

        KitClient.getInstance(this).getDiscoveryServices().removeListener(mGeoDiscoveryListener);
        Log.d("P2P", "GeoListener removed");
    }

    private final MessageListener mMessageListener = new MessageListener() {

        @Override
        public void onStateChanged(final int state) {
            Log.d("P2P", "MessageListener | State changed: " + state);
        }

        @Override
        public void onMessageReceived(final long timestamp, final UUID origin, final String type, final byte[] message) {
            Log.d("P2P", "MessageListener | Message received: From=" + origin + " type=" + type + " message=" + new String(message));
        }
    };








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_people);

        enableKit();
    }



    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


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
