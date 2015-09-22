package com.example.kelly.mysop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    private static final String TAG_LIKE = "like";
    JSONArray products = null;

    //搜尋相關
    private EditText searchName;
    private String searchObject;
    private Button searchButton;
    String TAG_Key="";

    private ListView listInput;
    private ListView listInput1;
    //private ArrayAdapter<String> adapter;
    MyAdapter adapter = null;
    MyAdapter1 adapter1 = null;
    private String[] name;
    private String[] master;
    private String[] photo;
    private String[] likeu;

    private String[] name1;
    private String[] master1;
    private String[] photo1;
    private String[] likeu1;

    //計算product 長度
    public int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listInput = (ListView)findViewById(R.id.list_sop);
        listInput1 = (ListView)findViewById(R.id.list_sop2);

        productsList = new ArrayList<HashMap<String, String>>();

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();	//取得Bundle
        // TAG_ACCOUNT = bundle.getString("TAG_ACCOUNT");	//輸出Bundle內容
        TAG_Key = bundle.getString("TAG_Key");

        new LoadAll().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        View v = (View) menu.findItem(R.id.action_search).getActionView();
        //文字編輯部分
        searchName = (EditText) v.findViewById(R.id.search);
        searchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                try {
                    searchObject = searchName.getText().toString(); // 取得輸入文字
                } catch (Exception e) {

                }

            }
        });

        //送出部分
        searchButton = (Button) v.findViewById(R.id.searchGo);
        searchButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                goSearch(); //要做甚麼
            }
        });

        return true;
    }
    private ListView.OnItemClickListener listener = new ListView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            Toast.makeText(getApplicationContext(), "你選擇的是" + name[position]+"\n必須登入後才能瀏覽內容", Toast.LENGTH_SHORT).show();

        }

    };
    private ListView.OnItemClickListener listener1 = new ListView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            Toast.makeText(getApplicationContext(), "你選擇的是" + name1[position]+"\n必須登入後才能瀏覽內容", Toast.LENGTH_SHORT).show();

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
        if (id == R.id.action_search) {
            openSearch();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    void goSearch() {
        if (searchObject == null) {
            //搜尋空值，不做事
        } else {

            Toast.makeText(this, "Searching", Toast.LENGTH_LONG).show();
            Log.d("CY", "search");
            TAG_Key=searchObject;
            Intent intent = new Intent(this, Home.class); //前進至xxxx頁面
            intent.putExtra("TAG_Key", TAG_Key); //傳值
            startActivity(intent); //啟動出發

        }
    }
    public void openSearch()
    {
        Toast.makeText(this, "準備搜尋", Toast.LENGTH_LONG).show();

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
            params.add(new BasicNameValuePair("Key", TAG_Key) );
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
                        String like = c.getString(TAG_LIKE);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_SOPNAME, sopname);
                        map.put(TAG_USERNAME, username);
                        map.put(TAG_PICTURE, picture);
                        map.put(TAG_LIKE,like);

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

            int k=0;
            if(products.length()%2==0){
                x=products.length()/2;
            }else{
                x=(products.length()+1)/2;
            }

            name = new String[x];
            master = new String[x];
            photo = new String[x];
            likeu = new String[x];
            name1 = new String[products.length()/2];
            master1 = new String[products.length()/2];
            photo1 = new String[products.length()/2];
            likeu1 = new String[products.length()/2];
            for (int i = 0; i < x; i++) {
                name[i]=productsList.get(i).get(TAG_SOPNAME);
                master[i]=productsList.get(i).get(TAG_USERNAME);
                photo[i]=productsList.get(i).get(TAG_PICTURE);
                likeu[i]=productsList.get(i).get(TAG_LIKE);
            }
            for (int i = products.length()-1; i >=x; i--) {
                name1[k]=productsList.get(i).get(TAG_SOPNAME);
                master1[k]=productsList.get(i).get(TAG_USERNAME);
                photo1[k]=productsList.get(i).get(TAG_PICTURE);
                likeu1[k]=productsList.get(i).get(TAG_LIKE);
                k++;
            }

            adapter = new MyAdapter(Home.this);
            adapter1= new MyAdapter1(Home.this);
            listInput.setAdapter(adapter);
            listInput1.setAdapter(adapter1);

            listInput.setOnItemClickListener(listener);
            listInput1.setOnItemClickListener(listener1);

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
            convertView = myInflater.inflate(R.layout.searchmyxml, null);

            TextView Name = (TextView) convertView.findViewById(R.id.name);
            TextView number = (TextView) convertView
                    .findViewById(R.id.txtengname);
            ImageView MysopLogo = (ImageView) convertView.findViewById(R.id.mysoplogo);
            TextView Like = (TextView) convertView.findViewById(R.id.likeu);

            new DownloadImageTask(MysopLogo)
                    .execute(photo[position]);

            Name.setText(name[position]);
            number.setText(master[position]);
            Like.setText(likeu[position]);
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
            return name1.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return name1[position];
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
            TextView Like1 = (TextView) convertView.findViewById(R.id.likeu);

            new DownloadImageTask(MysopLogo1)
                    .execute(photo1[position]);

            Name1.setText(name1[position]);
            number1.setText(master1[position]);
            Like1.setText(likeu1[position]);
            return convertView;
        }

    }

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
}
