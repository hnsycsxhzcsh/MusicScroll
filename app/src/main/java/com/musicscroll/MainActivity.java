package com.musicscroll;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private MusicScrollView mMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMsg = (MusicScrollView) findViewById(R.id.msg);
//        mMsg.initData();

    }
}
