package org.toolboxbodensee.iamdrunk;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class DescribeImageActivity extends Activity {



    OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe_image);
        client = new OkHttpClient();

        new Connection().execute();
    }


    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_describe_image, menu);
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
    private class Connection extends AsyncTask {
        String data;
        @Override
        protected Object doInBackground(Object[] params) {
            Log.e("jfjhg", "aaaa");
            try {
                data = run("https://api.imgur.com/3/gallery/random/random/1");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

            @Override
            protected void onPostExecute (Object o){
                super.onPostExecute(o);
                Log.e("jfjhg", "ssss");
                JSONArray jsonArray = null;
                JSONObject jsonObject = null;
                Log.e("jfjhg", "dddd");
                try {
                    Log.e("jfjhg", "ffff");
                    jsonObject = new JSONObject(data);
                    jsonArray = jsonObject.getJSONArray("data");
                    Log.e("jfjhg", jsonArray.toString());
                    Log.e("jfjhg", "gggg");
                } catch (JSONException e) {
                    Log.e("jfjhg", "hhhh");
                    e.printStackTrace();
                }

                Log.e("jfjhg", "jjjj");
                Log.e("jfjhg", jsonArray.length() + "");
                int zielindex = (int) (Math.random() * jsonArray.length());
                try {
                    Log.e("jfjhg", jsonArray.getJSONObject(zielindex).get("link").toString());
                    ImageView img = (ImageView)findViewById(R.id.desc_image);
                    //img.setImageBitmap(jsonArray.getJSONObject(zielindex).get("link").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

}

