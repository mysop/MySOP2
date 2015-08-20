package com.example.kelly.mysop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class StepActionControlGPS extends Activity {
    private LocationManager mLocationManager;               //宣告定位管理控制
    private ArrayList<Poi> Pois = new ArrayList<Poi>();   //建立List，屬性為Poi物件
    public TextView gps;

    private ProgressDialog pDialog;
    private ProgressDialog pDialog1;
    JSONParser jsonParser = new JSONParser();
    //讀取 gps
    private static String url_create_product = "http://140.115.80.237/front/mysop_ACgps.jsp";
    private static final String TAG_SUCCESS = "success";

    private static final String TAG_Latitude = "Latitude";
    private static final String TAG_Longitude = "Longitude";

    private static double DLatitude=0 ;
    private  static double DLongitude=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_action_control_gps);

        //建立物件，並放入List裡 (建立物件需帶入名稱、緯度、經度)
//        Pois.add(new Poi(25.04661 , 121.5168 ));
//        Pois.add(new Poi(24.13682 , 120.6850 ));
//        Pois.add(new Poi( 25.03362 , 121.56500 ));
//        Pois.add(new Poi( 22.61177 , 120.30031 ));
//        Pois.add(new Poi(25.10988 , 121.84519 ));

        gps=(TextView)findViewById(R.id.gps);

        //取得定位權限
        mLocationManager = (LocationManager) getSystemService(StepActionControlGPS.LOCATION_SERVICE);
       //連線取目的地
        new CheckGPS().execute();

        //取得在Layout建立的Button元件

        Button btn = (Button) findViewById(R.id.btn);


        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //把資料庫要把對的經緯度放進去
                Pois.add(new Poi(DLatitude ,DLongitude ));
                System.out.println("Now "+DLongitude+" AND "+DLatitude);
                //按下按鈕後讀取我的位置，定位抓取方式為網路讀取
                //(若欲以GPS為定位抓取方式則更改成LocationManager.GPS_PROVIDER)
                // 最後則帶入定位更新Listener。
                mLocationManager.requestLocationUpdates
                        (LocationManager.NETWORK_PROVIDER,0,10000.0f,LocationChange);
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(StepActionControlGPS.this);
                dialog1.setMessage("GPS...Pleas... wait");
                dialog1.show();

            }
        });

    }

    //更新定位Listener
    public LocationListener LocationChange = new LocationListener()
    {
        public void onLocationChanged(Location mLocation)
        {
            for(Poi mPoi : Pois)
            {
                //for迴圈將距離帶入，判斷距離為Distance function
                //需帶入使用者取得定位後的緯度、經度、景點店家緯度、經度。
                mPoi.setDistance(Distance(mLocation.getLatitude(),
                        mLocation.getLongitude(),
                        mPoi.getLatitude(),
                        mPoi.getLongitude()));
            }


            //印出我的座標-經度緯度

            Log.d("TAG", "我的座標 - 經度 : " + mLocation.getLongitude() + "  , 緯度 : " + mLocation.getLatitude());




            //for迴圈，印出景點店家名稱及距離，並依照距離由近至遠排列
            //第一筆為最近的景點店家，最後一筆為最遠的景點店家
            for(int i = 0 ; i < 1 ; i++ )
            {

                Log.d("TAG", " , 距離為 : " + DistanceText(Pois.get(i).getDistance()) );
                if(Pois.get(i).getDistance()>100){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(StepActionControlGPS.this);
                    dialog.setTitle("");
                    dialog.setMessage("還未到目的地，距離還有"+DistanceText(Pois.get(i).getDistance()));
                    dialog.show();

                }else{
                    Intent it = new Intent(StepActionControlGPS.this,Stepdescription.class);
                    startActivity(it);

                }
            }


        }
        public void onProviderDisabled(String provider)
        {
        }

        public void onProviderEnabled(String provider)
        {
        }

        public void onStatusChanged(String provider, int status,Bundle extras)
        {
        }
    };

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mLocationManager.removeUpdates(LocationChange);  //程式結束時停止定位更新
    }

    //帶入距離回傳字串 (距離小於一公里以公尺呈現，距離大於一公里以公里呈現並取小數點兩位)
    private String DistanceText(double distance)
    {
        if(distance < 1000 ) return String.valueOf((int)distance) + "m" ;
        else return new DecimalFormat("#.00").format(distance/1000) + "km" ;
    }



    //帶入使用者及景點店家經緯度可計算出距離
    public double Distance(double longitude1, double latitude1, double longitude2,double latitude2)
    {
        double radLatitude1 = latitude1 * Math.PI / 180;
        double radLatitude2 = latitude2 * Math.PI / 180;
        double l = radLatitude1 - radLatitude2;
        double p = longitude1 * Math.PI / 180 - longitude2 * Math.PI / 180;
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(l / 2), 2)
                + Math.cos(radLatitude1) * Math.cos(radLatitude2)
                * Math.pow(Math.sin(p / 2), 2)));
        distance = distance * 6378137.0;
        distance = Math.round(distance * 10000) / 10000;

        return distance ;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_step_action_control_g, menu);
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

    //物件類別class，名稱為Poi
    public class Poi
    {

        private double Latitude;    //景點店家緯度
        private double Longitude;   //景點店家經度
        private double Distance;    //景點店家距離

        //建立物件時需帶入景點店家名稱、景點店家緯度、景點店家經度
        public Poi(double latitude , double longitude)
        {
            //將資訊帶入類別屬性

            Latitude = latitude ;
            Longitude = longitude ;
        }

        //取得店家緯度
        public double getLatitude()
        {
            return Latitude;
        }

        //取得店家經度
        public double getLongitude()
        {
            return Longitude;
        }

        //寫入店家距離
        public void setDistance(double distance)
        {
            Distance = distance;
        }

        //取的店家距離
        public double getDistance()
        {

            return Distance;

        }
    }

    class CheckGPS extends AsyncTask<String, String, Integer> {


        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StepActionControlGPS.this);
            pDialog.setMessage("Loading.... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Integer doInBackground(String... args) {
            //寫死 Stepnumber
            String Stepnumber="3";
            int valoreOnPostExecute = 0;

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("Stepnumber", Stepnumber));

            JSONObject json = StepActionControlGPS.this.jsonParser.makeHttpRequest(StepActionControlGPS.url_create_product, "GET", params);


            try {
                int e = json.getInt(TAG_SUCCESS);
                if(e == 1) {
                    DLongitude = Double.parseDouble(json.getString(TAG_Longitude));
                    DLatitude=Double.parseDouble(json.getString(TAG_Latitude));
                    System.out.println("Here "+DLongitude+" AND "+DLatitude);


                }else if(e == 6){

                   // valoreOnPostExecute=1;

                }
            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return valoreOnPostExecute;
        }

        protected void onPostExecute(Integer valoreOnPostExecute) {

            pDialog.dismiss();
//            if(valoreOnPostExecute==1){
//                AlertDialog.Builder dialog = new AlertDialog.Builder(StepActionControlQRcode.this);
//                dialog.setTitle("");
//                dialog.setMessage("目標錯誤，請尋找正確QR code");
//                dialog.show();
//
//            }
        }
    }




}
