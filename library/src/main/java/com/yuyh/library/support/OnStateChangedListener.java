package com.yuyh.library.support;

import android.view.View;

/**
 * @author yuyh.
 * @date 2016/12/25.
 */
public interface OnStateChangedListener {

    void onShow();

    void onDismiss();

    void onHeightlightViewClick(View view);
}
