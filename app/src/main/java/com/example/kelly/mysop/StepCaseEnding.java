package com.example.kelly.mysop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class StepCaseEnding extends Activity {

    private TextView title;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;
    private EditText edit1;
    private EditText edit2;
    private EditText edit3;
    private EditText edit4;
    private EditText edit5;

    ArrayList<HashMap<String, String>> productsList;
    JSONArray products = null;


    //連線 case ending
    JSONParser jsonParser = new JSONParser();
    private static String url_all_products = "http://140.115.80.237/front/mysop_stepCaseclose.jsp";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_TEXT = "text";
    private static final String TAG_UNIT = "unit";
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_case_ending);

        title=(TextView)findViewById(R.id.endTitle);
        text1=(TextView)findViewById(R.id.endText1);
        text2=(TextView)findViewById(R.id.endText2);
        text3=(TextView)findViewById(R.id.endText3);
        text4=(TextView)findViewById(R.id.endText4);
        text5=(TextView)findViewById(R.id.endText5);
        edit1=(EditText)findViewById(R.id.endEdit1);
        edit2=(EditText)findViewById(R.id.endEdit2);
        edit3=(EditText)findViewById(R.id.endEdit3);
        edit4=(EditText)findViewById(R.id.endEdit4);
        edit5=(EditText)findViewById(R.id.endEdit5);

        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();

        new SOPContent().execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_step_case_ending, menu);
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

    class SOPContent extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StepCaseEnding.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            //先寫死stepnumber
            String Stepnumber ="1" ;


            ArrayList params = new ArrayList();


            params.add(new BasicNameValuePair("Stepnumber", Stepnumber) );

            // 抓紀錄
          JSONObject json1 = StepCaseEnding.this.jsonParser.makeHttpRequest(StepCaseEnding.url_all_products, "GET", params);



            try {

                //讀取紀錄
                int e = json1.getInt(TAG_SUCCESS);
                if(e == 1) {

                    products = json1.getJSONArray(TAG_PRODUCTS);

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String text = c.getString(TAG_TEXT);
                        String unit = c.getString(TAG_UNIT);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_TEXT, text);
                        map.put(TAG_UNIT, unit);

                        // adding HashList to ArrayList
                        productsList.add(map);
                    }
                    }



            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();


            }
        }
    }

