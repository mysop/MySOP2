package com.example.kelly.mysop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Login extends Activity {

    private ProgressDialog pDialog;

    private static String url_login = "http://140.115.80.237/front/mysop_login.jsp";
    private EditText et1;
    private EditText et2;
    String strHint1;
    String strHint2;

    JSONParser jsonParser = new JSONParser();
        private static final String TAG_SUCCESS = "success";
    static String TAG_ACCOUNT = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et1 = (EditText) findViewById(R.id.editText);
        et2 = (EditText) findViewById(R.id.editText2);

        strHint1 = getResources().getString(R.string.Login_email);
        strHint2 = getResources().getString(R.string.Login_passward);

        final EditText[] mArray = new EditText[] { et1,et2 };

        for (int i = 0; i < mArray.length; i++) {
            final int j = i;

            //EditText 获得焦点时hint消失，失去焦点时hint显示
            mArray[j].setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (!hasFocus) {
                        if(j == 0){
                            ((TextView) v).setHint(strHint1);
                        }
                        if(j == 1){
                            ((TextView) v).setHint(strHint2);
                        }

                    } else {
                        ((TextView) v).setHint("");
                    }

                }
            });
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    public void forgetpassword (View v){
        Intent it = new Intent(this,Forget.class);
        startActivity(it);
    }

    public void login_check(View view) {
         (Login.this.new CreateAccount()).execute(new String[0]);
    }
    class CreateAccount extends AsyncTask<String, String, String> {
        CreateAccount() {}

        protected void onPreExecute() {
            super.onPreExecute();
            Login.this.pDialog = new ProgressDialog(Login.this);
            Login.this.pDialog.setMessage("Login...");
            Login.this.pDialog.setIndeterminate(false);
            Login.this.pDialog.setCancelable(true);
            Login.this.pDialog.show();
        }

        protected String doInBackground(String... args) {
            String Account = Login.this.et1.getText().toString();
            String Password = Login.this.et2.getText().toString();



            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("Account", Account));
            params.add(new BasicNameValuePair("Password", Password));

            JSONObject json = Login.this.jsonParser.makeHttpRequest(Login.url_login, "POST", params);
            Log.d("Create Response", json.toString());

            try {
                int e = json.getInt(TAG_SUCCESS);
                if(e == 1) {


                }else if(e == 2){

                    Intent i = new Intent(Login.this.getApplicationContext(), Error.class);
                    Login.this.startActivity(i);
                    Login.this.finish();
                }
            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            Login.this.pDialog.dismiss();
        }
    }

}
