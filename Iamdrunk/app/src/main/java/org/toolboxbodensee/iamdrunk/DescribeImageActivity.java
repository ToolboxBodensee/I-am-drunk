package org.toolboxbodensee.iamdrunk;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class DescribeImageActivity extends Activity {


    String link ="";
    OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe_image);
        client = new OkHttpClient();

        new LinkConnection().execute();
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


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
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
    private class LinkConnection extends AsyncTask {
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
            JSONArray jsonArray = null;
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
                jsonArray = jsonObject.getJSONArray("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            int zielindex = (int) (Math.random() * jsonArray.length());
            try {
                link = jsonArray.getJSONObject(zielindex).get("link").toString();
                new BMPConnection().execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new BMPConnection().execute();
        }
    }private class BMPConnection extends AsyncTask {
        Bitmap bmp;
        @Override
        protected Object doInBackground(Object[] params) {
            bmp = getBitmapFromURL(link);
            return null;
        }

        @Override
        protected void onPostExecute (Object o){
            super.onPostExecute(o);

                ImageView img = (ImageView)findViewById(R.id.desc_image);
                img.setImageBitmap(bmp);
        }
    }

}

