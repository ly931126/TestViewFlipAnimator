package com.android.tv.settings.testviewflipanimator;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.layout_front)
    FrameLayout mLayoutFront;
    @BindView(R.id.layout_back)
    FrameLayout mLayoutBack;
    @BindView(R.id.activity_main)
    FrameLayout mActivityMain;

    // 右出动画
    private AnimatorSet mRightOutSet = null;
    // 左入动画
    private AnimatorSet mLeftInSet = null;
    private boolean mIsShowBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 设置动画
        setAnimator();
        // 设置镜头距离
        setCameraDistance();
    }

    /**
     * 改变视角距离，贴近屏幕
     */
    private void setCameraDistance() {
        int distance = 16000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mLayoutFront.setCameraDistance(scale);
        mLayoutBack.setCameraDistance(scale);
    }

    /**
     * 设置动画 初始化右出(RightOut)和左入(LeftIn)动画, 使用动画集合AnimatorSet. 当右出动画开始时, 点击事件无效,
     * 当左入动画结束时, 点击事件恢复.
     */
    private void setAnimator() {
        mRightOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.anim_out);
        mLeftInSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.my_anim_in);
        mRightOutSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mActivityMain.setClickable(false);
            }
        });
        mLeftInSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mActivityMain.setClickable(true);
            }
        });
    }

    /**
     * 翻转卡片
     */
    @OnClick(R.id.activity_main)
    public void onClick() {
        //如果正面朝上
        if (!mIsShowBack) {
            mRightOutSet.setTarget(mLayoutFront);
            mLeftInSet.setTarget(mLayoutBack);
            mRightOutSet.start();
            mLeftInSet.start();
            mIsShowBack = true;
        } else {
            mRightOutSet.setTarget(mLayoutBack);
            mLeftInSet.setTarget(mLayoutFront);
            mRightOutSet.start();
            mLeftInSet.start();
            mIsShowBack = false;
        }

    }
}
