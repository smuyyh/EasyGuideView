package com.yuyh.easyguideview;

import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yuyh.library.EasyGuide;
import com.yuyh.library.support.HShape;

/**
 * @author yuyh.
 * @date 2016/12/24.
 */
public class MainActivity extends AppCompatActivity {

    private Button button;

    EasyGuide easyGuide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);

        button.postDelayed(new Runnable() {
            @Override
            public void run() {

                View tipsView = createTipsView();

                int[] loc = new int[2];
                button.getLocationOnScreen(loc);


                easyGuide = new EasyGuide.Builder(MainActivity.this)
                        .addHightArea(button, HShape.RECTANGLE)
                        //.addIndicator(R.drawable.left_bottom, 100, 100)
                        //.addMessage("哈哈", 14)
                        //.setPositiveButton("我知道了", Constants.INVILID_VALUE, 14)
                        .addView(tipsView, 0, loc[1] + button.getHeight(), new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
                        .build();

                easyGuide.show();
            }
        }, 500);

    }

    private View createTipsView() {

        View view = LayoutInflater.from(this).inflate(R.layout.tips_view, null);

        ImageView ivIsee = (ImageView) view.findViewById(R.id.ivIsee);
        ivIsee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (easyGuide != null) {
                    easyGuide.dismiss();
                }
            }
        });

        return view;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}
