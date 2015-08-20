package com.example.kelly.mysop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;


public class StepCutControlIbeacon extends Activity implements BeaconConsumer {

    public static final String TAG = "BeaconsEverywhere";
    private BeaconManager beaconManager;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    //讀取 qrcode 圖片
    private static String url_uuid = "http://140.115.80.237/front/mysop_CCibeacon.jsp";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_UUID = "UUID";
    String UUID = "00000000-0000-0000-0000-000000000000";

    int connectfinish=0;

    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();//獲得當前的藍芽

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_cut_control_ibeacon);


        if (adapter == null){
            Toast.makeText(this, "藍芽藍芽?", Toast.LENGTH_LONG).show();
        }else if(adapter.isEnabled()!=true){//如果藍芽未開啟
            //打開藍芽(會問使用者)
            Intent enabler=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enabler);
            Toast.makeText(this, "藍芽藍芽?ohno", Toast.LENGTH_LONG).show();

            //則打開藍芽(不問使用者)
            //adapter.enable();

        }
        new Checkibeacon().execute();

        Log.d("isithere", Integer.toString(connectfinish));


        //beaconManager = BeaconManager.getInstanceForApplication(this);

        //beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        //beaconManager.bind(this);
    }


    public void refresh(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {

        if(connectfinish==0) {

        }
        Log.d("connectfinish?",Integer.toString(connectfinish));

        if(connectfinish == 0){}

        Region region = new Region("myBeaons", Identifier.parse(UUID), null, null);

        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                try {
                    Log.d(TAG, "didEnterRegion");
                    beaconManager.startRangingBeaconsInRegion(region);
                    Intent intent = new Intent();
                    intent.setClass(StepCutControlIbeacon.this, Stepnextcontrol.class);
                    startActivity(intent);
                    // 设置切换动画，从右边进入，左边退出
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    finish();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void didExitRegion(Region region) {
                try {
                    Log.d(TAG, "didExitRegion");
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {

            }
        });
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for(Beacon oneBeacon : beacons) {
                    Log.d(TAG, "distance: " + oneBeacon.getDistance() + " id:" + oneBeacon.getId1() + "/" + oneBeacon.getId2() + "/" + oneBeacon.getId3());
                }
            }
        });

        try {
            Log.d("uuid",region.getIdentifier(0).toString());
            beaconManager.startMonitoringBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_step_cut_control_ibeacon, menu);
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

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class Checkibeacon extends AsyncTask<String, String, Integer> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StepCutControlIbeacon.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected Integer doInBackground(String... args) {

            int returnvalue = 0;

            String StepNumber = "2";

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("StepNumber", StepNumber));

            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_uuid, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    UUID = json.getString(TAG_UUID);
                    returnvalue = 1;
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return returnvalue;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(Integer returnvalue) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            connectfinish=1;
            if (returnvalue == 1){
                beaconManager = BeaconManager.getInstanceForApplication(StepCutControlIbeacon.this);

                beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

                beaconManager.bind(StepCutControlIbeacon.this);
            }

        }
    }



}