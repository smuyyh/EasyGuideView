package com.yuyh.library.bean;

import com.yuyh.library.constant.Constants;

/**
 * @author yuyh.
 * @date 2016/12/24.
 */
public class Confirm {

    public String text;

    public int textSize = -1;

    public Confirm(String text) {
        this.text = text;
    }

    public Confirm(String text, int textSize) {
        this.text = text;
        this.textSize = textSize;
    }
}
