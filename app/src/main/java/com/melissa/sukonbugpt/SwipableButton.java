package com.melissa.sukonbugpt;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;

public class SwipableButton extends AppCompatImageView {

    private GestureDetector gestureDetector;
    private OnSwipeListener onSwipeListener;

    public SwipableButton(Context context) {
        super(context);
        init(context);
    }

    public SwipableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        gestureDetector = new GestureDetector(context, new SwipeGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void setOnSwipeListener(OnSwipeListener listener) {
        this.onSwipeListener = listener;
    }

    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceY > 0) {
                    // 아래쪽 스와이프
                    if (onSwipeListener != null) {
                        onSwipeListener.onSwipeDown();
                    }
                } else {
                    // 위쪽 스와이프
                    if (onSwipeListener != null) {
                        onSwipeListener.onSwipeUp();
                    }
                }
                return true;
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (onSwipeListener != null) {
                onSwipeListener.onSingleTap();
            }
            return true;
        }
    }

    public interface OnSwipeListener {
        void onSwipeUp();
        void onSwipeDown();
        void onSingleTap();
    }
}