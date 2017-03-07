package com.kb.scrollzoomlistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ScrollZoomListView mListView;
    private ArrayAdapter<String> mStringArrayAdapter;
    private List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ScrollZoomListView) findViewById(R.id.main_listview);

        mStringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getData());

        mListView.setAdapter(mStringArrayAdapter);

        View header = LayoutInflater.from(this).inflate(R.layout.layout_listview_header, null);
        ImageView imageView = (ImageView) header.findViewById(R.id.header_image);
        mListView.setZoomImageView(imageView);
        mListView.addHeaderView(header);
    }

    private List<String> getData() {
        mData = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mData.add("测试数据" + i);
        }
        return mData;
    }
}
