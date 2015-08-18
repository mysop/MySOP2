package com.example.kelly.mysop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_case_ending);


        text1=(TextView)findViewById(R.id.endText1);
        text2=(TextView)findViewById(R.id.endText2);
        text3=(TextView)findViewById(R.id.endText3);
        text4=(TextView)findViewById(R.id.endText4);
        text5=(TextView)findViewById(R.id.endText5);
        text6=(TextView)findViewById(R.id.endText6);
        text7=(TextView)findViewById(R.id.endText7);
        text8=(TextView)findViewById(R.id.endText8);
        text9=(TextView)findViewById(R.id.endText9);
        text10=(TextView)findViewById(R.id.endText10);
        edit1=(EditText)findViewById(R.id.endEdit1);
        edit2=(EditText)findViewById(R.id.endEdit2);
        edit3=(EditText)findViewById(R.id.endEdit3);
        edit4=(EditText)findViewById(R.id.endEdit4);
        edit5=(EditText)findViewById(R.id.endEdit5);
        edit6=(EditText)findViewById(R.id.endEdit6);
        edit7=(EditText)findViewById(R.id.endEdit7);
        edit8=(EditText)findViewById(R.id.endEdit8);
        edit9=(EditText)findViewById(R.id.endEdit9);
        edit10=(EditText)findViewById(R.id.endEdit10);
        unit1=(TextView)findViewById(R.id.Endunit1);
        unit2=(TextView)findViewById(R.id.Endunit2);
        unit3=(TextView)findViewById(R.id.Endunit3);
        unit4=(TextView)findViewById(R.id.Endunit4);
        unit5=(TextView)findViewById(R.id.Endunit5);
        unit6=(TextView)findViewById(R.id.Endunit6);
        unit7=(TextView)findViewById(R.id.Endunit7);
        unit8=(TextView)findViewById(R.id.Endunit8);
        unit9=(TextView)findViewById(R.id.Endunit9);
        unit10=(TextView)findViewById(R.id.Endunit10);
        l1=(LinearLayout)findViewById(R.id.l1);
        l2=(LinearLayout)findViewById(R.id.l2);
        l3=(LinearLayout)findViewById(R.id.l3);
        l4=(LinearLayout)findViewById(R.id.l4);
        l5=(LinearLayout)findViewById(R.id.l5);
        l6=(LinearLayout)findViewById(R.id.l6);
        l7=(LinearLayout)findViewById(R.id.l7);
        l8=(LinearLayout)findViewById(R.id.l8);
        l9=(LinearLayout)findViewById(R.id.l9);
        l10=(LinearLayout)findViewById(R.id.l10);
        change=(Button)findViewById(R.id.change);

        // TAG_ACCOUNT = bundle.getString("TAG_ACCOUNT");	//輸出Bundle內容
        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
        valueList = new ArrayList<HashMap<String, String>>();

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

    class SOPContent extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StepCaseEnding.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
           // pDialog.show();
        }


        protected String doInBackground(String... args) {

            //先寫死stepnumber casenumber
            String Stepnumber ="1" ;
            String Casenumber="4";

            ArrayList params = new ArrayList();
            ArrayList params1 = new ArrayList();

            params.add(new BasicNameValuePair("Stepnumber", Stepnumber) );
            params.add(new BasicNameValuePair("Casenumber", Casenumber) );

            // 抓紀錄說明和記錄值
          JSONObject json1 = StepCaseEnding.this.jsonParser.makeHttpRequest(StepCaseEnding.url_all_products, "GET", params);            try {


                int e = json1.getInt(TAG_SUCCESS);
                if(e == 1) {
                   //讀取紀錄說明
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
                    //抓記錄值
                    products1=json1.getJSONArray("values");
                    for(int i=0 ;i<products1.length();i++){
                        JSONObject c =products1.getJSONObject(i);
                        String value=c.getString(TAG_VALUE);
                        HashMap<String,String> vmap = new HashMap<String, String>();
                        vmap.put(TAG_VALUE,value);

                       valueList.add(vmap);
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
            if(Count<6) {
                switch (Count) {
                    case 1:
                        text1.setText(productsList.get(0).get(TAG_TEXT));
                        unit1.setText(productsList.get(0).get(TAG_UNIT));
                        edit1.setText(valueList.get(0).get(TAG_VALUE));
                        l1.setVisibility(0);
                        break;
                    case 2:
                        text1.setText(productsList.get(0).get(TAG_TEXT));
                        unit1.setText(productsList.get(0).get(TAG_UNIT));
                        edit1.setText(valueList.get(0).get(TAG_VALUE));
                        l1.setVisibility(0);
                        text2.setText(productsList.get(1).get(TAG_TEXT));
                        unit2.setText(productsList.get(1).get(TAG_UNIT));
                        edit2.setText(valueList.get(1).get(TAG_VALUE));
                        l2.setVisibility(0);
                        break;
                    case 3:
                        text1.setText(productsList.get(0).get(TAG_TEXT));
                        unit1.setText(productsList.get(0).get(TAG_UNIT));
                        edit1.setText(valueList.get(0).get(TAG_VALUE));
                        l1.setVisibility(0);
                        text2.setText(productsList.get(1).get(TAG_TEXT));
                        unit2.setText(productsList.get(1).get(TAG_UNIT));
                        edit2.setText(valueList.get(1).get(TAG_VALUE));
                        l2.setVisibility(0);
                        text3.setText(productsList.get(2).get(TAG_TEXT));
                        unit3.setText(productsList.get(2).get(TAG_UNIT));
                        edit3.setText(valueList.get(2).get(TAG_VALUE));
                        l3.setVisibility(0);
                        break;
                    case 4:
                        text1.setText(productsList.get(0).get(TAG_TEXT));
                        unit1.setText(productsList.get(0).get(TAG_UNIT));
                        edit1.setText(valueList.get(0).get(TAG_VALUE));
                        l1.setVisibility(0);
                        text2.setText(productsList.get(1).get(TAG_TEXT));
                        unit2.setText(productsList.get(1).get(TAG_UNIT));
                        edit2.setText(valueList.get(1).get(TAG_VALUE));
                        l2.setVisibility(0);
                        text3.setText(productsList.get(2).get(TAG_TEXT));
                        unit3.setText(productsList.get(2).get(TAG_UNIT));
                        edit3.setText(valueList.get(2).get(TAG_VALUE));
                        l3.setVisibility(0);
                        text4.setText(productsList.get(3).get(TAG_TEXT));
                        unit4.setText(productsList.get(3).get(TAG_UNIT));
                        edit4.setText(valueList.get(3).get(TAG_VALUE));
                        l4.setVisibility(0);
                        break;
                    case 5:
                        text1.setText(productsList.get(0).get(TAG_TEXT));
                        unit1.setText(productsList.get(0).get(TAG_UNIT));
                        edit1.setText(valueList.get(0).get(TAG_VALUE));
                        l1.setVisibility(0);
                        text2.setText(productsList.get(1).get(TAG_TEXT));
                        unit2.setText(productsList.get(1).get(TAG_UNIT));
                        edit2.setText(valueList.get(1).get(TAG_VALUE));
                        l2.setVisibility(0);
                        text3.setText(productsList.get(2).get(TAG_TEXT));
                        unit3.setText(productsList.get(2).get(TAG_UNIT));
                        edit3.setText(valueList.get(2).get(TAG_VALUE));
                        l3.setVisibility(0);
                        text4.setText(productsList.get(3).get(TAG_TEXT));
                        unit4.setText(productsList.get(3).get(TAG_UNIT));
                        edit4.setText(valueList.get(3).get(TAG_VALUE));
                        l4.setVisibility(0);
                        text5.setText(productsList.get(4).get(TAG_TEXT));
                        unit5.setText(productsList.get(4).get(TAG_UNIT));
                        edit5.setText(valueList.get(4).get(TAG_VALUE));
                        l5.setVisibility(0);
                        break;
                    default:
                        change.setVisibility(8);
                        break;

                }
            }else{
                text1.setText(productsList.get(0).get(TAG_TEXT));
                unit1.setText(productsList.get(0).get(TAG_UNIT));
                edit1.setText(valueList.get(0).get(TAG_VALUE));
                l1.setVisibility(0);
                text2.setText(productsList.get(1).get(TAG_TEXT));
                unit2.setText(productsList.get(1).get(TAG_UNIT));
                edit2.setText(valueList.get(1).get(TAG_VALUE));
                l2.setVisibility(0);
                text3.setText(productsList.get(2).get(TAG_TEXT));
                unit3.setText(productsList.get(2).get(TAG_UNIT));
                edit3.setText(valueList.get(2).get(TAG_VALUE));
                l3.setVisibility(0);
                text4.setText(productsList.get(3).get(TAG_TEXT));
                unit4.setText(productsList.get(3).get(TAG_UNIT));
                edit4.setText(valueList.get(3).get(TAG_VALUE));
                l4.setVisibility(0);
                text5.setText(productsList.get(4).get(TAG_TEXT));
                unit5.setText(productsList.get(4).get(TAG_UNIT));
                edit5.setText(valueList.get(4).get(TAG_VALUE));
                l5.setVisibility(0);

                switch (Count) {
                    case 6:
                        text6.setText(productsList.get(5).get(TAG_TEXT));
                        unit6.setText(productsList.get(5).get(TAG_UNIT));
                        edit6.setText(valueList.get(5).get(TAG_VALUE));
                        l6.setVisibility(0);
                        break;
                    case 7:
                        text6.setText(productsList.get(5).get(TAG_TEXT));
                        unit6.setText(productsList.get(5).get(TAG_UNIT));
                        edit6.setText(valueList.get(5).get(TAG_VALUE));
                        l6.setVisibility(0);
                        text7.setText(productsList.get(6).get(TAG_TEXT));
                        unit7.setText(productsList.get(6).get(TAG_UNIT));
                        edit7.setText(valueList.get(6).get(TAG_VALUE));
                        l7.setVisibility(0);
                        break;
                    case 8:
                        text6.setText(productsList.get(5).get(TAG_TEXT));
                        unit6.setText(productsList.get(5).get(TAG_UNIT));
                        edit6.setText(valueList.get(5).get(TAG_VALUE));
                        l6.setVisibility(0);
                        text7.setText(productsList.get(6).get(TAG_TEXT));
                        unit7.setText(productsList.get(6).get(TAG_UNIT));
                        edit7.setText(valueList.get(6).get(TAG_VALUE));
                        l7.setVisibility(0);
                        text8.setText(productsList.get(7).get(TAG_TEXT));
                        unit8.setText(productsList.get(7).get(TAG_UNIT));
                        edit8.setText(valueList.get(7).get(TAG_VALUE));
                        l8.setVisibility(0);
                        break;
                    case 9:
                        text6.setText(productsList.get(5).get(TAG_TEXT));
                        unit6.setText(productsList.get(5).get(TAG_UNIT));
                        edit6.setText(valueList.get(5).get(TAG_VALUE));
                        l6.setVisibility(0);
                        text7.setText(productsList.get(6).get(TAG_TEXT));
                        unit7.setText(productsList.get(6).get(TAG_UNIT));
                        edit7.setText(valueList.get(6).get(TAG_VALUE));
                        l7.setVisibility(0);
                        text8.setText(productsList.get(7).get(TAG_TEXT));
                        unit8.setText(productsList.get(7).get(TAG_UNIT));
                        edit8.setText(valueList.get(7).get(TAG_VALUE));
                        l8.setVisibility(0);
                        text9.setText(productsList.get(8).get(TAG_TEXT));
                        unit9.setText(productsList.get(8).get(TAG_UNIT));
                        edit9.setText(valueList.get(8).get(TAG_VALUE));
                        l9.setVisibility(0);
                        break;
                    case 10:
                        text6.setText(productsList.get(5).get(TAG_TEXT));
                        unit6.setText(productsList.get(5).get(TAG_UNIT));
                        edit6.setText(valueList.get(5).get(TAG_VALUE));
                        l6.setVisibility(0);
                        text7.setText(productsList.get(6).get(TAG_TEXT));
                        unit7.setText(productsList.get(6).get(TAG_UNIT));
                        edit7.setText(valueList.get(6).get(TAG_VALUE));
                        l7.setVisibility(0);
                        text8.setText(productsList.get(7).get(TAG_TEXT));
                        unit8.setText(productsList.get(7).get(TAG_UNIT));
                        edit8.setText(valueList.get(7).get(TAG_VALUE));
                        l8.setVisibility(0);
                        text9.setText(productsList.get(8).get(TAG_TEXT));
                        unit9.setText(productsList.get(8).get(TAG_UNIT));
                        edit9.setText(valueList.get(8).get(TAG_VALUE));
                        l9.setVisibility(0);
                        text10.setText(productsList.get(9).get(TAG_TEXT));
                        unit10.setText(productsList.get(9).get(TAG_UNIT));
                        edit10.setText(valueList.get(9).get(TAG_VALUE));
                        l10.setVisibility(0);
                        break;
                    default:
                        break;
               }
            }
            }
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

            int a=args[0]+1;
            String RecordOrder=Integer.toString(a);
            String Nexttext="";

            switch (a){
                case 1:
                    Nexttext=StepCaseEnding.this.edit1.getText().toString();
                    break;
                case 2:
                    Nexttext=StepCaseEnding.this.edit2.getText().toString();
                    break;
                case 3:
                    Nexttext=StepCaseEnding.this.edit3.getText().toString();
                    break;
                case 4:
                    Nexttext=StepCaseEnding.this.edit4.getText().toString();
                    break;
                case 5:
                    Nexttext=StepCaseEnding.this.edit5.getText().toString();
                    break;
                case 6:
                    Nexttext=StepCaseEnding.this.edit6.getText().toString();
                    break;
                case 7:
                    Nexttext=StepCaseEnding.this.edit7.getText().toString();
                    break;
                case 8:
                    Nexttext=StepCaseEnding.this.edit8.getText().toString();
                    break;
                case 9:
                    Nexttext=StepCaseEnding.this.edit9.getText().toString();
                    break;
                case 10:
                    Nexttext=StepCaseEnding.this.edit10.getText().toString();
                    break;
                default:
                    break;
            }

            ArrayList params = new ArrayList();
            System.out.println(Nexttext);

            params.add(new BasicNameValuePair("Newtext", Nexttext) );
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
    }

