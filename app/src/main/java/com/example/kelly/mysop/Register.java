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
    private static String url_create_product = "http://140.115.80.237/front/mysop_register.jsp";
    private static final String TAG_SUCCESS = "success";
    String TAG_ACCOUNT = "";


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
        String Password = Register.this.et2.getText().toString();
        String ConfirmPassword = Register.this.et3.getText().toString();
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        String Email = Register.this.et1.getText().toString();
        if(ConfirmPassword.equals(Password)&&Email.matches(str)){
            (Register.this.new CreateAccount()).execute(new String[0]);
        }else if(!ConfirmPassword.equals(Password)&&Email.matches(str)){
            AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
            dialog.setTitle("咦！");
            dialog.setMessage("請確認密碼一致");
            dialog.show();
        }else if(ConfirmPassword.equals(Password)&&!Email.matches(str)){
            AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
            dialog.setTitle("咦！");
            dialog.setMessage("帳號不符合格式");
            dialog.show();
        }else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
            dialog.setTitle("咦！");
            dialog.setMessage("帳號不符合格式，且密碼須一致");
            dialog.show();
        }
    }

    class CreateAccount extends AsyncTask<String, String, String> {
        CreateAccount() {}

        protected void onPreExecute() {
            super.onPreExecute();
            Register.this.pDialog = new ProgressDialog(Register.this);
            Register.this.pDialog.setMessage("Creating Account...");
            Register.this.pDialog.setIndeterminate(false);
            Register.this.pDialog.setCancelable(true);
            Register.this.pDialog.show();
        }

        protected String doInBackground(String... args) {
            String Account = Register.this.et1.getText().toString();
            String Password = Register.this.et2.getText().toString();
            //String ConfirmPassword = Register.this.et3.getText().toString();
            String Name = Register.this.et4.getText().toString();


            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("Account", Account));
            params.add(new BasicNameValuePair("Password", Password));
            //params.add(new BasicNameValuePair("ConfirmPassword", ConfirmPassword));
            params.add(new BasicNameValuePair("Name", Name));
            JSONObject json = Register.this.jsonParser.makeHttpRequest(Register.url_create_product, "POST", params);
            Log.d("Create Response", json.toString());

            try {
                int e = json.getInt(TAG_SUCCESS);
                if(e == 1) {
                    Register.this.TAG_ACCOUNT = Register.this.et1.getText().toString();
                    Intent i = new Intent(Register.this.getApplicationContext(), Emailverify.class);
                   //Intent i = new Intent(Register.this.getApplicationContext(), Search.class);

                    //設定傳送參數
                    Bundle bundle = new Bundle();
                    bundle.putString("TAG_ACCOUNT", TAG_ACCOUNT);
                    //bundle.putString("TAG_Key", "");
                    i.putExtras(bundle);	//將參數放入intent

                    Register.this.startActivity(i);
                    Register.this.finish();
                }else if(e == 2){
                    //帳號有人使用了
                    Intent ii = new Intent(Register.this.getApplicationContext(),RegisterError.class);
                    Register.this.startActivity(ii);
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


