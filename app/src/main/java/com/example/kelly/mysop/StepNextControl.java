package com.example.kelly.mysop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class StepNextControl extends Activity {

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_next_control = "http://140.115.80.237/front/mysop_StepNextControl.jsp";
    private static final String TAG_SUCCESS = "success";

    String TAG_NEXT_STEP_NUMBER = "";

    String TAG_CASE_NUMBER = "";
    String TAG_STEP_NUMBER = "";
    int TAG_STEP_ORDER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_next_control);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();	//取得Bundle
        TAG_CASE_NUMBER = bundle.getString("TAG_CASE_NUMBER");
        TAG_STEP_NUMBER = bundle.getString("TAG_STEP_NUMBER");
        TAG_STEP_ORDER = bundle.getInt("TAG_STEP_ORDER");

        new CheckNextControlRule().execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_step_next_control, menu);
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


    //判斷完工規則 1依順序決定 2依使用者決定 3依資料決定
    class CheckNextControlRule extends AsyncTask<String, String, Integer> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StepNextControl.this);
            pDialog.setMessage("Loading..... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Integer doInBackground(String... args) {

            //String Stepnumber = "1";
            String Stepnumber = TAG_STEP_NUMBER;
            int nextsteprule = 0;

            //for get
            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("Stepnumber", Stepnumber));

            JSONObject json = StepNextControl.this.jsonParser.makeHttpRequest(StepNextControl.url_next_control,"GET",params);

            try {
                //加入清單
                int e = json.getInt(TAG_SUCCESS);
                if(e == 1) {
                    nextsteprule = json.getInt("nextsteprule");
                    TAG_NEXT_STEP_NUMBER = json.getString("nextstepnumber");

                }

            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return nextsteprule;
        }

        protected void onPostExecute(Integer nextsteprule) {

            pDialog.dismiss();

            //設定傳送參數
            Bundle bundle = new Bundle();
            bundle.putString("TAG_NEXT_STEP_NUMBER", TAG_NEXT_STEP_NUMBER);
            bundle.putString("TAG_CASE_NUMBER",TAG_CASE_NUMBER);

            switch (nextsteprule){
                case 1:
                    // 依順序決定
                    Intent it1 = new Intent(StepNextControl.this,StepActionControl.class);
                    it1.putExtras(bundle);//將參數放入intent
                    startActivity(it1);
                    finish();
                    break;
                case 2:
                    // 依使用者決定
                    Intent it2 = new Intent(StepNextControl.this,StepNextControlUser.class);
                    it2.putExtras(bundle);//將參數放入intent
                    startActivity(it2);
                    finish();
                    break;
                case 3:
                    // 依資料決定
                    Intent it3 = new Intent(StepNextControl.this,StepNextControl.class);
                    it3.putExtras(bundle);//將參數放入intent
                    startActivity(it3);
                    finish();
                    break;

                default:
                    System.out.println("WRONG");
                    break;

            }


        }

    }
}
