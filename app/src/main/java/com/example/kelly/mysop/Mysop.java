package com.example.kelly.mysop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class Mysop extends Activity {



    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> productsList;
    private static String url_all_products = "http://localhost:8080/kelly/test_getall.jsp";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";

    JSONArray products = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysop);


        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
        // Loading products in Background Thread
        new LoadAllProducts().execute();




        RelativeLayout w = (RelativeLayout)findViewById(R.id.relative);
        w.setBackgroundColor(Color.parseColor("#EEFFBB"));
        // 取得 LinearLayout 物件
        LinearLayout l = (LinearLayout)findViewById(R.id.viewObj);



        LinearLayout l2 = new LinearLayout(this);
        l2.setBackgroundColor(Color.parseColor("#FF8888"));
        l2.setOrientation(LinearLayout.VERTICAL);
        //l2.setOnClickListener();


        TextView tv = new TextView(this);
        tv.setText("Designing");
        //tv.setText(productsList.get(0).get(TAG_PID));
        l2.addView(tv);

        TextView tv1 = new TextView(this);
        tv1.setText("k20150218");
        l2.addView( tv1 );
        Button b1 = new Button(this);
        b1.setText("取消");
        l2.addView( b1 );

        l.addView( l2 );

        LinearLayout l3 = new LinearLayout(this);
        l3.setBackgroundColor(Color.parseColor("#99BBFF"));
        l3.setOrientation(LinearLayout.VERTICAL);

        TextView tv3 = new TextView(this);
        tv3.setText("ddddd");
        l3.addView( tv3 );
        l.addView( l3 );

        //從這裡哈哈哈哈哈哈哈
        LinearLayout l4 = new LinearLayout(this);
        l4.setBackgroundResource(R.drawable.linearlayout);
        //l4.setLayoutParams(new RelativeLayout.LayoutParams(150, 150));
        RelativeLayout.LayoutParams r = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        r.addRule(RelativeLayout.BELOW, R.id.viewObj);
        w.addView(l4,r);


        // 從 LinearLayout 中移除 Button 1
        //ll.removeView( b1 );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mysop, menu);
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
            pDialog = new ProgressDialog(Mysop.this);
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
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);

                        // adding HashList to ArrayList
                        productsList.add(map);
                    }
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


            AlertDialog.Builder dialog = new AlertDialog.Builder(Mysop.this);
            dialog.setTitle("");
            dialog.setMessage(productsList.get(0).get(TAG_PID));
            dialog.show();

        }

    }
}
