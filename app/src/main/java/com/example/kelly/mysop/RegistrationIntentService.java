package com.example.kelly.mysop;

import android.app.AlertDialog;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }


    JSONParser jsonParser = new JSONParser();
    private static String url_token = "http://140.115.80.237/front/mysop_gcm.jsp";
    private static final String TAG_SUCCESS = "success";
    String TAG_ACCOUNT="";

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken("115664216111",
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);

            // TODO: Implement this method to send any registration to your app's servers.

            Bundle bundle = intent.getExtras();
            TAG_ACCOUNT = bundle.getString("TAG_ACCOUNT");

            sendRegistrationToServer(token);

            // Subscribe to topic channels
            subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        new sendRegistrationToServer().execute(token);
    }

    class sendRegistrationToServer extends AsyncTask<String, String, Integer> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Integer doInBackground(String... args) {

            int returnvalue = 0;

            String TAG_TOKEN = args[0];

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("Account", TAG_ACCOUNT));
            params.add(new BasicNameValuePair("Token", TAG_TOKEN));
            JSONObject json = RegistrationIntentService.this.jsonParser.makeHttpRequest(RegistrationIntentService.url_token, "POST", params);

            try {
                int e = json.getInt(TAG_SUCCESS);
                if(e == 1) {
                    returnvalue = 1;
                }else if(e == 6){
                    returnvalue = 6;
                }
            } catch (JSONException var9) {
                var9.printStackTrace();
            }
            return returnvalue;
        }

        protected void onPostExecute(Integer returnvalue) {
            if (returnvalue == 1){

            }else{
                Log.d("Fail to POST","沒有上傳成功");
            }
        }
    }
    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]
}
