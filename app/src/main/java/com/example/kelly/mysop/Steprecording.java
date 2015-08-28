package com.example.kelly.mysop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.widget.Toast;

//p303
public class Steprecording extends Activity {


    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> productsList;
    JSONArray products = null;

    private static String url_all_products = "http://140.115.80.237/front/mysop_steprecording2.jsp";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_RECODE = "recode";


    private static String url_record = "http://140.115.80.237/front/mysop_steprecording3.jsp";

    private GestureDetector detector;
    EditText[] edit1 = new EditText[20];

    private Intent recognizerIntent = null;
    private GridView gridView;
    private List<String> messageList;
    private ArrayAdapter<String> adapter;
    private Context context;
    EditText edtext = null;

    private int count=0;
    //private String step="";
    String RecordText[] = new String[20];

    String TAG_CASE_NUMBER = "";
    String TAG_STEP_NUMBER = "";
    int TAG_STEP_ORDER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steprecording);

        TextView ss = (TextView)findViewById(R.id.textView6);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();	//取得Bundle
        TAG_CASE_NUMBER = bundle.getString("TAG_CASE_NUMBER");
        TAG_STEP_NUMBER = bundle.getString("TAG_STEP_NUMBER");
        TAG_STEP_ORDER = bundle.getInt("TAG_STEP_ORDER");
        ss.setText(Integer.toString(TAG_STEP_ORDER));

        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
        // Loading products in Background Thread
        new LoadInput().execute();
        detector = new GestureDetector(new MySimpleOnGestureListener());

                ScrollView sv = (ScrollView)findViewById(R.id.scrollView);
        sv.setOnTouchListener(new MyOnTouchListener());
        //LinearLayout llb = (LinearLayout)findViewById(R.id.linearLayoutbackground);
        //llb.setOnTouchListener(new MyOnTouchListener());

        messageList = new ArrayList<>();
    }


    public void speak_onclick(View view) {
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說...");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        try {
            startActivityForResult(recognizerIntent, 1);
        }catch(ActivityNotFoundException a){
            Toast.makeText(getApplicationContext(),"抱歉您的系統不支持語音識別輸入。",Toast.LENGTH_LONG).show();
        }
    }

    int voice=0;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent it) {
        super.onActivityResult(requestCode, resultCode, it);
        messageList.clear();
        if (requestCode != 1) {
            return;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        List<String> list = it.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        for (String s : list) {
            messageList.add(s);
        }
        Log.d("howmany",String.valueOf(count));
        if(edtext != null){
            edtext.setText(messageList.get(0));
        }else {
            if (voice < count) {
                edit1[voice].setText(messageList.get(0));
                voice++;
                Log.d("vvv", String.valueOf(voice));
            } else {
                voice = count - 1;
                edit1[voice].setText(messageList.get(0));
                Log.d("nnn", String.valueOf(voice));
            }
        }
    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_steprecording, menu);
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
    }*/

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadInput extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Steprecording.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {

            String Stepnumber = TAG_STEP_NUMBER;
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Stepnumber", Stepnumber));
            // getting JSON string from URL
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
                        int r = i+1;
                        String id = c.getString(TAG_RECODE+r);


                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_RECODE+r, id);

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
            // updating UI from Background Thread
/*            runOnUiThread(new Runnable() {
                public void run() {

                }
            }); */

            LinearLayout ly = (LinearLayout)findViewById(R.id.linearlayoutinput);

            count=products.length();

            for(int i=0; i<products.length();i++) {

                int r = i+1;
                TextView text1 = new TextView(Steprecording.this);
                text1.setText(productsList.get(i).get(TAG_RECODE+r));


                edit1[i] = new EditText(Steprecording.this.getApplicationContext());
                //edit1[i].setId(400+i);

                edit1[i].setTextColor(Color.rgb(0, 0, 0));
                edit1[i].setOnFocusChangeListener(new MyOnFocusChangeListener());
                edit1[i].setSingleLine(true);
                edit1[i].setBackgroundColor(Color.parseColor("#FEFBE6"));
                //edit1[0].setText("eee");
                ly.addView(text1);
                ly.addView(edit1[i]);
            }

            //if(edit1.getTag().equals(1)){edit1.setText("qqq");}

            //ly.setOnTouchListener(new MyOnTouchListener());


        }

    }


    class MyOnFocusChangeListener implements View.OnFocusChangeListener{
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                Log.i("test", "获得焦点");
                Log.i("test", getCurrentFocus().toString());
                edtext = (EditText)getCurrentFocus();
            } else {
                Log.i("test", "失去焦点");
            }
        }
    }

    //抓滑動
    class MyOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return detector.onTouchEvent(event);
        }
    }
    class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // TODO Auto-generated method stub
            if ((e1.getX() - e2.getX()) > 50) {//说明是左滑
                Log.d("ohitwork",edit1[1].getText().toString());

                boolean didinput = true;

                for(int j=0;j<count;j++){
                    if(edit1[j].getText().toString().equals("")){
                        didinput = false;
                    }
                }

                if(didinput) {
                    //new Recording().execute();
                    Recording[] RC = new Recording[20];
                    for (int postcount = 0; postcount < count; postcount++) {
                        RC[postcount] = new Recording();
                        RC[postcount].execute(postcount);
                    }


                    Intent intent = new Intent();
                    intent.setClass(Steprecording.this, StepcutcontrolArtificial.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("TAG_CASE_NUMBER",TAG_CASE_NUMBER);
                    bundle.putString("TAG_STEP_NUMBER", TAG_STEP_NUMBER);
                    bundle.putInt("TAG_STEP_ORDER", TAG_STEP_ORDER);
                    intent.putExtras(bundle);//將參數放入intent
                    startActivity(intent);
                    //切換畫面，右近左出
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Steprecording.this);
                    dialog.setTitle("");
                    dialog.setMessage("請紀錄完成再前往下一頁!");
                    dialog.show();
                }
                return true;
            } else
                return false;
        }

    }

    //回傳
    class Recording extends AsyncTask<Integer, String, String> {
        Recording() {}

        protected void onPreExecute() {
            super.onPreExecute();
            Steprecording.this.pDialog = new ProgressDialog(Steprecording.this);
            Steprecording.this.pDialog.setMessage("Recording...");
            Steprecording.this.pDialog.setIndeterminate(false);
            Steprecording.this.pDialog.setCancelable(true);
            //Steprecording.this.pDialog.show();
        }

        protected String doInBackground(Integer... args) {

            //EditText et1 = (EditText)edit1.findViewById(20);
            //String Account = Steprecording.this.et1.getText().toString();

            int a = args[0];
            RecordText[a] = Steprecording.this.edit1[a].getText().toString();
            ArrayList params = new ArrayList();

            //和使用者有關的-->按號
            params.add(new BasicNameValuePair("CaseNumber", TAG_CASE_NUMBER));
            //同一step的每一次/個record
            params.add(new BasicNameValuePair("StepOrder", String.valueOf(TAG_STEP_ORDER)));
            params.add(new BasicNameValuePair("RecordOrder", String.valueOf(a+1)));
            Log.d("RecordOrder", String.valueOf(a+1));
            params.add(new BasicNameValuePair("RecordText", RecordText[a]));

            JSONObject json = Steprecording.this.jParser.makeHttpRequest(Steprecording.url_record, "POST", params);
            Log.d("Create Response", json.toString());

            try {
                int e = json.getInt(TAG_SUCCESS);
                if (e == 1) {
                    Log.d("YES","SSS");
                } else if (e == 2) {
                    Log.d("WRONG","SSS");
                }
            } catch (JSONException var9) {
                var9.printStackTrace();

            }
            //Steprecording.this.pDialog.dismiss();
        return null;
        }

        protected void onPostExecute(String file_url) {
            //Steprecording.this.pDialog.dismiss();
        }
    }




}
