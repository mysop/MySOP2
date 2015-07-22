package com.example.kelly.mysop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Emailverify extends Activity {


    JSONParser jParser = new JSONParser();
    private static String url_all_products = "http://localhost:8080/kelly/test-getall.jsp";
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

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

        // Check your log cat for JSON reponse
        Log.d("All Products: ", json.toString());

        InputEmailVerify = (EditText) findViewById(R.id.editText3);
        String EmailVerify = InputEmailVerify.getText().toString();

        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                String Emailverify = json.getString(TAG_EMAILVERIFY);

                if(Emailverify == EmailVerify){

                    //成功



                }else{
                    Intent i = new Intent(getApplicationContext(),EmailVertifyError.class);
                    // Closing all previous activities
                    startActivity(i);
                }

                finish();
            }else {

                //也失敗

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}
