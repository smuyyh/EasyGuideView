package com.yuyh.library.bean;

import com.yuyh.library.constant.Constants;

/**
 * @author yuyh.
 * @date 2016/12/24.
 */
public class Message {

    public String message;

    public int offsetX = Constants.CENTER;

    public int offsetY = Constants.CENTER;

    public int textSize = -1;

    public Message(String message) {
        this.message = message;
    }

    public Message(String message, int offsetX, int offsetY) {
        this.message = message;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public Message(String message, int textSize) {
        this.message = message;
        this.textSize = textSize;
    }

    public Message(String message, int offsetX, int offsetY, int textSize) {
        this.message = message;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.textSize = textSize;
    }
}
