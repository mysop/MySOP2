package com.example.kelly.mysop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Home extends Activity {
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    ArrayList<HashMap<String, String>> productsList;
    private static String url_all_products = "http://140.115.80.237/front/mysop_home.jsp";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_SOPNAME = "sopname";
    private static final String TAG_PICTURE = "picture";

    JSONArray products = null;

//    private ListView homelist;
//    MyAdapter adapter = null;
//
//    private ListView homelist1;
//    MyAdapter1 adapter1 = null;
//
//    private String[] list;
//    private String[] name;
//    private String[] logos;
//
//    private String[] list2;
//    private String[] name2;
//    private String[] logos2;
//
//    private String[] list1;
//    private String[] name1;
//    private String[] logos1;

    private ListView listInput;
    private ListView listInput1;
    //private ArrayAdapter<String> adapter;
    MyAdapter adapter = null;
    MyAdapter1 adapter1 = null;
    //存sopname 作者
    private String[] sopname;
    private String[] master;
    private String[] sopnumber;
    private String[] photo;

    private String[] sopname1;
    private String[] master1;
    private String[] sopnumber1;
    private String[] photo1;

    //計算product 長度
    public int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        productsList = new ArrayList<HashMap<String, String>>();

//        homelist = (ListView)findViewById(R.id.homelist);
//        homelist1 = (ListView)findViewById(R.id.homelist1);
        listInput = (ListView)findViewById(R.id.list_sop);
        listInput1 = (ListView)findViewById(R.id.list_sop2);

        new LoadAll().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    private ListView.OnItemClickListener listener = new ListView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            Toast.makeText(getApplicationContext(), "你選擇的是" + sopname[position]+"/n你必須登入後才能閱覽詳細內容", Toast.LENGTH_SHORT).show();


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

    //登入
    public void goToLogin (View v){
        Intent it = new Intent(this,Login.class);
        startActivity(it);
    }
    //註冊
    public void goToRegister (View v){
        Intent it = new Intent(this,Register.class);
        startActivity(it);
    }

    class LoadAll extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Home.this);
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
           // params.add(new BasicNameValuePair("Account", TAG_ACCOUNT) );
            // getting JSON string from URL
            JSONObject json = Home.this.jsonParser.makeHttpRequest(Home.url_all_products,"GET", params);


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
                        String username = c.getString(TAG_USERNAME);
                        String picture = c.getString(TAG_PICTURE);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_SOPNAME, sopname);
                        map.put(TAG_USERNAME, username);
                        map.put(TAG_PICTURE, picture);


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
//
//            int k=0;
//            if(products.length()%2==0){
//                x=products.length()/2;
//            }else{
//                x=(products.length()+1)/2;
//            }
//            System.out.println("MM"+products.length()+"x"+x);
//
//
//            list = new String[x];
//            name = new String[x];
//            logos= new String[x];
////            list1 = new String[products.length()/2];
////            name1 = new String[products.length()/2];
////            logos1 = new String[products.length()/2];
//            list2 = new String[(products.length()+1)/2];
//            name2 = new String[(products.length()+1)/2];
//            logos2 = new String[(products.length()+1)/2];
//
//            if(products.length()%2==0){
//                for (int i = 0; i <products.length(); i=i+2) {
//                    list[k] = productsList.get(i).get(TAG_USERNAME);
//                    name[k] = productsList.get(i).get(TAG_SOPNAME);
//                    logos[k] = productsList.get(i).get(TAG_PICTURE);
//                    list2[k] = productsList.get(i+1).get(TAG_USERNAME);
//                    name2[k] = productsList.get(i+1).get(TAG_SOPNAME);
//                    logos2[k] = productsList.get(i+1).get(TAG_PICTURE);
//                    k++;
//                }
//            }else{
//                for (int i = 0; i <products.length()-1; i=i+2) {
//                    list[k] = productsList.get(i).get(TAG_USERNAME);
//                    name[k] = productsList.get(i).get(TAG_SOPNAME);
//                    logos[k] = productsList.get(i).get(TAG_PICTURE);
//                    list2[k] = productsList.get(i+1).get(TAG_USERNAME);
//                    name2[k] = productsList.get(i+1).get(TAG_SOPNAME);
//                    logos2[k] = productsList.get(i+1).get(TAG_PICTURE);
//                    k++;
//                }
//                list[k] = productsList.get(products.length()-1).get(TAG_USERNAME);
//                name[k] = productsList.get(products.length()-1).get(TAG_SOPNAME);
//                logos[k] = productsList.get(products.length()-1).get(TAG_PICTURE);
//                list2[k] = "aa";
//                name2[k] = "bb";
//                logos2[k] ="http://140.115.80.237/front/picture/white.jpg";
//            }
//            // updating UI from Background Thread
////            for (int i = 0; i <x; i++) {
////                list[i] = productsList.get(i).get(TAG_USERNAME);
////                name[i] = productsList.get(i).get(TAG_SOPNAME);
////                logos[i] = productsList.get(i).get(TAG_PICTURE);
////            }
////
////            for (int i = products.length()-1; i >=x; i--) {
////                list1[k] = productsList.get(i).get(TAG_USERNAME);
////                name1[k] = productsList.get(i).get(TAG_SOPNAME);
////                logos1[k] = productsList.get(i).get(TAG_PICTURE);
////                k++;
////            }
//            adapter = new MyAdapter(Home.this);
//            homelist.setAdapter(adapter);
//           // adapter1 = new MyAdapter1(Home.this);
//            //homelist1.setAdapter(adapter1);

            int k=0;
            if(products.length()%2==0){
                int test2 =products.length();
                x=products.length()/2;
            }else{
                int test1 =products.length()+1;
                x=(products.length()+1)/2;
            }

            sopname = new String[x];
            master = new String[x];
            photo = new String[x];
            sopnumber = new String[x];
            sopname1 = new String[products.length()/2];
            master1 = new String[products.length()/2];
            photo1 = new String[products.length()/2];
            sopnumber1 = new String[products.length()/2];

            for (int i = 0; i < x; i++){
                sopname[i]=productsList.get(i).get(TAG_SOPNAME);
                master[i]=productsList.get(i).get(TAG_USERNAME);
                photo[i]=productsList.get(i).get(TAG_PICTURE);
            }
            for (int i = products.length()-1; i >=x; i--) {
                sopname1[k]=productsList.get(i).get(TAG_SOPNAME);
                master1[k]=productsList.get(i).get(TAG_USERNAME);
                photo1[k]=productsList.get(i).get(TAG_PICTURE);
                k++;
            }

            adapter = new MyAdapter(Home.this);
            adapter1= new MyAdapter1(Home.this);
            listInput.setAdapter(adapter);
            listInput1.setAdapter(adapter1);

            listInput.setOnItemClickListener(listener);
            listInput1.setOnItemClickListener(listener);

        }

    }

//    public class MyAdapter extends BaseAdapter {
//        private LayoutInflater myInflater;
//
//
//        public MyAdapter(Context c) {
//            myInflater = LayoutInflater.from(c);
//        }
//
//        @Override
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return name.length;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            // TODO Auto-generated method stub
//            return name[position];
//        }
//
//        @Override
//        public long getItemId(int position) {
//            // TODO Auto-generated method stub
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            // TODO Auto-generated method stub
//            convertView = myInflater.inflate(R.layout.homepitcure, null);
//
//            ImageView Logo = (ImageView) convertView.findViewById(R.id.imglogo);
//            TextView Name = (TextView) convertView.findViewById(R.id.name);
//            TextView number = (TextView) convertView
//                    .findViewById(R.id.txtengname);
//            ImageView Logo2 = (ImageView) convertView.findViewById(R.id.imglogo2);
//            TextView Name2 = (TextView) convertView.findViewById(R.id.name2);
//            TextView number2 = (TextView) convertView
//                    .findViewById(R.id.txtengname2);
//            LinearLayout l2 = (LinearLayout)convertView.findViewById(R.id.secondlayout);
//
//            new DownloadImageTask(Logo)
//                    .execute(logos[position]);
//            Name.setText(name[position]);
//            number.setText(list[position]);
//
//
//            new DownloadImageTask(Logo2)
//                    .execute(logos2[position]);
//            Name2.setText(name2[position]);
//            number2.setText(list2[position]);
//            if(list2[position] != "aa"){
//                l2.setVisibility(0);
//            }
//
//
//            return convertView;
//        }
//
//    }

    //另一邊
//    public class MyAdapter1 extends BaseAdapter {
//        private LayoutInflater myInflater;
//
//
//        public MyAdapter1(Context c) {
//            myInflater = LayoutInflater.from(c);
//        }
//
//        @Override
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return name1.length;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            // TODO Auto-generated method stub
//            return name1[position];
//        }
//
//        @Override
//        public long getItemId(int position) {
//            // TODO Auto-generated method stub
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            // TODO Auto-generated method stub
//            convertView = myInflater.inflate(R.layout.homepitcure, null);
//
//            ImageView Logo1 = (ImageView) convertView.findViewById(R.id.imglogo);
//            TextView Name1 = (TextView) convertView.findViewById(R.id.name);
//            TextView number1 = (TextView) convertView
//                    .findViewById(R.id.txtengname);
//
//            new DownloadImageTask(Logo1)
//                   .execute(logos1[position]);
//            Name1.setText(name1[position]);
//            number1.setText(list1[position]);
//
//            return convertView;
//        }
//
//    }

    //圖片網址
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
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
            return master.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return master[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            convertView = myInflater.inflate(R.layout.searchmyxml, null);
            TextView Name = (TextView) convertView.findViewById(R.id.name);
            TextView number = (TextView) convertView
                    .findViewById(R.id.txtengname);
            ImageView MysopLogo = (ImageView) convertView.findViewById(R.id.mysoplogo);

            new DownloadImageTask(MysopLogo)
                    .execute(photo[position]);
            System.out.println(" PI" + photo[position]);

            Name.setText(sopname[position]);
            number.setText(master[position]);

            return convertView;
        }

    }

    public class MyAdapter1 extends BaseAdapter {
        private LayoutInflater myInflater;


        public MyAdapter1(Context c) {
            myInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return master1.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return master1[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            convertView = myInflater.inflate(R.layout.searchmyxml, null);

            TextView Name1 = (TextView) convertView.findViewById(R.id.name);
            TextView number1 = (TextView) convertView
                    .findViewById(R.id.txtengname);

            ImageView MysopLogo1 = (ImageView) convertView.findViewById(R.id.mysoplogo);

            new DownloadImageTask(MysopLogo1)
                    .execute(photo1[position]);
            System.out.println(" PI1" + photo1[position]);

            Name1.setText(sopname1[position]);
            number1.setText(master1[position]);

            return convertView;
        }

    }

}
