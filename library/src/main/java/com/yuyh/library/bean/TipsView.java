package com.yuyh.library.bean;

import android.view.View;
import android.widget.RelativeLayout;

import com.yuyh.library.constant.Constants;

/**
 * @author yuyh.
 * @date 2016/12/24.
 */
public class TipsView {

    public View view;

    public int resId = -1;

    public int offsetX = Constants.CENTER;

    public int offsetY = Constants.CENTER;

    public RelativeLayout.LayoutParams params;

    public TipsView(int resId, int offsetX, int offsetY) {
        this.resId = resId;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public TipsView(View view, int offsetX, int offsetY) {
        this.view = view;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public TipsView(View view, int offsetX, int offsetY, RelativeLayout.LayoutParams params) {
        this.view = view;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.params = params;
    }
}
