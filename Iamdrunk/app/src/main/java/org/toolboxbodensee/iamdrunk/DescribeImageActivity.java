package org.toolboxbodensee.iamdrunk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;
import com.clarifai.api.exception.ClarifaiException;


public class DescribeImageActivity extends Activity {
    private static final String APP_ID = "0oyGmUkNF8s6Ha399dnpf8KqzPun7OW9aMTSHTyf";
    private static final String APP_SECRET = "tD2Ujs_Gi5NRfK9Oyp52dmTWRhPqHlEm3A_r86we";

    String link ="";
    OkHttpClient client;

    List<RecognitionResult> results;

    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe_image);
        client = new OkHttpClient();
        input = (EditText)findViewById(R.id.desc_input);
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

    public void enterPress(View view){
        boolean success = false;
        TextView txt = (TextView)findViewById(R.id.desc_label);

        for (Tag tag : results.get(0).getTags()) {
            Log.e("tool", tag.getName());
            if(input.getText().toString().contains(tag.getName())) {
                success = true;
            }

        }
        input.setText("");
        if (success) {
            txt.setText("Du bist noch nüchtern!");
            launchAppChooser();
        }
        else
            txt.setText("Du bist betrunken!");
    }

    private void launchAppChooser() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

        }
    }
    private class BMPConnection extends AsyncTask {
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
            new Clarifai().execute();

        }
    }
    private class Clarifai extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            ClarifaiClient clarifai = new ClarifaiClient(APP_ID, APP_SECRET);
            results = clarifai.recognize(new RecognitionRequest(link));


            return null;
        }

        @Override
        protected void onPostExecute (Object o){
            super.onPostExecute(o);




        }
    }

}

