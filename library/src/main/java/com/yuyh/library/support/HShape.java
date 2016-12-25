package com.yuyh.library.support;

import android.support.annotation.IntDef;

/**
 * 高亮区域形状
 *
 * @author yuyh.
 * @date 2016/12/24.
 */
@IntDef({
        HShape.CIRCLE,
        HShape.RECTANGLE,
        HShape.OVAL
})
public @interface HShape {

    int CIRCLE = 0;

    int RECTANGLE = 1;

    int OVAL = 2;
}
