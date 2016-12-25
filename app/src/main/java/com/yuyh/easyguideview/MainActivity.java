package com.yuyh.easyguideview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuyh.library.EasyGuide;
import com.yuyh.library.support.HShape;

/**
 * @author yuyh.
 * @date 2016/12/24.
 */
public class MainActivity extends AppCompatActivity {

    EasyGuide easyGuide;

    private Toolbar toolbar;

    private MenuItem menuItem;
    private TextView menuView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.common_toolbar);

        setSupportActionBar(toolbar);

    }

    public void show(View view) {
        int[] loc = new int[2];
        view.getLocationOnScreen(loc);

        View tipsView = createTipsView();

        if (easyGuide != null && easyGuide.isShowing())
            easyGuide.dismiss();

        easyGuide = new EasyGuide.Builder(MainActivity.this)
                .addHightArea(view, HShape.RECTANGLE)
                //.addIndicator(R.drawable.left_bottom, 100, 100)
                //.addMessage("哈哈", 14)
                //.setPositiveButton("朕知道了", Constants.INVILID_VALUE, 14)
                .addView(tipsView, 0, loc[1] + view.getHeight(), new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
                .build();

        easyGuide.show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menuItem = menu.findItem(R.id.menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                menuView = (TextView) findViewById(R.id.menu);
                show(menuView);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
