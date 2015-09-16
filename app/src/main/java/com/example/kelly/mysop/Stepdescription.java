package com.example.kelly.mysop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//p302
public class Stepdescription extends Activity {

    private GestureDetector detector;

    String TAG_CASE_NUMBER = "";
    String TAG_STEP_NUMBER = "";
    int TAG_STEP_ORDER = 0;


    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> productsList;
    JSONArray products = null;

    private static String url_next = "http://140.115.80.237/front/mysop_stepdescription.jsp";
    private static final String TAG_SUCCESS = "success";
    private int TAG_Next=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepdescription);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();	//取得Bundle
        TAG_CASE_NUMBER = bundle.getString("TAG_CASE_NUMBER");
        TAG_STEP_NUMBER = bundle.getString("TAG_STEP_NUMBER");
        TAG_STEP_ORDER = bundle.getInt("TAG_STEP_ORDER");

        //TAG_CASE_NUMBER = "J1234";
        //TAG_STEP_NUMBER = "10";
        //TAG_STEP_ORDER = 1;
        new CheckNextActivity().execute();

        WebView wv = (WebView)findViewById(R.id.webView);
        wv.loadUrl("https://www.google.com.tw");
        detector = new GestureDetector(new MySimpleOnGestureListener());
        WebView ww = (WebView)findViewById(R.id.webView);
        ww.setOnTouchListener(new MyOnTouchListener());
    }

    class CheckNextActivity extends AsyncTask<String, String, Integer> {

        protected void onPreExecute() {
            super.onPreExecute();
            //pDialog = new ProgressDialog(StepActionControl.this);
            //pDialog.setMessage("Loading..... Please wait...");
            //pDialog.setIndeterminate(false);
            //pDialog.setCancelable(false);
            //pDialog.show();
        }
        protected Integer doInBackground(String... args) {

            int NextActivity=0;
            String Stepnumber = TAG_STEP_NUMBER;

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Stepnumber", Stepnumber));

            JSONObject json = jParser.makeHttpRequest(url_next, "GET", params);

            Log.d("All Products: ", json.toString());

            try {

                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    NextActivity=1;
                    TAG_Next = NextActivity;
                } else if(success == 6) {
                    NextActivity=2;
                    TAG_Next = NextActivity;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return NextActivity;
        }
        protected void onPostExecute(Integer NextActivity) {
            if(NextActivity==0){
               Log.d("下一步","讀取失敗");
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stepdescription, menu);
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
                if(TAG_Next==1){
                    intent.setClass(Stepdescription.this, Steprecording.class);
                }else{
                    intent.setClass(Stepdescription.this, StepCutControl.class);
                }
                Bundle bundle = new Bundle();
                bundle.putString("TAG_CASE_NUMBER",TAG_CASE_NUMBER);
                bundle.putString("TAG_STEP_NUMBER", TAG_STEP_NUMBER);
                bundle.putInt("TAG_STEP_ORDER", TAG_STEP_ORDER);
                intent.putExtras(bundle);//將參數放入intent
                startActivity(intent);
                // 设置切换动画，从右边进入，左边退出
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                finish();
                return true;
            } else
                return false;
        }

    }
}
