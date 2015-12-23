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

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Emailverify extends Activity {

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String url = "http://140.115.80.237/front/mysop_captcha.jsp";
    private static final String TAG_SUCCESS = "success";
    private EditText InputEmailVerify;
    String TAG_ACCOUNT = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailverify);
        InputEmailVerify = (EditText) findViewById(R.id.editText3);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();	//取得Bundle
        TAG_ACCOUNT = bundle.getString("TAG_ACCOUNT");	//輸出Bundle內容


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


        Intent i = new Intent(this, Search.class);
        Bundle bundle = new Bundle();
        bundle.putString("TAG_ACCOUNT", TAG_ACCOUNT);
        i.putExtras(bundle);	//將參數放入intent
        startActivity(i);
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
            params.add(new BasicNameValuePair("Emailverify", InputEmailVerify));
            params.add(new BasicNameValuePair("Account", TAG_ACCOUNT));

            JSONObject json = Emailverify.this.jsonParser.makeHttpRequest(Emailverify.url, "POST", params);
            Log.d("Create Response", json.toString());

            try {
                int e = json.getInt(TAG_SUCCESS);
                if(e == 1) {
/*                    Intent i = new Intent(Emailverify.this.getApplicationContext(), Home.class);
                    Emailverify.this.startActivity(i);
                    Emailverify.this.finish();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Emailverify.this);
                    dialog.setTitle("");
                    dialog.setMessage("成功！");
                    dialog.show(); */


                }else if(e == 6){
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

