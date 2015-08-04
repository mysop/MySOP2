package com.example.kelly.mysop;

/**
 * Created by Chris on 2015/8/4.
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; //image downloader

    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // 標題
        TextView artist = (TextView)vi.findViewById(R.id.artist); // 歌手名
        TextView duration = (TextView)vi.findViewById(R.id.duration); // 時長
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // 縮略圖

        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);

        // value for list
        title.setText(song.get(Mysop.TAG_PRODUCTS));
        artist.setText(song.get(Mysop.TAG_PID));
        duration.setText(song.get(Mysop.TAG_NAME));
        //imageLoader.DisplayImage(song.get(Mysop.KEY_THUMB_URL), thumb_image);
        return vi;
    }
}
