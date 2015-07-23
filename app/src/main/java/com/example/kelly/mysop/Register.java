package com.example.kelly.mysop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Register extends Activity {

    private ProgressDialog pDialog;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    String strHint1;
    String strHint2;
    String strHint3;
    String strHint4;

    JSONParser jsonParser = new JSONParser();
    private static String url_create_product = "http://140.115.80.237/front/test1.jsp";
    private static final String TAG_SUCCESS = "success";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et1 = (EditText) findViewById(R.id.InputEmail);
        et2 = (EditText) findViewById(R.id.InputPassword);
        et3 = (EditText) findViewById(R.id.ConfirmPassword);
        et4 = (EditText) findViewById(R.id.Name);

        strHint1 = getResources().getString(R.string.Register_InputEmail);
        strHint2 = getResources().getString(R.string.Register_InputPassword);
        strHint3 = getResources().getString(R.string.Register_ConfirmPassword);
        strHint4 = getResources().getString(R.string.Register_Name);

        final EditText[] mArray = new EditText[] { et1,et2,et3,et4 };

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
                        if(j == 2){
                            ((TextView) v).setHint(strHint3);
                        }
                        if(j == 3){
                            ((TextView) v).setHint(strHint4);
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
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    public void register_check(View view){
            (Register.this.new CreateAccount()).execute(new String[0]);
        }

class CreateAccount extends AsyncTask<String, String, String> {
    CreateAccount() {
    }

    protected void onPreExecute() {
        super.onPreExecute();
        Register.this.pDialog = new ProgressDialog(Register.this);
        Register.this.pDialog.setMessage("Creating Account..");
        Register.this.pDialog.setIndeterminate(false);
        Register.this.pDialog.setCancelable(true);
        Register.this.pDialog.show();
    }

    protected String doInBackground(String... args) {
        String Email = Register.this.et1.getText().toString();
        String Password = Register.this.et2.getText().toString();
        String ConfirmPassword = Register.this.et3.getText().toString();
        String Name = Register.this.et4.getText().toString();
        ArrayList params = new ArrayList();
        params.add(new BasicNameValuePair("Email", Email));
        params.add(new BasicNameValuePair("Password", Password));
        params.add(new BasicNameValuePair("ConfirmPassword", ConfirmPassword));
        params.add(new BasicNameValuePair("Name", Name));
        JSONObject json = Register.this.jsonParser.makeHttpRequest(Register.url_create_product, "POST", params);
        Log.d("Create Response", json.toString());

        try {
            int e = json.getInt("success");
            if(e == 1) {
                Intent i = new Intent(Register.this.getApplicationContext(), Emailverify.class);
                Register.this.startActivity(i);
                Register.this.finish();
            }
        } catch (JSONException var9) {
            var9.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(String file_url) {
        Register.this.pDialog.dismiss();
    }
   }
}


