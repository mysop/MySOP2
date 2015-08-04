package com.example.kelly.mysop;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.*;
import android.graphics.Color;
import android.widget.GridLayout.LayoutParams;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Start extends Activity {

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();


    private static String url_all_products = "http://140.115.80.237/front/mysop_start.jsp";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_SOPNAME = "sop_name";
    private static String SOPNAME = "";
    private static final String TAG_SOPDETAIL = "sop_detail";
    private static String SOPDETAIL = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        // Loading products in Background Thread
        new LoadAllProducts().execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
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

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Start.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    SOPNAME = json.getString(TAG_SOPNAME);
                    SOPDETAIL = json.getString(TAG_SOPDETAIL);

                } else {



                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
/*            runOnUiThread(new Runnable() {
                public void run() {

                }
            }); */

            TextView start_title = (TextView)findViewById(R.id.start_title);
            start_title.setText(SOPNAME);

            TextView start_detail = (TextView)findViewById(R.id.start_detail);
            start_detail.setText(SOPDETAIL);

            TextView start_right = (TextView)findViewById(R.id.start_right);
            start_right.setText("9");

            AlertDialog.Builder dialog = new AlertDialog.Builder(Start.this);
            dialog.setTitle("");
            dialog.setMessage(SOPNAME);
            dialog.show();


        }

    }

}
