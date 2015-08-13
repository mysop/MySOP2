package com.example.kelly.mysop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class StepCaseEnding extends Activity {

    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;
    private EditText edit1,edit2,edit3,edit4,edit5;
    private TextView unit1,unit2,unit3,unit4,unit5;
    private LinearLayout l1,l2,l3,l4,l5;
    private Button change;
    public int Count;
    public int Step=1;



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
        unit1=(TextView)findViewById(R.id.Endunit1);
        unit2=(TextView)findViewById(R.id.Endunit2);
        unit3=(TextView)findViewById(R.id.Endunit3);
        unit4=(TextView)findViewById(R.id.Endunit4);
        unit5=(TextView)findViewById(R.id.Endunit5);
        l1=(LinearLayout)findViewById(R.id.l1);
        l2=(LinearLayout)findViewById(R.id.l2);
        l3=(LinearLayout)findViewById(R.id.l3);
        l4=(LinearLayout)findViewById(R.id.l4);
        l5=(LinearLayout)findViewById(R.id.l5);
        change=(Button)findViewById(R.id.change);


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
    public void endChange (View v){
      for(Step=1;Step<=Count;Step++){
          new SOPContent1().execute();
      }


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
                    Count=products.length();

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
            switch(Count){
                case 1:
                    edit1.setText(productsList.get(0).get(TAG_TEXT));
                    unit1.setText(productsList.get(0).get(TAG_UNIT));
                    l1.setVisibility(0);

                    break;
                case 2:
                    edit1.setText(productsList.get(0).get(TAG_TEXT));
                    unit1.setText(productsList.get(0).get(TAG_UNIT));
                    l1.setVisibility(0);
                    edit2.setText(productsList.get(1).get(TAG_TEXT));
                    unit2.setText(productsList.get(1).get(TAG_UNIT));
                    l2.setVisibility(0);
                    break;
                case 3:
                    edit1.setText(productsList.get(0).get(TAG_TEXT));
                    unit1.setText(productsList.get(0).get(TAG_UNIT));
                    l1.setVisibility(0);
                    edit2.setText(productsList.get(1).get(TAG_TEXT));
                    unit2.setText(productsList.get(1).get(TAG_UNIT));
                    l2.setVisibility(0);
                    edit3.setText(productsList.get(2).get(TAG_TEXT));
                    unit3.setText(productsList.get(2).get(TAG_UNIT));
                    l3.setVisibility(0);
                    break;
                case 4:
                    edit1.setText(productsList.get(0).get(TAG_TEXT));
                    unit1.setText(productsList.get(0).get(TAG_UNIT));
                    l1.setVisibility(0);
                    edit2.setText(productsList.get(1).get(TAG_TEXT));
                    unit2.setText(productsList.get(1).get(TAG_UNIT));
                    l2.setVisibility(0);
                    edit3.setText(productsList.get(2).get(TAG_TEXT));
                    unit3.setText(productsList.get(2).get(TAG_UNIT));
                    l3.setVisibility(0);
                    edit4.setText(productsList.get(3).get(TAG_TEXT));
                    unit4.setText(productsList.get(3).get(TAG_UNIT));
                    l4.setVisibility(0);
                    break;
                case 5:
                    edit1.setText(productsList.get(0).get(TAG_TEXT));
                    unit1.setText(productsList.get(0).get(TAG_UNIT));
                    l1.setVisibility(0);
                    edit2.setText(productsList.get(1).get(TAG_TEXT));
                    unit2.setText(productsList.get(1).get(TAG_UNIT));
                    l2.setVisibility(0);
                    edit3.setText(productsList.get(2).get(TAG_TEXT));
                    unit3.setText(productsList.get(2).get(TAG_UNIT));
                    l3.setVisibility(0);
                    edit4.setText(productsList.get(3).get(TAG_TEXT));
                    unit4.setText(productsList.get(3).get(TAG_UNIT));
                    l4.setVisibility(0);
                    edit5.setText(productsList.get(4).get(TAG_TEXT));
                    unit5.setText(productsList.get(4).get(TAG_UNIT));
                    l5.setVisibility(0);
                    break;
                default:
                    change.setVisibility(8);
                    break;


            }




            }
        }


    class SOPContent1 extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StepCaseEnding.this);
            pDialog.setMessage("Changing..Please wait...");
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

