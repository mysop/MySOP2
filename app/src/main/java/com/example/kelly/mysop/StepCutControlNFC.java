package com.example.kelly.mysop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;



public class StepCutControlNFC extends Activity {

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;


    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    //讀取 NFC
    private static String url_NFC = "http://140.115.80.237/front/mysop_CCnfc.jsp";
    private static final String TAG_SUCCESS = "success";

    String TAG_CASE_NUMBER = "";
    String TAG_STEP_NUMBER = "";
    int TAG_STEP_ORDER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_cut_control_nfc);

        TextView ss = (TextView)findViewById(R.id.CC_NFC_textView2);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();	//取得Bundle
        TAG_CASE_NUMBER = bundle.getString("TAG_CASE_NUMBER");
        TAG_STEP_NUMBER = bundle.getString("TAG_STEP_NUMBER");
        TAG_STEP_ORDER = bundle.getInt("TAG_STEP_ORDER");
        ss.setText(Integer.toString(TAG_STEP_ORDER));

        mTextView = (TextView)findViewById(R.id.CC_NFC_textView5);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter != null) {
            mTextView.setText("支持讀取NFC!");
        } else {
            mTextView.setText("不支持NFC!");
        }
        if (!mNfcAdapter.isEnabled()) {

            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setTitle("注意");
            alertbox.setMessage("請開啟NFC!");
            alertbox.setPositiveButton("開啟", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(intent);
                    }
                }
            });
            alertbox.setNegativeButton("關閉", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertbox.show();

        }



        mPendingIntent = PendingIntent.getActivity(this, 0,new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

        try {
            ndefIntent.addDataType("*/*");
            mIntentFilters = new IntentFilter[] { ndefIntent };
        }catch (Exception e) {
            Log.e("TagDispatch", e.toString());
        }
        mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };

    }


    @Override
    public void onNewIntent(Intent intent) {
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String s = action + "\n\n" + tag.toString();
        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        //NFC Tag中第一項Text要放UUID
        String[] NFC_UUID = new String[20];

        if (data != null) {
            try {
                for (int i = 0; i < data.length; i++) {
                    NdefRecord [] recs = ((NdefMessage)data[i]).getRecords();
                    for (int j = 0; j < recs.length; j++) {

                        if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {

                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";

                            int langCodeLen = payload[0] & 0077;
                            s += ("\n\nNdefMessage[" + i + "], NdefRecord[" + j + "]:\n\"" +
                                    new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1, textEncoding) + "\"");
                            NFC_UUID[j] = new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1, textEncoding);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TagDispatch", e.toString());
            }
        }
        //mTextView.setText(s);
        new Check_NFC().execute(NFC_UUID[0]);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    class Check_NFC extends AsyncTask<String, String, Integer> {


        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StepCutControlNFC.this);
            pDialog.setMessage("Matching... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Integer doInBackground(String... args) {

            int returnvalue = 0;

            //String Stepnumber="4";
            String Stepnumber = TAG_STEP_NUMBER;
            String NFC_UUID = args[0];

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("Stepnumber", Stepnumber));
            params.add(new BasicNameValuePair("NFC_UUID", NFC_UUID));
            JSONObject json = StepCutControlNFC.this.jsonParser.makeHttpRequest(StepCutControlNFC.url_NFC, "GET", params);


            try {
                int e = json.getInt(TAG_SUCCESS);
                if(e == 1) {
                    returnvalue = 1;
                }else if(e == 6){
                    returnvalue = 6;
                }
            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return returnvalue;
        }

        protected void onPostExecute(Integer returnvalue) {

            pDialog.dismiss();
            if (returnvalue == 1){
                Intent intent = new Intent();
                intent.setClass(StepCutControlNFC.this, StepNextControl.class);
                Bundle bundle = new Bundle();
                bundle.putString("TAG_CASE_NUMBER",TAG_CASE_NUMBER);
                bundle.putString("TAG_STEP_NUMBER", TAG_STEP_NUMBER);
                bundle.putInt("TAG_STEP_ORDER", TAG_STEP_ORDER);
                intent.putExtras(bundle);//將參數放入intent
                startActivity(intent);
                //切換畫面，右近左出
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }else if(returnvalue == 6){
                AlertDialog.Builder dialog = new AlertDialog.Builder(StepCutControlNFC.this);
                dialog.setTitle("");
                dialog.setMessage("比對結果錯誤，請使用正確的NFC Tag比對!");
                dialog.show();
            }

        }
    }
}
