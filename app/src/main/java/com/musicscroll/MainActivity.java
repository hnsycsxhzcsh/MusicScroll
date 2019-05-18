package com.musicscroll;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.musicscrollview.MusicScrollView;
import com.musicscrollview.MusicScrollView2;

public class MainActivity extends AppCompatActivity {
    private MusicScrollView2 mMsg2;
    //    private MusicScrollView mMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mMsg = (MusicScrollView) findViewById(R.id.msg);
//        mMsg.initData();

        mMsg2 = (MusicScrollView2) findViewById(R.id.msv2);
        mMsg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMsg2.isStart()) {
                    mMsg2.stopAnim();
                } else {
                    mMsg2.startAnim();
                }
            }
        });
    }
}
