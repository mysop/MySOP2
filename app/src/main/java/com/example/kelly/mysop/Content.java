package com.example.kelly.mysop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//鍵盤手請看237

public class Content extends Activity {

    private EditText inputText;
    private ListView listInput;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;

    private ImageView picture;
    private TextView title;
    private TextView master;
    private TextView download;
    private TextView star;
    private TextView cagetory;
    private TextView subtitle;
    private TextView Ctext;
    private TextView sopnumber;

    JSONParser jsonParser = new JSONParser();
    //讀取 sop內容
    private static String url_create_product = "http://140.115.80.237/front/mysop_content.jsp";
    //讀取 評論
    private static String url_create_product1 = "http://140.115.80.237/front/mysop_content1.jsp";
    //寫入評論
    private static String url_create_product2 = "http://140.115.80.237/front/mysop_content3.jsp";


    String TAG_ACCOUNT = "q@gmail.com";
    ArrayList<HashMap<String, String>> productsList;
    ArrayList<HashMap<String, String>> productsList1;
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "sop";
    private static final String TAG_PID = "account";
    private static final String TAG_NAME = "sop_comment";
    private static final String TAG_NUMBER = "sopnumber";
    private static final String TAG_DETAIL = "detail";
    private static final String TAG_SOPNAME = "sopname";
    private static final String TAG_INTRO = "intro";
    private static final String TAG_USERNAME="username";
    private static final String TAG_STARTRULE="start_rule";
    private static String NUMBER ="";
    private  static String DETAIL="";
    private static String SOPNAME="";
    private static String INTRO="";
    private static String USERNAME="";
    private static String STARTRULE="";

    JSONArray products = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        inputText = (EditText)findViewById(R.id.inputText);
        listInput = (ListView)findViewById(R.id.listInputText);
        items = new ArrayList<String>();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,items);
        listInput.setAdapter(adapter);

        picture=(ImageView)findViewById(R.id.content_picture);
        title=(TextView)findViewById(R.id.content_title);
        master=(TextView)findViewById(R.id.content_master);
        download=(TextView)findViewById(R.id.download);
        star=(TextView)findViewById(R.id.star);
        cagetory=(TextView)findViewById(R.id.category);
        subtitle=(TextView)findViewById(R.id.content_subtitle);
         Ctext=(TextView)findViewById(R.id.content_text);
        sopnumber=(TextView)findViewById(R.id.content_sopnumber);


        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();	//取得Bundle
       // TAG_ACCOUNT = bundle.getString("TAG_ACCOUNT");	//輸出Bundle內容


        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
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

    //新增評論
    public void writeCommon (View v){
        if(!inputText.getText().toString().equals("")){
            new SOPContent1().execute();


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


            params1.add(new BasicNameValuePair("Sopnumber", Sopnumber) );
            params.add(new BasicNameValuePair("Sopnumber", Sopnumber) );


            // json抓sop內容  json1抓評論
            JSONObject json = Content.this.jsonParser.makeHttpRequest(Content.url_create_product, "GET", params);
            JSONObject json1 = Content.this.jsonParser.makeHttpRequest(Content.url_create_product1, "GET", params1);

//            // Check your log cat for JSON reponse
//            Log.d("All Products: ", json1.toString());

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

                //讀取sop內容


                int e2 = json.getInt(TAG_SUCCESS);

                if(e2==1){
                    SOPNAME = json.getString(TAG_SOPNAME);
                    NUMBER  = json.getString(TAG_NUMBER);
                    DETAIL = json.getString(TAG_DETAIL);
                    INTRO  = json.getString(TAG_INTRO);
                    STARTRULE=json.getString(TAG_STARTRULE);
                }else{
                   // System.out.println("HAHA NO"+json.getString(TAG_SOPNAME));

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


            //動態listview 產生評論
            for (int i = 0; i < products.length(); i++) {
                items.add(productsList.get(i).get(TAG_PID)+"\n"+productsList.get(i).get(TAG_NAME));
                listInput.setAdapter(adapter);
                inputText.setText("");
            }

            //sop內容讀取
            title.setText(SOPNAME);
            subtitle.setText(INTRO);
            sopnumber.setText(NUMBER);
            Ctext.setText(DETAIL);

            System.out.println("HELLO "+STARTRULE);
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

}
