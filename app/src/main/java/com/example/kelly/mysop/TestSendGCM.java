package com.example.kelly.mysop;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class TestSendGCM extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_send_gcm);

    }

    public void sendNotification(String msg) {
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                try {
                    URLConnection connection = new URL("https://android.googleapis.com/gcm/send").openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Accept-Charset", "UTF-8");
                    connection.setRequestProperty("Authorization", "key=AIzaSyDwskyDkNLfMZwiw3lQue_yxsAOL1XrR7A");
                    connection.setRequestProperty("Content-Type", "application/json");

                    // encode output

                    JSONObject custom_msg = new JSONObject();
                    custom_msg.put("message", params[0]);
                    JSONObject notification = new JSONObject();
                    notification.put("title", "test title");
                    notification.put("body", "test body");
                    JSONObject data = new JSONObject();
                    //data.put("to", client_token); // 這裏放receiver的token

                    data.put("notification", notification);
                    data.put("data", custom_msg);

                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(data.toString().getBytes("UTF-8"));

                    InputStream inputStream = connection.getInputStream();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(msg);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_send_gcm, menu);
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
