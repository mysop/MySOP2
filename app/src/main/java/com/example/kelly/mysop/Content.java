package com.example.kelly.mysop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

//134行 帳號暫時註解
//帳號暫時寫死  sopnumber也是

public class Content extends Activity {

    private EditText inputText;
    private ListView listInput;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;

    private TextView title;
    private TextView master;
    private TextView download;
    private TextView star;
    private TextView cagetory;
    private TextView subtitle;
    private TextView Ctext;
    private ImageView graph1,graph2,graph3;
    private HorizontalScrollView horizontalScrollView;

    JSONParser jsonParser = new JSONParser();
    //讀取 sop內容 圖片
    private static String url_create_product = "http://140.115.80.237/front/mysop_content.jsp";
    //讀取 評論
    private static String url_create_product1 = "http://140.115.80.237/front/mysop_content1.jsp";
    //寫入評論
    private static String url_create_product2 = "http://140.115.80.237/front/mysop_content3.jsp";
    //數like數
    private static String url_create_product4 = "http://140.115.80.237/front/mysop_content5.jsp";
    //加入清單
    private static String url_create_product5 = "http://140.115.80.237/front/mysop_content6.jsp";


    TextView sopnumber;
    String TAG_ACCOUNT = "q@gmail.com";
    ArrayList<HashMap<String, String>> productsList;
    ArrayList<HashMap<String, String>> likeproductsList;
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "sop";
    private static final String TAG_LIKEPRODUCTS = "likenumber";
    private static final String TAG_PID = "account";
    private static final String TAG_NAME = "sop_comment";
    private static final String TAG_NUMBER = "sopnumber";
    private static final String TAG_DETAIL = "detail";
    private static final String TAG_SOPNAME = "sopname";
    private static final String TAG_INTRO = "intro";
    private static final String TAG_USERNAME="username";
    private static final String TAG_SOPGRAPH="sopgraph";
    private static final String TAG_GRAPH1="graph1";
    private static final String TAG_GRAPH2="graph2";
    private static final String TAG_GRAPH3="graph3";
    private static final String TAG_STARTRULE="start_rule";


    private static String NUMBER ="";
    private  static String DETAIL="";
    private static String SOPNAME="";
    private static String INTRO="";
    private static String USERNAME="";
    private  static String ACCOUNT="";

    private static String STARTRULE="";
    private static String SOPGRAPH="";
    private static String GRAPH1="";
    private static String GRAPH2="";
    private static String GRAPH3="";

    private int likecount=0;


    JSONArray products = null;
    JSONArray likeproducts = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        inputText = (EditText)findViewById(R.id.inputText);
        listInput = (ListView)findViewById(R.id.listInputText);
        items = new ArrayList<String>();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,items);
        listInput.setAdapter(adapter);

        title=(TextView)findViewById(R.id.content_title);
        master=(TextView)findViewById(R.id.content_master);
        download=(TextView)findViewById(R.id.download);
        star=(TextView)findViewById(R.id.star);
        cagetory=(TextView)findViewById(R.id.category);
        subtitle=(TextView)findViewById(R.id.content_subtitle);
         Ctext=(TextView)findViewById(R.id.content_text);
        sopnumber=(TextView)findViewById(R.id.content_sopnumber);

        graph1=(ImageView)findViewById(R.id.graph1);
        graph2=(ImageView)findViewById(R.id.graph2);
        graph3=(ImageView)findViewById(R.id.graph3);

        horizontalScrollView=( HorizontalScrollView)findViewById(R.id.horizontalScrollView);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();	//取得Bundle
       // TAG_ACCOUNT = bundle.getString("TAG_ACCOUNT");	//輸出Bundle內容


        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
        likeproductsList = new ArrayList<HashMap<String, String>>();
        // Loading products in Background Thread
         new SOPContent().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //新增評論
    public void writeCommon (View v){
        if(!inputText.getText().toString().equals("")){
            new SOPContent1().execute();
        }

    }

    //加入清單 和到Mysop頁面
    public void addtolist (View v){
        new SOPContent2().execute();
        Intent it = new Intent(this,Mysop.class);
        startActivity(it);
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


    class SOPContent extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Content.this);
            pDialog.setMessage("Loading Content. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            String Sopnumber = Content.this.sopnumber.getText().toString();


            ArrayList params = new ArrayList();
            ArrayList params1 = new ArrayList();
            ArrayList params3 = new ArrayList();

            params1.add(new BasicNameValuePair("Sopnumber", Sopnumber) );
            params.add(new BasicNameValuePair("Sopnumber", Sopnumber) );
            params3.add(new BasicNameValuePair("Sopnumber", Sopnumber) );

            // json抓sop內容  json1抓評論  json3抓like數
            JSONObject json = Content.this.jsonParser.makeHttpRequest(Content.url_create_product, "GET", params);
            JSONObject json1 = Content.this.jsonParser.makeHttpRequest(Content.url_create_product1, "GET", params1);
            JSONObject json3 = Content.this.jsonParser.makeHttpRequest(Content.url_create_product4, "GET", params3);


            try {

                //讀取評論
                int e = json1.getInt(TAG_SUCCESS);
                if(e == 1) {

                    products = json1.getJSONArray(TAG_PRODUCTS);

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);

                        // adding HashList to ArrayList
                        productsList.add(map);
                    }
                }else if(e == 2) {
                }
                //算like
                int e1 = json3.getInt(TAG_SUCCESS);

                if(e1 == 1) {
                    likeproducts = json3.getJSONArray(TAG_LIKEPRODUCTS);
                    for(int i=0;i<likeproducts.length();i++){
                        likecount++;
                    }
                }else{

                }

                //讀取sop內容 sop圖片
                int e2 = json.getInt(TAG_SUCCESS);

                if(e2==1){
                    SOPGRAPH = json.getString(TAG_SOPGRAPH);
                    SOPNAME = json.getString(TAG_SOPNAME);
                    NUMBER  = json.getString(TAG_NUMBER);
                    DETAIL = json.getString(TAG_DETAIL);
                    INTRO  = json.getString(TAG_INTRO);
                    ACCOUNT= json.getString(TAG_PID);
                    STARTRULE=json.getString(TAG_STARTRULE);
                    //判斷有沒有介紹的圖片
                    if(json.getString(TAG_GRAPH1).equals(null)){
                        GRAPH1="none";
                    }else{
                    GRAPH1 = json.getString(TAG_GRAPH1);
                    }
                    if(json.getString(TAG_GRAPH2).equals(null)){
                        GRAPH2="none";
                    }else{
                        GRAPH2 = json.getString(TAG_GRAPH2);
                    }
                    if(json.getString(TAG_GRAPH3).equals(null)){
                        GRAPH3="none";
                    }else{
                        GRAPH3 = json.getString(TAG_GRAPH3);
                    }

                }else{
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

            //自動新增評論
            for (int i = 0; i < products.length(); i++) {
                items.add(productsList.get(i).get(TAG_PID)+"\n"+productsList.get(i).get(TAG_NAME));
                listInput.setAdapter(adapter);
                inputText.setText("");
            }
            //放入sop內容
            title.setText(SOPNAME);
            subtitle.setText(INTRO);
            sopnumber.setText(NUMBER);
            Ctext.setText(DETAIL);
            master.setText(ACCOUNT);

            //放入sop圖片們
            new DownloadImageTask((ImageView)findViewById(R.id.content_picture))
                    .execute(SOPGRAPH);
            if(GRAPH1.equals("none")){
                graph1.setVisibility(8);
            }else{
            new DownloadImageTask((ImageView)findViewById(R.id.graph1))
                    .execute(GRAPH1);
                horizontalScrollView.setVisibility(0);
                horizontalScrollView.getLayoutParams().height=200;
                graph1.getLayoutParams().width=200;
                graph1.getLayoutParams().height=200;
            }
            if(GRAPH2.equals("none")){
                graph2.setVisibility(8);
            }else{
                new DownloadImageTask((ImageView)findViewById(R.id.graph2))
                        .execute(GRAPH2);
                horizontalScrollView.setVisibility(0);
                horizontalScrollView.getLayoutParams().height=200;
                graph1.getLayoutParams().width=200;
                graph1.getLayoutParams().height=200;
            }
            if(GRAPH3.equals("none")){
                graph3.setVisibility(8);
            }else{
                new DownloadImageTask((ImageView)findViewById(R.id.graph3))
                        .execute(GRAPH3);
                horizontalScrollView.setVisibility(0);
                horizontalScrollView.getLayoutParams().height=200;
                graph1.getLayoutParams().width=200;
                graph1.getLayoutParams().height=200;
            }


            //放入收藏數
             download.setText(String.valueOf(likecount));

            //放入啟動規則
            switch (STARTRULE){
                case "1":
                    cagetory.setText("人工啟動");
                    cagetory.setTextSize(20);
                    break;
                case "2":
                    cagetory.setText("前一步驟\n完工");
                    cagetory.setTextSize(16);
                    break;
                case "3":
                    cagetory.setText("Beacon");
                    cagetory.setTextSize(20);
                    break;
                case "4":
                    cagetory.setText("QR code");
                    cagetory.setTextSize(20);
                    break;
                case "5":
                    cagetory.setText("NFC");
                    cagetory.setTextSize(25);
                    break;
                case "6":
                    cagetory.setText("定位");
                    cagetory.setTextSize(25);
                    break;
                case "7":
                    cagetory.setText("時間到期");
                    cagetory.setTextSize(16);
                    break;
            }
        }
    }

    //寫入評論
    class SOPContent1 extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Content.this);
            pDialog.setMessage("Loading Content. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            String Inputtext = Content.this.inputText.getText().toString();
            String Sopnumber = Content.this.sopnumber.getText().toString();

            //for get
            ArrayList params2 = new ArrayList();

            params2.add(new BasicNameValuePair("Inputtext", Inputtext) );
            params2.add(new BasicNameValuePair("Account", TAG_ACCOUNT));
            params2.add(new BasicNameValuePair("Sopnumber", Sopnumber) );

            JSONObject json2 = Content.this.jsonParser.makeHttpRequest(Content.url_create_product2,"POST",params2);


            try {
               //寫入評論
                int e3 = json2.getInt(TAG_SUCCESS);
                if(e3 == 1) {
                    USERNAME = json2.getString(TAG_USERNAME);
                }

            } catch (JSONException var9) {
                var9.printStackTrace();
            }
            pDialog.dismiss();

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();

            items.add(USERNAME+"\n"+inputText.getText().toString());
            listInput.setAdapter(adapter);
            inputText.setText("");

        }

    }

    //加入清單
    class SOPContent2 extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Content.this);
            pDialog.setMessage("Adding to List. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {


            String Sopnumber = Content.this.sopnumber.getText().toString();

            //for get
            ArrayList params2 = new ArrayList();

            params2.add(new BasicNameValuePair("Account", TAG_ACCOUNT));
            params2.add(new BasicNameValuePair("Sopnumber", Sopnumber) );

            JSONObject json2 = Content.this.jsonParser.makeHttpRequest(Content.url_create_product5,"POST",params2);

            try {
                //加入清單
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
