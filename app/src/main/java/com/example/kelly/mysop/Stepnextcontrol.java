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
    private static String url_create_product1 = "http://140.115.80.237/front/mysop_CC.jsp";
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_next_control);

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

    //判斷完工規則 1人工啟動 2前一步驟完工 3beacon 4QRcode 5NFC 6定位 7時間
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

            //先寫死stepnumber
            String Stepnumber = "1";

            int finishrule=0;

            //for get
            ArrayList params2 = new ArrayList();

            params2.add(new BasicNameValuePair("Stepnumber", Stepnumber));

            JSONObject json2 = StepNextControl.this.jsonParser.makeHttpRequest(StepNextControl.url_create_product1,"GET",params2);

            try {
                //加入清單
                int e3 = json2.getInt(TAG_SUCCESS);
                if(e3 == 1) {
                    finishrule=json2.getInt("finishrule");
                }

            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return finishrule;
        }

        protected void onPostExecute(Integer finishrule) {

            pDialog.dismiss();
            switch (finishrule){
                case 1:
                    // cagetory.setText("記錄完成");
                    //予帆會在steprecording做 所以直接跳下一頁
                    Intent it = new Intent(StepNextControl.this,StepNextControl.class);
                    startActivity(it);
                    finish();
                    break;
                case 2:
                    // cagetory.setText("記錄完成且通過");

                    break;
                case 3:
                    //cagetory.setText("人工確認");
                    Intent it3 = new Intent(StepNextControl.this,StepcutcontrolArtificial.class);
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
