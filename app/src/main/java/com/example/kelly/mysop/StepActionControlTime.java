package com.example.kelly.mysop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.text.ParseException;


public class StepActionControlTime extends Activity {
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    //讀取 時間是否一樣
    private static String url_create_product = "http://140.115.80.237/front/mysop_ACtime.jsp";
    private static final String TAG_SUCCESS = "success";
    private static String str;
    private static Button timebtn;
    private static String Starttime="";
    private static TextView timedifference;
    //檢查 是否有過期 0未過期 1過期 2剛好
    private static int check=0;

    String TAG_CASE_NUMBER = "";
    String TAG_STEP_NUMBER = "";
    int TAG_STEP_ORDER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_action_control_time);

        TextView ss = (TextView)findViewById(R.id.textView2);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();	//取得Bundle
        TAG_CASE_NUMBER = bundle.getString("TAG_CASE_NUMBER");
        TAG_STEP_NUMBER = bundle.getString("TAG_STEP_NUMBER");
        TAG_STEP_ORDER = bundle.getInt("TAG_STEP_ORDER");
        ss.setText(Integer.toString(TAG_STEP_ORDER));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
        Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
        str = formatter.format(curDate);
        System.out.println("HERE "+str);

        timebtn=(Button)findViewById(R.id.timecheck);
        timedifference=(TextView)findViewById(R.id.timedifference);

        new CheckTime().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_step_action_control_time, menu);
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

    public void timeonclick(View v){
        if(check==2){
            Intent it5 = new Intent(StepActionControlTime.this,Stepdescription.class);
            Bundle bundle = new Bundle();
            bundle.putString("TAG_CASE_NUMBER",TAG_CASE_NUMBER);
            bundle.putString("TAG_STEP_NUMBER", TAG_STEP_NUMBER);
            bundle.putInt("TAG_STEP_ORDER", TAG_STEP_ORDER);
            it5.putExtras(bundle);//將參數放入intent
            startActivity(it5);
            finish();
        }else if(check==0){
            AlertDialog.Builder dialog = new AlertDialog.Builder(StepActionControlTime.this);
            dialog.setTitle("");
            dialog.setMessage("時間未到，請靜候");
            dialog.show();
        }else if(check==1){
            AlertDialog.Builder dialog = new AlertDialog.Builder(StepActionControlTime.this);
            dialog.setTitle("");
            dialog.setMessage("時間已過");
            dialog.show();
        }
    }

    class CheckTime extends AsyncTask<String, String, Integer> {


        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StepActionControlTime.this);
            pDialog.setMessage("Time checking.... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Integer doInBackground(String... args) {
            //String Stepnumber="5";
            String Stepnumber = TAG_STEP_NUMBER;
            String Time= str;
            Integer CHECK=0;//CHECK 0代表過期或未到 1代表已到

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("Stepnumber", Stepnumber));
            params.add(new BasicNameValuePair("Time", Time));

            JSONObject json = StepActionControlTime.this.jsonParser.makeHttpRequest(StepActionControlTime.url_create_product, "POST", params);


            try {
                int e = json.getInt(TAG_SUCCESS);
                if(e == 1) {
                    Intent intent = new Intent(StepActionControlTime.this,Stepdescription.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("TAG_CASE_NUMBER",TAG_CASE_NUMBER);
                    bundle.putString("TAG_STEP_NUMBER", TAG_STEP_NUMBER);
                    bundle.putInt("TAG_STEP_ORDER", TAG_STEP_ORDER);
                    intent.putExtras(bundle);//將參數放入intent
                    startActivity(intent);
                    CHECK=1;
                    timebtn.setText("啟動");

                }else if(e == 6){

                    Starttime=json.getString("starttime");
                    System.out.println("Startime"+Starttime);

                }
            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return CHECK;
        }

        protected void onPostExecute(Integer CHECK) {

            pDialog.dismiss();
            //計算時間差並顯示
            if(CHECK==0) {
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
                Date now = null;

                try {
                    now = df.parse(Starttime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date = null;
                try {
                    date = df.parse(str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long l;

                //比較時間大小
                if(now.getTime()>date.getTime()) {
                    //未過期
                     l = now.getTime() - date.getTime();
                    check=0;
                }else{
                    //過期
                    l = date.getTime() - now.getTime();
                    check=1;
                }
                long l2 = l/(30*24*60*60);

                //計算時間差
                long month=l2/1000;
                long day = l / (24 * 60 * 60 * 1000)- month * 30;
                long hour = (l / (60 * 60 * 1000) - month * 30 * 24 - day * 24);
                long min = ((l / (60 * 1000)) - month * 30 * 24 * 60- day * 24 * 60 - hour * 60);
                long s = (l / 1000 - month * 30 * 24 * 60 * 60- day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                System.out.println(month+"月" + day + "天" + hour + "小时" + min + "分" + s + "秒");

                if(check==0) {
                    timebtn.setText("時辰未到");
                    if (month == 0) {
                        timedifference.setText("還差" + day + "天" + hour + "小时" + min + "分");
                    } else if (month == 0 && day == 0) {
                        timedifference.setText("還差" + hour + "小时" + min + "分");
                    } else if (month == 0 && day == 0 && hour == 0) {
                        timedifference.setText("還差" + min + "分");
                        timedifference.setTextColor(Color.RED);
                    } else {
                        timedifference.setText("還差" + month + "月" + day + "天" + hour + "小时" + min + "分");
                    }
                }else{
                    //過期
                    Intent intent = new Intent(StepActionControlTime.this,Stepdescription.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("TAG_CASE_NUMBER",TAG_CASE_NUMBER);
                    bundle.putString("TAG_STEP_NUMBER", TAG_STEP_NUMBER);
                    bundle.putInt("TAG_STEP_ORDER", TAG_STEP_ORDER);
                    intent.putExtras(bundle);//將參數放入intent
                    startActivity(intent);
                    timebtn.setText("啟動");
                }
            }

        }
    }
}
