package com.example.kelly.mysop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Emailverify extends Activity {

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String url = "http://localhost:8080/kelly/test-getall.jsp";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_EMAILVERIFY = "emailverify";
    private EditText InputEmailVerify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailverify);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_emailverify, menu);
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

    public void emailverify_check(View view) {

        (Emailverify.this.new CreateAccount()).execute(new String[0]);
    }

    class CreateAccount extends AsyncTask<String, String, String> {
        CreateAccount() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            Emailverify.this.pDialog = new ProgressDialog(Emailverify.this);
            Emailverify.this.pDialog.setMessage("verifying...");
            Emailverify.this.pDialog.setIndeterminate(false);
            Emailverify.this.pDialog.setCancelable(true);
            Emailverify.this.pDialog.show();
        }

        protected String doInBackground(String... args) {
            String InputEmailVerify =  Emailverify.this.InputEmailVerify.getText().toString();

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("EmailVerify", InputEmailVerify));

            JSONObject json = Emailverify.this.jsonParser.makeHttpRequest(Emailverify.url, "POST", params);
            Log.d("Create Response", json.toString());

            try {
                int e = json.getInt("success");
                if(e == 1) {
/*                    Intent i = new Intent(Emailverify.this.getApplicationContext(), Emailverify.class);
                    Emailverify.this.startActivity(i);
                    Emailverify.this.finish(); */
                    Emailverify.this.pDialog.setMessage("CORRECT!");
                    Emailverify.this.pDialog.setIndeterminate(false);
                    Emailverify.this.pDialog.setCancelable(true);
                    Emailverify.this.pDialog.show();


                }else if(e == 2){
                    Intent i = new Intent(Emailverify.this.getApplicationContext(), EmailVertifyError.class);
                    Emailverify.this.startActivity(i);
                    Emailverify.this.finish();
                }
            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            Emailverify.this.pDialog.dismiss();
        }
    }
}

