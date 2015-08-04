package com.example.kelly.mysop;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

public class Mysop extends Activity {

    ArrayList<HashMap<String, String>> productsList;
    //private static String url_all_products = "http://localhost:8080/kelly/test_getall.jsp";
    static String url_all_products = "http://140.115.80.237/front/test_getall.jsp";
    static final String TAG_SUCCESS = "success";
    static final String TAG_PRODUCTS = "products";
    static final String TAG_PID = "pid";
    static final String TAG_NAME = "name";
    ListView list;
    LazyAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysop);


        ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

        XMLParser parser = new XMLParser();
        String xml = parser.getXmlFromUrl(url_all_products); // 從網络獲取xml
        Document doc = parser.getDomElement(xml); // 獲取 DOM 節點

        NodeList nl = doc.getElementsByTagName(TAG_PRODUCTS);
        // 循環遍曆所有的歌節點 <song>
        for (int i = 0; i < nl.getLength(); i++) {
            // 新建一個 HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);
            //每個子節點添加到HashMap關鍵= >值
            map.put(TAG_PID, parser.getValue(e, TAG_PID));
            map.put(TAG_PRODUCTS, parser.getValue(e, TAG_PRODUCTS));
            map.put(TAG_NAME, parser.getValue(e, TAG_NAME));
           // map.put(KEY_DURATION, parser.getValue(e, KEY_DURATION));
           // map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));

            // HashList添加到數組列表
            songsList.add(map);
        }


        list=(ListView)findViewById(R.id.list);


        adapter=new LazyAdapter(this, songsList);
        list.setAdapter(adapter);


        //为單一列表行添加單擊事件

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //這裏可以自由發揮，比如播放一首歌曲等等
            }
<<<<<<< HEAD
        });
=======

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



            RelativeLayout w = (RelativeLayout)findViewById(R.id.relative);
            w.setBackgroundColor(Color.parseColor("#EEFFBB"));
            // 取得 LinearLayout 物件
            LinearLayout l = new LinearLayout(Mysop.this);
            l.setBackgroundResource(R.drawable.linearlayout);
            //l.setId(0);


            LinearLayout l2 = new LinearLayout(Mysop.this);
            l2.setBackgroundColor(Color.parseColor("#FF8888"));
            l2.setOrientation(LinearLayout.VERTICAL);
            //l2.setOnClickListener();
            TextView tv = new TextView(Mysop.this);
            tv.setText(productsList.get(0).get(TAG_PID));
            //tv.setText(productsList.get(0).get(TAG_PID));
            l2.addView(tv);
            TextView tv1 = new TextView(Mysop.this);
            tv1.setText(productsList.get(0).get(TAG_NAME));
            l2.addView( tv1 );

            l.addView( l2 );

            LinearLayout l3 = new LinearLayout(Mysop.this);
            l3.setBackgroundColor(Color.parseColor("#99BBFF"));
            l3.setOrientation(LinearLayout.VERTICAL);

            TextView tv3 = new TextView(Mysop.this);
            tv3.setText(productsList.get(0).get(TAG_PID));
            l3.addView( tv3 );
            l.addView( l3 );

            //從這裡哈哈哈哈哈哈哈
            w.addView(l);
/*
            LinearLayout l4 = new LinearLayout(Mysop.this);
            l4.setBackgroundResource(R.drawable.linearlayout);
            //l4.setLayoutParams(new RelativeLayout.LayoutParams(150, 150));
            RelativeLayout.LayoutParams r = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            //r.addRule(RelativeLayout.BELOW, R.id.  );
            r.addRule(RelativeLayout.ALIGN_PARENT_TOP);


            LinearLayout nl2 = new LinearLayout(Mysop.this);
            nl2.setBackgroundColor(Color.parseColor("#FF8888"));
            nl2.setOrientation(LinearLayout.VERTICAL);
            //l2.setOnClickListener();
            TextView ntv = new TextView(Mysop.this);
            ntv.setText(productsList.get(1).get(TAG_PID));
            nl2.addView(ntv);
            l4.addView(nl2);

            w.addView(l4,r);

*/


            AlertDialog.Builder dialog = new AlertDialog.Builder(Mysop.this);
            dialog.setTitle("");
            dialog.setMessage(productsList.get(0).get(TAG_PID));
            dialog.show();


        }

>>>>>>> origin/master
    }
}


