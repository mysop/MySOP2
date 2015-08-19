package com.example.kelly.mysop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class StepActionControlQRcode extends Activity {

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    //讀取 qrcode 圖片
    private static String url_create_product = "http://140.115.80.237/front/mysop_ACqrcode.jsp";
    private static final String TAG_SUCCESS = "success";


    TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_action_control_qrcode);
        textView1 = (TextView) findViewById(R.id.qrcode);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_step_action_control_qrcode, menu);
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

    public void qrcheck (View v){
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        if(getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size()==0)
        {
            // 未安裝
            Toast.makeText(this, "請至 Play 商店安裝 ZXing 條碼掃描器", Toast.LENGTH_LONG).show();
        }
        else
        {
            // SCAN_MODE, 可判別所有支援的條碼
            // QR_CODE_MODE, 只判別 QRCode
            // PRODUCT_MODE, UPC and EAN 碼
            // ONE_D_MODE, 1 維條碼
            intent.putExtra("SCAN_MODE", "SCAN_MODE");

            // 呼叫ZXing Scanner，完成動作後回傳 1 給 onActivityResult 的 requestCode 參數
            startActivityForResult(intent, 1);
        }
    }


    // 接收 ZXing 掃描後回傳來的結果
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(requestCode==1)
        {
            if(resultCode==RESULT_OK)
            {
                // ZXing回傳的內容
                String contents = intent.getStringExtra("SCAN_RESULT");
                textView1.setText(contents.toString());
                new CheckQR().execute();
            }
            else
            if(resultCode==RESULT_CANCELED)
            {
                Toast.makeText(this, "取消掃描", Toast.LENGTH_LONG).show();
            }
        }
    }

    class CheckQR extends AsyncTask<String, String, Integer> {


        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StepActionControlQRcode.this);
            pDialog.setMessage("QR code checking.... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Integer doInBackground(String... args) {
            //寫死 Stepnumber
            String Stepnumber="1";
            String QRnumber=StepActionControlQRcode.this.textView1.getText().toString();
            int valoreOnPostExecute = 0;



            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("Stepnumber", Stepnumber));
            params.add(new BasicNameValuePair("QRnumber", QRnumber));

            JSONObject json = StepActionControlQRcode.this.jsonParser.makeHttpRequest(StepActionControlQRcode.url_create_product, "POST", params);


            try {
                int e = json.getInt(TAG_SUCCESS);
                if(e == 1) {
                    Intent it = new Intent(StepActionControlQRcode.this,Stepdescription.class);
                    startActivity(it);

                }else if(e == 6){

                    valoreOnPostExecute=1;

                }
            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return valoreOnPostExecute;
        }

        protected void onPostExecute(Integer valoreOnPostExecute) {

            pDialog.dismiss();
            if(valoreOnPostExecute==1){
                AlertDialog.Builder dialog = new AlertDialog.Builder(StepActionControlQRcode.this);
                dialog.setTitle("");
                dialog.setMessage("目標錯誤，請尋找正確QR code");
                dialog.show();

            }
        }
    }
}
