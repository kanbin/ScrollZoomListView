package com.kb.scrollzoomlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/3/6.
 */
public class ScrollZoomListView extends ListView {
    private static final String TAG = ScrollZoomListView.class.getSimpleName();

    private ImageView mImageView;
    private int mImageViewHeight; // 初始高度

    public ScrollZoomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mImageViewHeight = context.getResources().getDimensionPixelSize(R.dimen.size_default_height);
    }

    public void setZoomImageView(ImageView imageView){
        mImageView = imageView;
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        /**
         * 滑动过度两种情况：
         * deltaY: 增量
         * 下拉过度：deltaY是-
        * 上拉过度：deltaY是+
        */
        if (deltaY < 0){ // 下拉过度
            // ImageView 进行放大的效果
            mImageView.getLayoutParams().height = mImageView.getHeight() - deltaY;
            mImageView.requestLayout();
        }else { // 上拉过度
            // ImageView 进行缩小的效果
            mImageView.getLayoutParams().height = mImageView.getHeight() - deltaY;
            mImageView.requestLayout();

        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        View header = (View) mImageView.getParent();
        // ListView 会滑出去的高度（负数）
        int deltaY = header.getTop(); // ----

        // 当ImageView被放大过，才会执行缩小
        if (mImageView.getHeight() > mImageViewHeight){
            // ImageView 进行缩小的效果
            mImageView.getLayoutParams().height = mImageView.getHeight() + deltaY;
            // 由于滑出去了一截，所以要让header重新摆放，top为0
            header.layout(header.getLeft(), 0, header.getRight(), header.getHeight()); // ----
            mImageView.requestLayout();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP){
            // 开启动画
            ResetAnimation animation = new ResetAnimation(mImageViewHeight);
//            animation.setInterpolator(new AnticipateInterpolator());
//            animation.setInterpolator(new LinearInterpolator());
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.setDuration(300);
            mImageView.startAnimation(animation);
        }
        return super.onTouchEvent(ev);

    }

    public class ResetAnimation extends Animation{
        private int extraHeight;
        private int currentHeight;

        public ResetAnimation(int targetHeight) {
            currentHeight = mImageView.getHeight();
            extraHeight = mImageView.getHeight() - targetHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);

            /**
             * 0 ~ 1
             * height ~ 初始高度
             */

            // ImageView 进行缩小的效果
            mImageView.getLayoutParams().height = (int) (currentHeight - extraHeight * interpolatedTime);
            mImageView.requestLayout();

        }
    }
}
