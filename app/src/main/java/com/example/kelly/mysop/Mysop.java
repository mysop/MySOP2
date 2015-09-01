package com.example.kelly.mysop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Mysop extends Activity {



    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    ArrayList<HashMap<String, String>> productsList;
    //private static String url_all_products = "http://localhost:8080/kelly/test_getall.jsp";
    // private static String url_all_products = "http://140.115.80.237/front/test_getall.jsp";
    private static String url_all_products = "http://140.115.80.237/front/mysop_mysop.jsp";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_CASENUMBER = "casenumber";
    private static final String TAG_SOPNAME = "sopname";
    private static final String TAG_STARTRULE = "startrule";
    private static final String TAG_STARTVALUE = "startvalue";
    private static String str,timedifference;
    public int check;

    private ListView listInput;
    //private ArrayAdapter<String> adapter;
    MyAdapter adapter = null;
    private ArrayList<String> items;

    private ImageView picture;
    private TextView title;
    private TextView master;

    JSONArray products = null;

   //帳號先寫死
    String TAG_ACCOUNT = "test@gmail.com";


    //存casenumber  sopname
    private String[] list;
    private String[] name;
    private int[] logos = new int[] { R.drawable.nfc, R.drawable.beacon,
            R.drawable.gps,R.drawable.qrcode,R.drawable.white };
    int[] key;
    private String[] timesee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysop);
        listInput = (ListView)findViewById(R.id.list_sop);
        items = new ArrayList<String>();
       // adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,items);


//        Intent intent = this.getIntent();
//        Bundle bundle = intent.getExtras();
//        TAG_ACCOUNT=bundle.getString("TAG_ACCOUNT");

                // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
        // Loading products in Background Thread
        new LoadAllProducts().execute();

        // TAG_ACCOUNT = bundle.getString("TAG_ACCOUNT");	//輸出Bundle內容

        //時間
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
        Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
        str = formatter.format(curDate);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mysop, menu);
        return true;
    }

    private ListView.OnItemClickListener listener = new ListView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            Toast.makeText(getApplicationContext(), "你選擇的是" + list[position], Toast.LENGTH_SHORT).show();

            Bundle bundle = new Bundle();
            bundle.putString("TAG_CASE_NUMBER", list[position]);
            Intent it = new Intent(Mysop.this,StepActionControl.class);
            it.putExtras(bundle);//將參數放入intent
            startActivity(it);

        }

    };

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
    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Mysop.this);
            pDialog.setMessage("Loading products. Please wait...");
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
            params.add(new BasicNameValuePair("Account", TAG_ACCOUNT) );
            // getting JSON string from URL
            JSONObject json = Mysop.this.jsonParser.makeHttpRequest(Mysop.url_all_products,"GET", params);


            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String sopname = c.getString(TAG_SOPNAME);
                        String sopnumber = c.getString(TAG_CASENUMBER);
                        String startrule = c.getString(TAG_STARTRULE);
                        String startvalue = c.getString(TAG_STARTVALUE);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_SOPNAME, sopname);
                        map.put(TAG_CASENUMBER, sopnumber);
                        map.put(TAG_STARTRULE, startrule);
                        map.put(TAG_STARTVALUE,startvalue);

                        System.out.println("HELLO" + sopname + sopnumber);

                        // adding HashList to ArrayList
                        productsList.add(map);
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

            list = new String[products.length()];
            name = new String[products.length()];
            key = new int[products.length()];
            timesee = new String[products.length()];
            // updating UI from Background Thread
            for (int i = 0; i < products.length(); i++) {
                list[i] = productsList.get(i).get(TAG_CASENUMBER);
                System.out.println("JJ"+list[i]);
                name[i] = productsList.get(i).get(TAG_SOPNAME);
               // items.add(productsList.get(i).get(TAG_SOPNAME) + "\n" + productsList.get(i).get(TAG_CASENUMBER));
                //listInput.setAdapter(adapter);
                switch (productsList.get(i).get(TAG_STARTRULE)){
                    case "1":
                       // cagetory.setText("人工啟動");
                        key[i]=4;
                        break;
                    case "2":
                        //cagetory.setText("前一步驟\n完工");
                        key[i]=4;
                        break;
                    case "3":
                        //cagetory.setText("Beacon");
                        key[i]=1;
                        break;
                    case "4":
                        //cagetory.setText("QR code");
                        key[i]=3;
                        break;
                    case "5":
                        //cagetory.setText("NFC");
                        key[i]=0;
                        break;
                    case "6":
                        //cagetory.setText("定位");
                        key[i]=2;
                        break;
                    case "7":
                        //cagetory.setText("時間到期");
                        key[i]=4;
                        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
                        Date now = null;

                        try {
                            now = df.parse(productsList.get(i).get(TAG_STARTVALUE));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date date = null;
                        try {
                            date = df.parse(str);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        long l;

                        //比較時間大小
                        if(now.getTime()>date.getTime()) {
                            //未過期
                            l = now.getTime() - date.getTime();
                            check=0;
                        }else{
                            //過期
                            l = date.getTime() - now.getTime();
                            check=1;
                        }
                        //計算時間差
                        long month=l/(30 * 24 * 60 * 60 * 1000);
                        long day = l / (24 * 60 * 60 * 1000- month * 30);
                        long hour = (l / (60 * 60 * 1000) - month * 30 * 24 - day * 24);
                        long min = ((l / (60 * 1000)) - month * 30 * 24 * 60- day * 24 * 60 - hour * 60);
                        long s = (l / 1000 - month * 30 * 24 * 60 * 60- day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                        System.out.println(month+"月" + day + "天" + hour + "小时" + min + "分" + s + "秒");

                        if(check==0) {

                            if (month == 0) {
                                timedifference="還差" + day + "天" ;
                            } else if (month == 0 && day == 0) {
                                timedifference="還差" + hour + "小时" + min + "分";
                            } else if (month == 0 && day == 0 && hour == 0) {
                                timedifference="還差" + min + "分";
                            } else {
                                timedifference="還差" + month + "月" + day + "天";
                            }
                        }else{
                            //過期
                           // timedifference.setTextColor(Color.RED);
                            if (month == 0) {
                                timedifference="過期" + day + "天" ;
                            } else if (month == 0 && day == 0) {
                                timedifference="過期" + hour + "小时" + min + "分";
                            } else if (month == 0 && day == 0 && hour == 0) {
                                timedifference="過期" + min + "分";
                            } else {
                                timedifference="過期" + month + "月" + day + "天";
                            }
                        }
                        timesee[i]=timedifference;
                        break;
                }
            }



            adapter = new MyAdapter(Mysop.this);
            listInput.setAdapter(adapter);

            listInput.setOnItemClickListener(listener);
        }

    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater myInflater;


        public MyAdapter(Context c) {
            myInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return name.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return name[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            convertView = myInflater.inflate(R.layout.myxml, null);

            ImageView Logo = (ImageView) convertView.findViewById(R.id.imglogo);
            TextView Name = (TextView) convertView.findViewById(R.id.name);
            TextView number = (TextView) convertView
                    .findViewById(R.id.txtengname);
            TextView time = (TextView)convertView.findViewById(R.id.timetext);

            if(logos[key[position]]!=R.drawable.white){
                Logo.setVisibility(0);
                time.setVisibility(8);
            }
            Logo.setImageResource(logos[key[position]]);
            Name.setText(name[position]);
            number.setText(list[position]);
            time.setText(timesee[position]);


            return convertView;
        }

    }
}