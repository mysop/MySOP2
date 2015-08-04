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


        list = (ListView) findViewById(R.id.list);


        adapter = new LazyAdapter(this, songsList);
        list.setAdapter(adapter);


        //为單一列表行添加單擊事件

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //這裏可以自由發揮，比如播放一首歌曲等等
            }

        });

    }
}
        

