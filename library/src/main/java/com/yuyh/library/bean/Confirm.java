package com.yuyh.library.bean;

import android.view.View;

/**
 * @author yuyh.
 * @date 2016/12/24.
 */
public class Confirm {

    public String text;

    public int textSize = -1;

    public View.OnClickListener listener;

    public Confirm(String text) {
        this.text = text;
    }

    public Confirm(String text, int textSize) {
        this.text = text;
        this.textSize = textSize;
    }

    public Confirm(String text, int textSize, View.OnClickListener listener) {
        this.text = text;
        this.textSize = textSize;
        this.listener = listener;
    }
}
