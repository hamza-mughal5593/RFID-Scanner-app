package com.example.module_android_bluedemo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> list3 = new ArrayList<>();


    // Constructor
    public ImageAdapter(Context c, ArrayList<String> list) {
        mContext = c;
        list3 = list;
    }

    public int getCount() {
        return list3.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView imageView;

        if (convertView == null) {
            imageView = new TextView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setPadding(8, 8, 8, 8);
        }
        else
        {
            imageView = (TextView) convertView;
        }
        imageView.setText(list3.get(position));
        return imageView;
    }


}
