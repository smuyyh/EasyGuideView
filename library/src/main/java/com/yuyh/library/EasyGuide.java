package com.yuyh.library;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuyh.library.bean.Confirm;
import com.yuyh.library.bean.HighlightArea;
import com.yuyh.library.bean.TipsView;
import com.yuyh.library.bean.Message;
import com.yuyh.library.constant.Constants;
import com.yuyh.library.support.HShape;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * @author yuyh.
 * @date 2016/12/24.
 */
public class EasyGuide {

    private Activity mActivity;
    private FrameLayout mParentView;
    private EasyGuideView mGuideView;
    private LinearLayout mTipView;

    List<HighlightArea> mAreas = new ArrayList<>();
    List<TipsView> mIndicators = new ArrayList<>();
    List<Message> mMessages = new ArrayList<>();
    Confirm mConfirm;

    public EasyGuide(Activity activity) {
        this(activity, null, null, null, null);
    }

    public EasyGuide(Activity activity, List<HighlightArea> areas, List<TipsView> indicators, List<Message> messages, Confirm confirm) {
        this.mActivity = activity;
        this.mAreas = areas;
        this.mIndicators = indicators;
        this.mMessages = messages;
        this.mConfirm = confirm;

        mParentView = (FrameLayout) mActivity.getWindow().getDecorView();

    }

    private void addView(View view, int offsetX, int offsetY) {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

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

    public void show() {

        mGuideView = new EasyGuideView(mActivity);
        mGuideView.setHightLightAreas(mAreas);

        mTipView = new LinearLayout(mActivity);
        mTipView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        mTipView.setOrientation(LinearLayout.VERTICAL);

        if (mIndicators != null) {
            for (TipsView image : mIndicators) {
                ImageView ivIndicator = new ImageView(mActivity);
                ivIndicator.setImageResource(image.resId);
                addView(ivIndicator, image.offsetX, image.offsetY);
            }
        }

        if (mMessages != null) {
            for (Message message : mMessages) {
                TextView tvMsg = new TextView(mActivity);
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
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            mTipView.addView(tvConfirm);
        }

        addView(mTipView, Constants.CENTER, Constants.CENTER);

        mParentView.addView(mGuideView, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }

    public void dismiss() {
        mGuideView.recyclerVitmap();
        if (mParentView.indexOfChild(mGuideView) > 0) {
            mParentView.removeView(mGuideView);
        }
    }

    public static class Builder {

        Activity activity;

        List<HighlightArea> areas = new ArrayList<>();
        List<TipsView> indicators = new ArrayList<>();
        List<Message> messages = new ArrayList<>();

        Confirm confirm;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder addHightArea(View view, @HShape int shape) {
            HighlightArea area = new HighlightArea(view, shape);
            areas.add(area);
            return this;
        }

        public Builder addHightLightArea(HighlightArea area) {
            areas.add(area);
            return this;
        }

        public Builder addIndicator(int resId, int offX, int offY) {
            ImageView ivIndicator = new ImageView(activity);
            ivIndicator.setImageResource(resId);
            indicators.add(new TipsView(ivIndicator, offX, offY));
            return this;
        }

        public Builder addView(View view, int offX, int offY) {
            indicators.add(new TipsView(view, offX, offY));
            return this;
        }

        public Builder addMessage(String message, int textSize) {
            messages.add(new Message(message, textSize));
            return this;
        }

        public Builder setPositiveButton(String btnText, int textSize) {
            this.confirm = new Confirm(btnText, textSize);
            return this;
        }

        public Builder setPositiveButton(String btnText, int offsetY, int textSize) {
            this.confirm = new Confirm(btnText, offsetY, textSize);
            return this;
        }

        public EasyGuide build() {
            EasyGuide guide = new EasyGuide(activity, areas, indicators, messages, confirm);
            return guide;
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
