package com.example.kelly.mysop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.widget.Toast;

//p303
public class Steprecording extends Activity {


    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> productsList;
    JSONArray products = null;

    private static String url_all_products = "http://140.115.80.237/front/mysop_steprecoding.jsp";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_RECODE = "recode";


    private static String url_record = "http://140.115.80.237/front/mysop_login.jsp";

    private GestureDetector detector;
    EditText edit1;
    EditText et1;

    private Intent recognizerIntent = null;
    private GridView gridView;
    private List<String> messageList;
    private ArrayAdapter<String> adapter;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steprecording);

        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
        // Loading products in Background Thread
        new LoadInput().execute();
        detector = new GestureDetector(new MySimpleOnGestureListener());
        ScrollView sv = (ScrollView)findViewById(R.id.scrollView);
        sv.setOnTouchListener(new MyOnTouchListener());

        context = this;
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        /*if (!hasRecognizer()) {
            Toast.makeText(context, "無語音辨識服務", Toast.LENGTH_SHORT).show();
            //finish();
            return;
        }*/
        messageList = new ArrayList<>();

    }

    private boolean hasRecognizer() {
        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(recognizerIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() != 0) {
            return true;
        } else {
            return false;
        }
    }

/*    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {// 失去焦点
            edit1.getTag();
        }
    }*/


    public void speak_onclick(View view) {
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說...");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        startActivityForResult(recognizerIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent it) {
        messageList.clear();
        if (requestCode != 1) {
            return;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        List<String> list =
                it.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        for (String s : list) {
            messageList.add(s);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_steprecording, menu);
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
    class LoadInput extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Steprecording.this);
            pDialog.setMessage("Loading. Please wait...");
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

                    products = json.getJSONArray(TAG_PRODUCTS);

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);


                        // Storing each json item in variable
                        int r = i+2;
                        String id = c.getString(TAG_RECODE+r);


                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_RECODE+r, id);

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

            LinearLayout ly = (LinearLayout)findViewById(R.id.linearlayoutinput);

            for(int i=0; i<products.length();i++) {

                int r = i+2;
                TextView text1 = new TextView(Steprecording.this);
                text1.setText(productsList.get(i).get(TAG_RECODE+r));


                edit1 = new EditText(Steprecording.this.getApplicationContext());
                edit1.setId(400+i);

                ly.addView(text1);
                ly.addView(edit1);
            }

            //if(edit1.getTag().equals(1)){edit1.setText("qqq");}

            ly.setOnTouchListener(new MyOnTouchListener());


        }

    }



    //抓滑動，還沒寫儲存的doinbackground
    class MyOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return detector.onTouchEvent(event);
        }
    }
    class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // TODO Auto-generated method stub
            if ((e1.getX() - e2.getX()) > 50) {//说明是左滑
                Intent intent = new Intent();
                intent.setClass(Steprecording.this, StepcutcontrolArtificial.class);
                startActivity(intent);
                // 设置切换动画，从右边进入，左边退出
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                return true;
            } else
                return false;
        }

    }

    //回傳
    class Recording extends AsyncTask<String, String, String> {
        Recording() {}

        protected void onPreExecute() {
            super.onPreExecute();
            Steprecording.this.pDialog = new ProgressDialog(Steprecording.this);
            Steprecording.this.pDialog.setMessage("Recording...");
            Steprecording.this.pDialog.setIndeterminate(false);
            Steprecording.this.pDialog.setCancelable(true);
            Steprecording.this.pDialog.show();
        }

        protected String doInBackground(String... args) {

            //EditText et1 = (EditText)edit1.findViewById(20);
            String Account = Steprecording.this.et1.getText().toString();


            ArrayList params = new ArrayList();
         //   params.add(new BasicNameValuePair("Account", Account));

            JSONObject json = Steprecording.this.jParser.makeHttpRequest(Steprecording.url_record, "POST", params);
            Log.d("Create Response", json.toString());

            try {
                int e = json.getInt(TAG_SUCCESS);
                if(e == 1) {

                }else if(e == 2){

                }
            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            Steprecording.this.pDialog.dismiss();
        }
    }




}
