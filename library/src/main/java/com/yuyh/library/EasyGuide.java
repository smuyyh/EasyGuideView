package com.yuyh.library;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuyh.library.bean.Confirm;
import com.yuyh.library.bean.HighlightArea;
import com.yuyh.library.bean.Message;
import com.yuyh.library.bean.TipsView;
import com.yuyh.library.constant.Constants;
import com.yuyh.library.support.HShape;
import com.yuyh.library.support.OnStateChangedListener;
import com.yuyh.library.view.EasyGuideView;

import java.util.ArrayList;
import java.util.List;

import static android.view.MotionEvent.ACTION_UP;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 新手引导
 * <p>
 * https://github.com/smuyyh/EasyGuideView
 *
 * @author yuyh.
 * @date 2016/12/24.
 */
public class EasyGuide {

    private Activity mActivity;
    private FrameLayout mParentView;
    private EasyGuideView mGuideView;
    private LinearLayout mTipView;

    private List<HighlightArea> mAreas = new ArrayList<>();
    private List<TipsView> mIndicators = new ArrayList<>();
    private List<Message> mMessages = new ArrayList<>();
    private Confirm mConfirm;
    private boolean dismissAnyWhere;
    private boolean performViewClick;

    private OnStateChangedListener listener;

    public EasyGuide(Activity activity) {
        this(activity, null, null, null, null, true, false);
    }

    public EasyGuide(Activity activity, List<HighlightArea> areas, List<TipsView> indicators,
                     List<Message> messages, Confirm confirm, boolean dismissAnyWhere, boolean performViewClick) {
        this.mActivity = activity;
        this.mAreas = areas;
        this.mIndicators = indicators;
        this.mMessages = messages;
        this.mConfirm = confirm;
        this.dismissAnyWhere = dismissAnyWhere;
        this.performViewClick = performViewClick;

        mParentView = (FrameLayout) mActivity.getWindow().getDecorView();

    }

    /**
     * 设置引导提示 状态改变(显示/取消) 监听
     *
     * @param listener
     */
    public void setOnStateChangedListener(OnStateChangedListener listener) {
        this.listener = listener;
    }

    /**
     * 显示引导提示
     */
    public void show() {

        mGuideView = new EasyGuideView(mActivity);
        mGuideView.setHightLightAreas(mAreas);

        mTipView = new LinearLayout(mActivity);
        mTipView.setGravity(Gravity.CENTER_HORIZONTAL);
        mTipView.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        mTipView.setOrientation(LinearLayout.VERTICAL);

        if (mIndicators != null) {
            for (TipsView tipsView : mIndicators) {
                addView(tipsView.view, tipsView.offsetX, tipsView.offsetY, tipsView.params);
            }
        }

        if (mMessages != null) {
            int padding = dip2px(mActivity, 5);
            for (Message message : mMessages) {
                TextView tvMsg = new TextView(mActivity);
                tvMsg.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                tvMsg.setPadding(padding, padding, padding, padding);
                tvMsg.setGravity(Gravity.CENTER);
                tvMsg.setText(message.message);
                tvMsg.setTextColor(Color.WHITE);
                tvMsg.setTextSize(message.textSize == -1 ? 12 : message.textSize);

                mTipView.addView(tvMsg);
            }
        }

        if (mConfirm != null) {
            TextView tvConfirm = new TextView(mActivity);
            tvConfirm.setGravity(Gravity.CENTER);
            tvConfirm.setText(mConfirm.text);
            tvConfirm.setTextColor(Color.WHITE);
            tvConfirm.setTextSize(mConfirm.textSize == -1 ? 13 : mConfirm.textSize);
            tvConfirm.setBackgroundResource(R.drawable.btn_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            params.topMargin = dip2px(mActivity, 10);
            tvConfirm.setLayoutParams(params);
            int lr = dip2px(mActivity, 8);
            int tb = dip2px(mActivity, 5);
            tvConfirm.setPadding(lr, tb, lr, tb);
            tvConfirm.setOnClickListener(mConfirm.listener != null ?
                    mConfirm.listener : new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            mTipView.addView(tvConfirm);
        }

        addView(mTipView, Constants.CENTER, Constants.CENTER, new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        mParentView.addView(mGuideView, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        if (dismissAnyWhere || performViewClick) {
            mGuideView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case ACTION_UP:
                            if (mAreas.size() > 0) {

                                for (HighlightArea area : mAreas) {
                                    final View view = area.mHightlightView;

                                    // 如果点击事件作用在该View上
                                    if (view != null && inRangeOfView(view, event)) {

                                        dismiss();

                                        if (listener != null) {
                                            listener.onHeightlightViewClick(view);
                                        }

                                        if (performViewClick) {
                                            view.performClick();
                                        }
                                    } else if (dismissAnyWhere) {
                                        dismiss();
                                    }
                                }
                                return false;
                            } else {
                                dismiss();
                                return false;
                            }
                        default:
                            break;
                    }
                    return true;
                }
            });
        }

        if (listener != null) {
            listener.onShow();
        }
    }

    /**
     * 取消引导提示
     */
    public void dismiss() {
        mGuideView.recyclerBitmap();
        if (mParentView.indexOfChild(mGuideView) > 0) {
            mParentView.removeView(mGuideView);

            if (listener != null) {
                listener.onDismiss();
            }
        }
    }

    /**
     * 添加任意 View 到引导提示的布局上
     *
     * @param view
     * @param offsetX X轴偏移，正数表示从布局的左侧往右偏移量，负数表示从布局的右侧往左偏移量。{@link Constants#CENTER}表示居中
     * @param offsetY Y轴偏移，正数表示从上往下，负数表示从下往上。{@link Constants#CENTER}表示居中
     * @param params  参数
     */
    private void addView(View view, int offsetX, int offsetY, RelativeLayout.LayoutParams params) {
        if (params == null)
            params = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

        if (offsetX == Constants.CENTER) {
            params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        } else if (offsetX < 0) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            params.rightMargin = -offsetX;
        } else {
            params.leftMargin = offsetX;
        }

        if (offsetY == Constants.CENTER) {
            params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        } else if (offsetY < 0) {
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            params.bottomMargin = -offsetY;
        } else {
            params.topMargin = offsetY;
        }

        mGuideView.addView(view, params);
    }

    public boolean isShowing() {
        return mParentView.indexOfChild(mGuideView) > 0;
    }

    public boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }

    public static class Builder {

        Activity activity;

        List<HighlightArea> areas = new ArrayList<>();
        List<TipsView> views = new ArrayList<>();
        List<Message> messages = new ArrayList<>();

        Confirm confirm;

        boolean dismissAnyWhere = true;
        boolean performViewClick;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        /**
         * 添加高亮区域
         *
         * @param view
         * @param shape 高亮区域形状
         * @return
         */
        public Builder addHightArea(View view, @HShape int shape) {
            HighlightArea area = new HighlightArea(view, shape);
            areas.add(area);
            return this;
        }

        public Builder addHightLightArea(HighlightArea area) {
            areas.add(area);
            return this;
        }

        /**
         * 添加箭头指示的图片资源
         *
         * @param resId
         * @param offX  X轴偏移 正数表示从布局的左侧往右偏移量，负数表示从布局的右侧往左偏移量。{@link Constants#CENTER}表示居中
         * @param offY  Y轴偏移 正数表示从布局的上侧往下偏移量，负数表示从布局的下侧往上偏移量。{@link Constants#CENTER}表示居中
         * @return
         */
        public Builder addIndicator(int resId, int offX, int offY) {
            ImageView ivIndicator = new ImageView(activity);
            ivIndicator.setImageResource(resId);
            views.add(new TipsView(ivIndicator, offX, offY));
            return this;
        }

        public Builder addView(View view, int offX, int offY) {
            views.add(new TipsView(view, offX, offY));
            return this;
        }

        /**
         * 添加任意的View
         *
         * @param view
         * @param offX   X轴偏移 正数表示从布局的左侧往右偏移量，负数表示从布局的右侧往左偏移量。{@link Constants#CENTER}表示居中
         * @param offY   Y轴偏移 正数表示从布局的上侧往下偏移量，负数表示从布局的下侧往上偏移量。{@link Constants#CENTER}表示居中
         * @param params 参数
         * @return
         */
        public Builder addView(View view, int offX, int offY, RelativeLayout.LayoutParams params) {
            views.add(new TipsView(view, offX, offY, params));
            return this;
        }

        /**
         * 添加提示信息，默认居中显示
         *
         * @param message
         * @param textSize
         * @return
         */
        public Builder addMessage(String message, int textSize) {
            messages.add(new Message(message, textSize));
            return this;
        }

        /**
         * 添加确定按钮，默认居中显示在提示信息下方
         *
         * @param btnText
         * @param textSize
         * @return
         */
        public Builder setPositiveButton(String btnText, int textSize) {
            this.confirm = new Confirm(btnText, textSize);
            return this;
        }

        public Builder setPositiveButton(String btnText, int textSize, View.OnClickListener listener) {
            this.confirm = new Confirm(btnText, textSize, listener);
            return this;
        }

        /**
         * 是否点击任意区域消失。默认true
         *
         * @param dismissAnyWhere
         * @return
         */
        public Builder dismissAnyWhere(boolean dismissAnyWhere) {
            this.dismissAnyWhere = dismissAnyWhere;
            return this;
        }

        /**
         * 若点击作用在高亮区域，是否执行高亮区域的点击事件
         *
         * @param performViewClick
         * @return
         */
        public Builder performViewClick(boolean performViewClick) {
            this.performViewClick = performViewClick;
            return this;
        }

        public EasyGuide build() {
            EasyGuide guide = new EasyGuide(activity, areas, views, messages, confirm, dismissAnyWhere, performViewClick);
            return guide;
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
