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


public class Changepassword extends Activity {


    private ProgressDialog pDialog;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    String strHint1;
    String strHint2;
    String strHint3;

    JSONParser jsonParser = new JSONParser();
    private static String url_changepassword = "http://140.115.80.237/front/mysop_register.jsp";
    private static final String TAG_SUCCESS = "success";
    static String TAG_ACCOUNT = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);


        et1 = (EditText) findViewById(R.id.Originalpassword);
        et2 = (EditText) findViewById(R.id.NewPassword);
        et3 = (EditText) findViewById(R.id.Confirmnewpassward);

        strHint1 = getResources().getString(R.string.Changepassward_Originalpassword);
        strHint2 = getResources().getString(R.string.Changepassward_NewPassword);
        strHint3 = getResources().getString(R.string.Changepassward_Confirmnewpassward);

        final EditText[] mArray = new EditText[] { et1,et2,et3 };

        for (int i = 0; i < mArray.length; i++) {
            final int j = i;

            //EditText 获得焦点时hint消失，失去焦点时hint显示
            mArray[j].setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        getMenuInflater().inflate(R.menu.menu_changepassword, menu);
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

    public void changepassword_check(View view){
        String NewPassword = Changepassword.this.et2.getText().toString();
        String ConfirmNewPassword = Changepassword.this.et3.getText().toString();
        if(ConfirmNewPassword.equals(NewPassword)){
            (Changepassword.this.new CreateAccount()).execute(new String[0]);
        }else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(Changepassword.this);
            dialog.setTitle("咦！");
            dialog.setMessage("請確認密碼一致");
            dialog.show();
        }
    }

    class CreateAccount extends AsyncTask<String, String, String> {
        CreateAccount() {}

        protected void onPreExecute() {
            super.onPreExecute();
            Changepassword.this.pDialog = new ProgressDialog(Changepassword.this);
            Changepassword.this.pDialog.setMessage("Changing...");
            Changepassword.this.pDialog.setIndeterminate(false);
            Changepassword.this.pDialog.setCancelable(true);
            Changepassword.this.pDialog.show();
        }

        protected String doInBackground(String... args) {
            String Originalpassword = Changepassword.this.et1.getText().toString();
            String NewPassword = Changepassword.this.et2.getText().toString();


            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("Account", Login.TAG_ACCOUNT));
            params.add(new BasicNameValuePair("Originalpassword", Originalpassword));
            params.add(new BasicNameValuePair("NewPassword", NewPassword));

            JSONObject json = Changepassword.this.jsonParser.makeHttpRequest(Changepassword.url_changepassword, "POST", params);
            Log.d("Create Response", json.toString());

            try {
                int e = json.getInt(TAG_SUCCESS);
                if(e == 1) {

                    Intent i = new Intent(Changepassword.this.getApplicationContext(), Emailverify.class);
                    Changepassword.this.startActivity(i);
                    Changepassword.this.finish();
                }else if(e == 2){

                    Intent i = new Intent(Changepassword.this.getApplicationContext(), ChangePasswordError.class);
                    Changepassword.this.startActivity(i);
                    Changepassword.this.finish();
                }
            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            Changepassword.this.pDialog.dismiss();
        }
    }



}
