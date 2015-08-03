package com.example.kelly.mysop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    JSONParser jsonParser = new JSONParser();
    private static String url_create_product = "http://140.115.80.237/front/mysop_register.jsp";
    private static final String TAG_SUCCESS = "success";
    String TAG_ACCOUNT = "";


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

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();	//取得Bundle
        TAG_ACCOUNT = bundle.getString("TAG_ACCOUNT");	//輸出Bundle內容

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
            items.add(inputText.getText().toString());
            listInput.setAdapter(adapter);
            inputText.setText("");


        }
    }


    class CreateAccount extends AsyncTask<String, String, String> {
        CreateAccount() {}

//        protected void onPreExecute() {
//            super.onPreExecute();
//            Register.this.pDialog = new ProgressDialog(Register.this);
//            Register.this.pDialog.setMessage("Creating Account...");
//            Register.this.pDialog.setIndeterminate(false);
//            Register.this.pDialog.setCancelable(true);
//            Register.this.pDialog.show();
//        }

        protected String doInBackground(String... args) {
//            String Account = Register.this.et1.getText().toString();
//            String Password = Register.this.et2.getText().toString();
//            //String ConfirmPassword = Register.this.et3.getText().toString();
//            String Name = Register.this.et4.getText().toString();

            Drawable Picture = Content.this.picture.getDrawable();
            String Title = Content.this.title.toString();
            String Master = Content.this.master.toString();
            String Download = Content.this.download.toString();
            String Star = Content.this.star.toString();
            String Cagetory = Content.this.cagetory.toString();
            String Subtitle = Content.this.subtitle.toString();
            String CText = Content.this.Ctext.toString();
            String Inputtext = Content.this.inputText.toString();


            ArrayList params = new ArrayList();
          //  params.add(new BasicNameValuePair("Picture", Picture));
            params.add(new BasicNameValuePair("Title", Title));
            params.add(new BasicNameValuePair("Master", Master));
            params.add(new BasicNameValuePair("Download", Download));
            params.add(new BasicNameValuePair("Star", Star));
            params.add(new BasicNameValuePair("Cagetory", Cagetory));
            params.add(new BasicNameValuePair("Subtitle", Subtitle));
            params.add(new BasicNameValuePair("CText", CText));
            params.add(new BasicNameValuePair("Inputtext", Inputtext) );
            params.add(new BasicNameValuePair("Account", TAG_ACCOUNT));

            JSONObject json = Content.this.jsonParser.makeHttpRequest(Content.url_create_product, "POST", params);
            Log.d("Create Response", json.toString());

            try {
                int e = json.getInt(TAG_SUCCESS);
                if(e == 1) {

                }else if(e == 2){


                }
            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return null;
        }

//        protected void onPostExecute(String file_url) {
//            Register.this.pDialog.dismiss();
//        }
    }
}
