package com.example.kelly.mysop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StepCaseEnding extends Activity {

    private TextView text1,text2,text3,text4,text5,text6,text7,text8,text9,text10;
    private EditText edit1,edit2,edit3,edit4,edit5,edit6,edit7,edit8,edit9,edit10;
    private TextView unit1,unit2,unit3,unit4,unit5,unit6,unit7,unit8,unit9,unit10;
    private LinearLayout l1,l2,l3,l4,l5,l6,l7,l8,l9,l10;
    private Button change;
    public int Count;
    public int Step=1;

    //先寫死帳號和 SOPnumber
    String TAG_ACCOUNT = "q@gmail.com";
    String Sopnumber = "20150814";
    ArrayList<HashMap<String, String>> productsList;
    ArrayList<HashMap<String, String>> valueList;
    JSONArray products = null;
    JSONArray products1 = null;

    //連線 case ending
    JSONParser jsonParser = new JSONParser();
    //第一個是傳入紀錄說明和記錄值  第二個是更改紀錄值  第三個是 結案（刪掉代辦）
    private static String url_all_products = "http://140.115.80.237/front/mysop_stepCaseclose.jsp";
    private static String url_all_products1 = "http://140.115.80.237/front/mysop_stepCaseclose1.jsp";
    private static String url_all_products2 = "http://140.115.80.237/front/mysop_stepCaseclose2.jsp";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_TEXT = "text";
    private static final String TAG_UNIT = "unit";
    private static final String TAG_VALUE = "value";
    private ProgressDialog pDialog;

    JSONParser jParser = new JSONParser();
    EditText[] edit = new EditText[20];
    String RecordText[] = new String[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_case_ending);

        change=(Button)findViewById(R.id.change);

        // TAG_ACCOUNT = bundle.getString("TAG_ACCOUNT");	//輸出Bundle內容
        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
        valueList = new ArrayList<HashMap<String, String>>();


        new LoadInput().execute();

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
        SOPContent1[] SO= new SOPContent1[10];
      for(Step=0;Step<Count;Step++){
          SO[Step]=new SOPContent1();
          SO[Step].execute(Step);
      }
        AlertDialog.Builder dialog = new AlertDialog.Builder(StepCaseEnding.this);
        dialog.setTitle("");
        dialog.setMessage("更改成功");
        dialog.show();
    }

    public void close (View v){
        new SOPContent2().execute();
        Intent it = new Intent(this,Mysop.class);
        startActivity(it);
    }

    //更改紀錄
    class SOPContent1 extends AsyncTask<Integer, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StepCaseEnding.this);
            pDialog.setMessage("Changing..Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
          //  pDialog.show();

        }


        protected String doInBackground(Integer... args) {

            //先寫死stepnumber
            String Casenumber ="4" ;

            int a=args[0];
            String RecordOrder=Integer.toString(a+1);
            RecordText[a] = StepCaseEnding.this.edit[a].getText().toString();

            ArrayList params = new ArrayList();
            System.out.println("jj"+RecordText[a]);

            params.add(new BasicNameValuePair("Newtext", RecordText[a]) );
            params.add(new BasicNameValuePair("Casenumber", Casenumber) );
            params.add(new BasicNameValuePair("Recordorder", RecordOrder) );

            // 上傳更改的紀錄
            JSONObject json1 = StepCaseEnding.this.jsonParser.makeHttpRequest(StepCaseEnding.url_all_products1, "POST", params);

            try {
                //更改紀錄
                int e = json1.getInt(TAG_SUCCESS);
                if(e == 1) {

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
          //  pDialog.dismiss();

        }
    }
    //結案 刪除sop
    class SOPContent2 extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StepCaseEnding.this);
            pDialog.setMessage("Being Close. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            ArrayList params2 = new ArrayList();

            params2.add(new BasicNameValuePair("Account", TAG_ACCOUNT));
            params2.add(new BasicNameValuePair("Sopnumber", Sopnumber) );

            JSONObject json2 = StepCaseEnding.this.jsonParser.makeHttpRequest(StepCaseEnding.url_all_products2,"POST",params2);
            try {

                int e3 = json2.getInt(TAG_SUCCESS);
                if(e3 == 1) {
                }

            } catch (JSONException var9) {
                var9.printStackTrace();
            }
            pDialog.dismiss();
            return null;
        }

    }


    //讀取紀錄 和紀錄說明
    class LoadInput extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StepCaseEnding.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            //先寫死stepnumber casenumber
            String Stepnumber ="1" ;
            String Casenumber="4";


            params.add(new BasicNameValuePair("Stepnumber", Stepnumber) );
            params.add(new BasicNameValuePair("Casenumber", Casenumber) );

            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);



            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                //step = json.getString("step");
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    products = json.getJSONArray(TAG_PRODUCTS);

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
                    //抓記錄值
                    products1=json.getJSONArray("values");
                    for(int i=0 ;i<products1.length();i++){
                        JSONObject c =products1.getJSONObject(i);
                        String value=c.getString(TAG_VALUE);
                        HashMap<String,String> vmap = new HashMap<String, String>();
                        vmap.put(TAG_VALUE,value);

                        valueList.add(vmap);
                    }
                } else {



                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
/*            runOnUiThread(new Runnable() {
                public void run() {

                }
            }); */

            LinearLayout ly = (LinearLayout)findViewById(R.id.endlayout);

            Count=products.length();

            for(int i=0; i<products.length();i++) {

                TextView text1 = new TextView(StepCaseEnding.this);
                text1.setTextSize(17);
                text1.setText(productsList.get(i).get(TAG_TEXT)+"("+productsList.get(i).get(TAG_UNIT)+")");


                edit[i] = new EditText(StepCaseEnding.this.getApplicationContext());


                edit[i].setTextColor(Color.rgb(0, 0, 0));
                edit[i].setSingleLine(true);
                //edit.linecolor
                edit[i].setText(valueList.get(i).get(TAG_VALUE));
                ly.addView(text1);
                ly.addView(edit[i]);
            }



        }

    }
    }

