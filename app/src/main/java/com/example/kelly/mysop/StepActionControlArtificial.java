package com.example.kelly.mysop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class StepActionControlArtificial extends Activity {
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_create_product = "http://140.115.80.237/front/mysop_ACArtificial.jsp";
    private static final String TAG_SUCCESS = "success";

    //private static String Step="";

    private static TextView steporder;

    String TAG_STEP_NUMBER = "";
    int TAG_STEP_ORDER = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_action_control_artificial);
/*        steporder = (TextView)findViewById(R.id.textView2);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();	//取得Bundle
        TAG_STEP_NUMBER = bundle.getString("TAG_STEP_NUMBER");
        TAG_STEP_ORDER = bundle.getInt("TAG_STEP_ORDER");

        steporder.setText( Integer.toString(TAG_STEP_ORDER));
*/
        //new CheckStep().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_step_action_control_artificial, menu);
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

    public void artfiOpen(View v){
        Intent it = new Intent(StepActionControlArtificial.this,Stepdescription.class);
        startActivity(it);
    }

    //讀取第幾步驟
/*    class CheckStep extends AsyncTask<String, String, String> {


        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StepActionControlArtificial.this);
            pDialog.setMessage("QR code checking.... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            //寫死 Stepnumber
            String Stepnumber="3";

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("Stepnumber", Stepnumber));

            JSONObject json = StepActionControlArtificial.this.jsonParser.makeHttpRequest(StepActionControlArtificial.url_create_product, "GET", params);


            try {
                int e = json.getInt(TAG_SUCCESS);
                if(e == 1) {
                    Step=json.getString("step_order");
                    System.out.println("Here"+Step);

                }else if(e == 6){


                }
            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String valoreOnPostExecute) {

            pDialog.dismiss();
            steporder.setText(Step);

        }
    }*/
}
