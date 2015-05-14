package com.almeros.android.multitouch;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by rharter on 12/11/14.
 */
public class LongPressGestureDetector {

    private float downX, downY;
    private boolean pressing;
    private OnLongPressGestureListener listener;
    private int delay;
    private int slop;
    private Handler handler = new Handler();
    private Runnable callback = new Runnable() {
        @Override
        public void run() {
            if (pressing) {
                listener.onLongPressBegin(LongPressGestureDetector.this);
            }
        }
    };

    public LongPressGestureDetector(Context context, OnLongPressGestureListener listener) {
        this.listener = listener;

        ViewConfiguration vc = ViewConfiguration.get(context);
        delay = ViewConfiguration.getLongPressTimeout();
        slop = vc.getScaledTouchSlop() * 2;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                pressing = true;
                downX = event.getRawX();
                downY = event.getRawY();
                handler.postDelayed(callback, delay);
                return true;
            case MotionEvent.ACTION_MOVE:
                float deltaX = Math.abs(event.getRawX() - downX);
                float deltaY = Math.abs(event.getRawY() - downY);
                if (deltaX < slop && deltaY < slop) {
                    break;
                }
            default:
                if (pressing) {
                    listener.onLongPressEnd(this);
                }
                pressing = false;
                handler.removeCallbacks(callback);
        }
        return true;
    }

    public interface OnLongPressGestureListener {
        public void onLongPressBegin(LongPressGestureDetector detector);

        public void onLongPressEnd(LongPressGestureDetector detector);
    }
}