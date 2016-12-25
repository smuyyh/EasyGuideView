package com.yuyh.easyguideview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.yuyh.library.EasyGuide;
import com.yuyh.library.EasyGuideView;
import com.yuyh.library.bean.HighlightArea;
import com.yuyh.library.constant.Constants;
import com.yuyh.library.support.HShape;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuyh.
 * @date 2016/12/24.
 */
public class MainActivity extends AppCompatActivity {

    EasyGuideView guide;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        guide = (EasyGuideView) findViewById(R.id.guide);

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        new EasyGuide.Builder(this)
                .addHightArea(button, HShape.RECTANGLE)
                .addHightArea(button, HShape.RECTANGLE)
                .addMessage("哈哈", 14)
                .setPositiveButton("我知道了", Constants.INVILID_VALUE, 14)
                .build()
                .show();
    }
}
