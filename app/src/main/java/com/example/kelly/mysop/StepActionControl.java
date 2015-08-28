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
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class StepActionControl extends Activity {
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_create_product1 = "http://140.115.80.237/front/mysop_AC.jsp";
    private static final String TAG_SUCCESS = "success";
    String TAG_STEP_NUMBER = "";
    int TAG_STEP_ORDER = 0;

    private static String url_update = "http://140.115.80.237/front/mysop_AC1.jsp";
    private static String url_start_ac = "http://140.115.80.237/front/mysop_AC2.jsp";
    String TAG_CASE_NUMBER = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_action_control);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();	//取得Bundle

        //從P305來的話
        if(intent.hasExtra("TAG_NEXT_STEP_NUMBER")){
            TAG_STEP_NUMBER = bundle.getString("TAG_NEXT_STEP_NUMBER");
            new Update().execute();

        }else{
            //沒從P305來的話
            TAG_CASE_NUMBER = "3";//nfc
            //TAG_CASE_NUMBER = bundle.getString("TAG_CASE_NUMBER");
            new Checkall().execute();
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_step_action_control, menu);
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


    //不從p305來
    class Checkall extends AsyncTask<String, String, Integer> {
        protected void onPreExecute() {
            super.onPreExecute();
            //pDialog = new ProgressDialog(StepActionControl.this);
            //pDialog.setMessage("Loading..... Please wait...");
            //pDialog.setIndeterminate(false);
            //pDialog.setCancelable(false);
            //pDialog.show();
        }

        protected Integer doInBackground(String... args) {

            int startrule=0;
            String Casenumber = TAG_CASE_NUMBER;

            //for get
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("Casenumber", Casenumber));
            JSONObject json = StepActionControl.this.jsonParser.makeHttpRequest(StepActionControl.url_start_ac,"GET",params);

            try {
                //加入清單
                int e = json.getInt(TAG_SUCCESS);
                if(e == 1) {
                    startrule = json.getInt("startrule");
                    TAG_STEP_NUMBER = json.getString("stepnumber");
                    TAG_STEP_ORDER = json.getInt("steporder");
                }else{

                }

            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return startrule;
        }

        protected void onPostExecute(Integer startrule) {
            //pDialog.dismiss();

            //設定傳送參數
            Bundle bundle = new Bundle();
            bundle.putString("TAG_STEP_NUMBER", TAG_STEP_NUMBER);
            bundle.putInt("TAG_STEP_ORDER", TAG_STEP_ORDER);

            switch (startrule){
                case 1:
                    // cagetory.setText("人工啟動");
                    Intent it1 = new Intent(StepActionControl.this,StepActionControlArtificial.class);
                    it1.putExtras(bundle);//將參數放入intent
                    startActivity(it1);

                    break;
                case 2:
                    // cagetory.setText("前一步驟\n完工");
                    Intent it = new Intent(StepActionControl.this,Stepdescription.class);
                    it.putExtras(bundle);//將參數放入intent
                    startActivity(it);

                    break;
                case 3:
                    //cagetory.setText("Beacon");
                    Intent it3 = new Intent(StepActionControl.this,StepActionControlIbeacon.class);
                    it3.putExtras(bundle);//將參數放入intent
                    startActivity(it3);
                    break;
                case 4:
                    //cagetory.setText("QR code");
                    Intent it4 = new Intent(StepActionControl.this,StepActionControlQRcode.class);
                    it4.putExtras(bundle);//將參數放入intent
                    startActivity(it4);
                    finish();
                    break;
                case 5:
                    // cagetory.setText("NFC");
                    Intent it5 = new Intent(StepActionControl.this,StepActionControlNFC.class);
                    it5.putExtras(bundle);//將參數放入intent
                    startActivity(it5);
                    finish();
                    break;
                case 6:
                    // cagetory.setText("定位");
                    Intent it6 = new Intent(StepActionControl.this,StepActionControlGPS.class);
                    it6.putExtras(bundle);//將參數放入intent
                    startActivity(it6);
                    finish();
                    break;
                case 7:
                    //  cagetory.setText("時間到期");
                    Intent it7 = new Intent(StepActionControl.this,StepActionControlTime.class);
                    it7.putExtras(bundle);//將參數放入intent
                    startActivity(it7);
                    finish();
                    break;
                default:
                    Log.d("WRONG", "wrong");
                    break;
            }
        }

    }

    //從p305來，Update last_do_order
    class Update extends AsyncTask<String, String, Integer> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StepActionControl.this);
            pDialog.setMessage("Loading..... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Integer doInBackground(String... args) {

            int updatecheck=0;
            String Stepnumber = TAG_STEP_NUMBER;

            //for get
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("Stepnumber", Stepnumber));
            JSONObject json = StepActionControl.this.jsonParser.makeHttpRequest(StepActionControl.url_update,"POST",params);

            try {
                //加入清單
                int e3 = json.getInt(TAG_SUCCESS);
                if(e3 == 1) {
                    updatecheck=1;
                }else{
                    updatecheck=2;
                }

            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return updatecheck;
        }

        protected void onPostExecute(Integer updatecheck) {
            pDialog.dismiss();
            if(updatecheck==1) {
                new CheckStartrule().execute();
            }
        }

    }

    //判斷啟動規則 1人工啟動 2前一步驟完工 3beacon 4QRcode 5NFC 6定位 7時間
    class CheckStartrule extends AsyncTask<String, String, Integer> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StepActionControl.this);
            pDialog.setMessage("Loading..... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Integer doInBackground(String... args) {


            String Stepnumber = TAG_STEP_NUMBER;

            int startrule = 0;

            //for get
            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("Stepnumber", Stepnumber));

            JSONObject json = StepActionControl.this.jsonParser.makeHttpRequest(StepActionControl.url_create_product1,"GET",params);

            try {
                //加入清單
                int e3 = json.getInt(TAG_SUCCESS);
                if(e3 == 1) {
                    startrule = json.getInt("startrule");
                    TAG_STEP_ORDER = json.getInt("steporder");
                }

            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return startrule;
        }

        protected void onPostExecute(Integer startrule) {

            pDialog.dismiss();

            //設定傳送參數
            Bundle bundle = new Bundle();
            bundle.putString("TAG_STEP_NUMBER", TAG_STEP_NUMBER);
            bundle.putInt("TAG_STEP_ORDER", TAG_STEP_ORDER);

            switch (startrule){
                case 1:
                   // cagetory.setText("人工啟動");
                    Intent it1 = new Intent(StepActionControl.this,StepActionControlArtificial.class);
                    it1.putExtras(bundle);//將參數放入intent
                    startActivity(it1);

                    break;
                case 2:
                   // cagetory.setText("前一步驟\n完工");
                    Intent it = new Intent(StepActionControl.this,Stepdescription.class);
                    it.putExtras(bundle);//將參數放入intent
                    startActivity(it);

                    break;
                case 3:
                    //cagetory.setText("Beacon");
                    Intent it3 = new Intent(StepActionControl.this,StepActionControlIbeacon.class);
                    it3.putExtras(bundle);//將參數放入intent
                    startActivity(it3);
                    break;
                case 4:
                    //cagetory.setText("QR code");
                    Intent it4 = new Intent(StepActionControl.this,StepActionControlQRcode.class);
                    it4.putExtras(bundle);//將參數放入intent
                    startActivity(it4);
                    finish();
                    break;
                case 5:
                   // cagetory.setText("NFC");
                    Intent it5 = new Intent(StepActionControl.this,StepActionControlNFC.class);
                    it5.putExtras(bundle);//將參數放入intent
                    startActivity(it5);
                    finish();
                    break;
                case 6:
                   // cagetory.setText("定位");
                    Intent it6 = new Intent(StepActionControl.this,StepActionControlGPS.class);
                    it6.putExtras(bundle);//將參數放入intent
                    startActivity(it6);
                    finish();
                    break;
                case 7:
                  //  cagetory.setText("時間到期");
                    Intent it7 = new Intent(StepActionControl.this,StepActionControlTime.class);
                    it7.putExtras(bundle);//將參數放入intent
                    startActivity(it7);
                    finish();
                    break;
                default:
                    System.out.println("WRONG");
                    break;
            }


        }

    }

}
