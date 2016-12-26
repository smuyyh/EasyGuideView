package com.yuyh.easyguideview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //btnShow(findViewById(R.id.menu_button));
    }

    /**
     * 显示自定义提示布局
     *
     * @param view
     */
    public void btnShow(View view) {
        int[] loc = new int[2];
        view.getLocationOnScreen(loc);

        View tipsView = createTipsView();

        if (easyGuide != null && easyGuide.isShowing())
            easyGuide.dismiss();

        easyGuide = new EasyGuide.Builder(MainActivity.this)
                // 增加View高亮区域，可同时显示多个
                .addHightArea(view, HShape.RECTANGLE)
                // 复杂的提示布局，建议通过此方法，较容易控制
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

    /**
     * 各个组件分别添加。此方式只能显示简单的提示信息与按钮，建议通过addView形式
     *
     * @param view
     */
    public void menuShow(View view) {
        int[] loc = new int[2];
        view.getLocationOnScreen(loc);

        View tipsView = createTipsView();

        if (easyGuide != null && easyGuide.isShowing())
            easyGuide.dismiss();

        easyGuide = new EasyGuide.Builder(MainActivity.this)
                .addHightArea(view, HShape.CIRCLE)
                .addIndicator(R.drawable.right_top, loc[0], loc[1] + view.getHeight())
                .addMessage("点击菜单显示", 14)
                .setPositiveButton("朕知道了~", 15, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        easyGuide.dismiss();
                        Log.i("TAG", "dismiss");
                    }
                })
                .dismissAnyWhere(true)
                .build();

        easyGuide.show();
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
                menuShow(menuView);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (easyGuide != null && easyGuide.isShowing())
            easyGuide.dismiss();
    }
}
