package com.englishvocabularygame.evog;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created on 1.01.2017.
 */

public class CustomToast {

    private static final int ANIMATION_DURATION = 600;

    private int HIDE_DELAY = 5000;

    private View mContainer;

    private int gravity = Gravity.CENTER;

    private TextView mTextView;

    private Handler mHandler;

    private AlphaAnimation mFadeInAnimation;

    private AlphaAnimation mFadeOutAnimation;

    public CustomToast(Context context, int HIDE_DELAY, int gravity) {
        ViewGroup container = (ViewGroup) ((Activity) context)
                .findViewById(android.R.id.content);
        View v = ((Activity) context).getLayoutInflater().inflate(
                R.layout.custom_toast, container);
        this.HIDE_DELAY = HIDE_DELAY;
        this.gravity = gravity;
        init(v);
    }

    private void init(View v) {
        mContainer = v.findViewById(R.id.mbContainer);
        mContainer.setVisibility(View.GONE);
        mTextView = (TextView) v.findViewById(R.id.mbMessage);
        mFadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        mFadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
        mFadeOutAnimation.setDuration(ANIMATION_DURATION);
        mFadeOutAnimation
                .setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mContainer.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

        mHandler = new Handler();

    }


    public void show(String message, int color, int backgroundColor) {
        mContainer.setVisibility(View.VISIBLE);

        ((LinearLayout) mContainer).setGravity(gravity
                | Gravity.CENTER_VERTICAL);

        mTextView.setBackgroundColor(backgroundColor);
        mTextView.setTextColor(color);
        mTextView.setText(message);

        mFadeInAnimation.setDuration(ANIMATION_DURATION);

        mContainer.startAnimation(mFadeInAnimation);
        mHandler.postDelayed(mHideRunnable, HIDE_DELAY);
    }

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mContainer.startAnimation(mFadeOutAnimation);
        }
    };

}
