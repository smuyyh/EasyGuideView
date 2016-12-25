package com.yuyh.library.bean;

import com.yuyh.library.constant.Constants;

/**
 * @author yuyh.
 * @date 2016/12/24.
 */
public class Confirm {

    public String text;

    public int offsetY = -1;

    public int textSize = -1;

    public Confirm(String text) {
        this.text = text;
    }

    public Confirm(String text, int offsetY) {
        this.text = text;
        this.offsetY = offsetY;
    }

    public Confirm(String text, int offsetY, int textSize) {
        this.text = text;
        this.offsetY = offsetY;
        this.textSize = textSize;
    }
}
