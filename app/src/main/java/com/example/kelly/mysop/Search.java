package com.example.kelly.mysop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.widget.AdapterView.OnItemClickListener;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Search extends Activity {
    private GridView gridView;
    private int[] image = {
            R.drawable.star1, R.drawable.star2, R.drawable.test,
            R.drawable.test1, R.drawable.test2,R.drawable.test,
            R.drawable.test1, R.drawable.test2
    };
    private String[] image1 ;
    private String[] imgText ;
    private String[] imgText2;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    ArrayList<HashMap<String, String>> productsList = new ArrayList<HashMap<String, String>>();
    private static String url_all_products = "http://140.115.80.237/front/mysop_home.jsp";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_SOPNAME = "sopname";
    private static final String TAG_PICTURE = "picture";
    JSONArray products = null;
    int jj=10;

    List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
    SimpleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        new LoadSearch().execute();

        adapter = new SimpleAdapter(this,
                items, R.layout.grid_item, new String[]{"image", "text","text2"},
                new int[]{R.id.image, R.id.text,R.id.text2});

        gridView = (GridView)findViewById(R.id.main_page_gridview);
        gridView.setNumColumns(2);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new OnItemClickListener () {

            @Override
            public void onItemClick (AdapterView < ? > parent, View view,int position, long id){
                Toast.makeText(getApplicationContext(), "您選擇的是"+imgText[position], Toast.LENGTH_SHORT).show();
            }
        }) ;

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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

    class LoadSearch extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("CY","onPreExcute");
            pDialog = new ProgressDialog(Search.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            Log.d("CY","doBackground");
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // params.add(new BasicNameValuePair("Account", TAG_ACCOUNT) );
            // getting JSON string from URL
            JSONObject json = Search.this.jsonParser.makeHttpRequest(Search.url_all_products,"GET", params);


            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    Log.d("CY","success");
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String sopname = c.getString(TAG_SOPNAME);
                        String username = c.getString(TAG_USERNAME);
                        String picture = c.getString(TAG_PICTURE);
                        Log.d("CY","sopname "+sopname+" username "+username+" picture "+picture);
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_SOPNAME, sopname);
                        map.put(TAG_USERNAME, username);
                        map.put(TAG_PICTURE, picture);


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
            Log.d("CY","onPostExecute");
            pDialog.dismiss();

            jj=products.length();
            imgText = new String[products.length()];
            imgText2 = new String[products.length()];
            image1 = new String[products.length()];


            for (int i = 0; i <products.length(); i++) {
                    imgText[i] = productsList.get(i).get(TAG_SOPNAME);
                    imgText2[i] = productsList.get(i).get(TAG_USERNAME);
                   image1[i] = productsList.get(i).get(TAG_PICTURE);
                }
            for (int i = 0; i < imgText.length; i++) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("image", image[i]);
                item.put("text", imgText[i]);
                item.put("text2",imgText2[i]);
                items.add(item);
            }

        }


    }
    //圖片網址
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}
