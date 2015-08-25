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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Forgetpwd extends Activity {

    private ProgressDialog pDialog;

    private static String url_forgetpwd = "http://140.115.80.237/front/mysop_forgetpwd.jsp";
    private EditText et1;
    String strHint1;


    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    String TAG_ACCOUNT = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd);

        et1 = (EditText) findViewById(R.id.forgetpwd_editText1);
        strHint1 = getResources().getString(R.string.Login_email);
        final EditText[] mArray = new EditText[] {et1};
        for (int i = 0; i < mArray.length; i++) {
            final int j = i;
            //EditText 獲得/失去焦點hint消失/出現
            mArray[j].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        if(j == 0){
                            ((TextView) v).setHint(strHint1);
                        }
                    } else {
                        ((TextView) v).setHint("");
                    }
                }
            });
        }


    }

    public void Account_check(View view) {
        new CreateAccount().execute();
    }
    class CreateAccount extends AsyncTask<String, String, Integer> {


        protected void onPreExecute() {
            super.onPreExecute();
            Forgetpwd.this.pDialog = new ProgressDialog(Forgetpwd.this);
            Forgetpwd.this.pDialog.setMessage("Please wait...");
            Forgetpwd.this.pDialog.setIndeterminate(false);
            Forgetpwd.this.pDialog.setCancelable(true);
            Forgetpwd.this.pDialog.show();
        }

        protected Integer doInBackground(String... args) {

            int returnvalue = 0;
            String Account = Forgetpwd.this.et1.getText().toString();

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("Account", Account));

            JSONObject json = Forgetpwd.this.jsonParser.makeHttpRequest(Forgetpwd.url_forgetpwd, "GET", params);
            Log.d("Create Response", json.toString());

            try {
                int e = json.getInt(TAG_SUCCESS);
                if(e == 1) {
                    returnvalue = 1;

                }else if(e == 6){
                    returnvalue = 2;
                }
            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return returnvalue;
        }

        protected void onPostExecute(Integer returnvalue) {
            Forgetpwd.this.pDialog.dismiss();
            if(returnvalue == 1){

                TAG_ACCOUNT = Forgetpwd.this.et1.getText().toString();
                Intent intent = new Intent();
                intent.setClass(Forgetpwd.this, Forget.class);
                //設定傳送參數
                Bundle bundle = new Bundle();
                bundle.putString("TAG_ACCOUNT", TAG_ACCOUNT);
                intent.putExtras(bundle);//將參數放入intent

                startActivity(intent);
                finish();
            }else if(returnvalue == 2){
                AlertDialog.Builder dialog = new AlertDialog.Builder(Forgetpwd.this);
                dialog.setTitle("");
                dialog.setMessage("查無此帳號!");
                dialog.show();
            }

        }
    }



}
