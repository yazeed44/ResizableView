package net.yazeed44.resizableviewlibrary;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 10/11/14.
 */
public class ResizableViewLayout extends ResizeFrameView {

    public static final String TAG = ResizableViewLayout.class.getSimpleName();
    private final ArrayList<Integer> mThirtyMultiples = new ArrayList<>();

    private float mMaxScaleFactor = 3;
    private AspectRatio mChosenAspectRatio;
    private int xDelta;
    private int yDelta;
    private PointF mRotatePoint;
    private double mLastComAngle;
    private Point mViewCenter;
    private double mLastImgAngle;
    private LayoutParams mRotateLayoutParams;
    private float mLastX = -1;
    private float mLastY = -1;


    public ResizableViewLayout(Context context) {
        super(context);
    }

    public ResizableViewLayout(final Context context, AttributeSet set) {
        super(context, set);
    }

    public ResizableViewLayout(final Context context, AttributeSet set, int defStyle) {
        super(context, set, defStyle);
    }

    @Override
    public void setResizableView(View resizableView) {
        super.setResizableView(resizableView);
        init();
    }

    private void init() {

        //generateThirtyMultiples();

    }

    private void generateThirtyMultiples() {

        for (int i = -360; i >= -360 && i <= 360; i++) {
            if (i % 5 == 0) {
                mThirtyMultiples.add(i);
            }

        }
    }

    private void onDraggingShape(final MotionEvent shapeMotionEvent) {
        switch (mShapeId) {

            case 0:
                drag0(shapeMotionEvent);
                break;

            case 1:
                drag1(shapeMotionEvent);
                break;

            case 2:
                drag2(shapeMotionEvent);
                break;

            case 3:
                drag3(shapeMotionEvent);
                break;

            default:
                throw new IllegalStateException("Shape Id is  -1 !!");
        }


    }

    private void drag0(final MotionEvent shapeEvent) {
        final int[] xy = new int[2];
        getLocationOnScreen(xy);


        final float newWidth = (float) ((getWidthWithScale()) + (xy[0] - shapeEvent.getRawX()));

        resize(newWidth, getHeightWithScale());


    }

    private Point getAnglePoint(Point O, PointF A, float angle) {
        int x, y;
        float dOA = getDistance(O, A);
        double p1 = angle * Math.PI / 180f;
        double p2 = Math.acos((A.x - O.x) / dOA);
        x = (int) (O.x + dOA * Math.cos(p1 + p2));

        double p3 = Math.acos((A.x - O.x) / dOA);
        y = (int) (O.y + dOA * Math.sin(p1 + p3));
        return new Point(x, y);
    }


    private void drag2(final MotionEvent shapeEvent) {


        final int[] xy = new int[2];
        getLocationOnScreen(xy);


        final float newWidth = getWidthWithScale() + (shapeEvent.getRawX() - xy[0]);


        resize(newWidth, getHeightWithScale());
    }


    private void drag1(final MotionEvent shapeEvent) {

        final int[] xy = new int[2];
        getLocationOnScreen(xy);


        final float newHeight = (getHeightWithScale()) + (xy[1] - shapeEvent.getRawY());

        resize(getWidthWithScale(), newHeight);
    }



    private void drag3(final MotionEvent shapeEvent) {

        final int[] xy = new int[2];
        getLocationOnScreen(xy);

        final float newHeight = shapeEvent.getRawY() - xy[1];

        resize(getWidthWithScale(), newHeight);


    }

    private void assignShapeId(final View view) {
        mShapeId = ((ResizeShapeView) view).getShapeId();

    }

    private void resize(final float newWidth, final float newHeight) {

        final Point newDimension = mChosenAspectRatio.calculateDimension(getWidthWithScale(), getHeightWithScale(), Math.round(newWidth), Math.round(newHeight));
        Log.d(TAG, "New dimension    " + newDimension);
        final float width = newDimension.x;
        final float height = newDimension.y;

        mScaleXFactor = (width / (float) getWidth());
        mScaleYFactor = (height / (float) getHeight());

        setScaleX(mScaleXFactor);
        setScaleY(mScaleYFactor);

    }

    @Override
    public OnTouchListener createShapesListener() {

        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {

                //Touch listener for resize shapes
                final int eventAction = motionEvent.getAction();


                switch (eventAction) {

                    case MotionEvent.ACTION_DOWN: {
                        //First touch

                        break;
                    }


                    case MotionEvent.ACTION_MOVE: {
                        //Moving the finger on screen

                        assignShapeId(v);
                        onDraggingShape(motionEvent);
                        break;
                    }


                    case MotionEvent.ACTION_UP: {
                        //Finger off the screen
                        mShapeId = -1;


                        break;
                    }

                }

                return true;

            }
        };


    }

    @Override
    public OnTouchListener createResizableViewListener() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //TODO Add scale gesture
                //TODO Add rotate gesture

                final int x = Math.round(event.getRawX());
                final int y = Math.round(event.getRawY());
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) getLayoutParams();
                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = -250;
                        layoutParams.bottomMargin = -250;
                        setLayoutParams(layoutParams);
                        break;
                }

                return true;
            }
        };
    }




    @Override
    public void setScaleX(float scaleX) {

        if (scaleX < -mMaxScaleFactor) {
            mScaleXFactor = -mMaxScaleFactor;
            super.setScaleX(mScaleXFactor);
            Log.d(TAG, "New scale x   " + mScaleXFactor);
            return;
        } else if (mScaleXFactor > mMaxScaleFactor) {
            mScaleXFactor = mMaxScaleFactor;
            super.setScaleX(mScaleXFactor);
            Log.d(TAG, "New scale x   " + mScaleXFactor);
            return;
        }

        super.setScaleX(scaleX);
        Log.d(TAG, "New scale x   " + mScaleXFactor);
    }

    @Override
    public void setScaleY(float scaleY) {

        if (mScaleYFactor < -mMaxScaleFactor) {
            mScaleYFactor = -mMaxScaleFactor;
            super.setScaleY(mScaleYFactor);
            Log.d(TAG, "New scale y   " + mScaleYFactor);
            return;
        } else if (mScaleYFactor > mMaxScaleFactor) {
            mScaleYFactor = mMaxScaleFactor;
            super.setScaleY(mScaleYFactor);
            Log.d(TAG, "New scale y   " + mScaleYFactor);
            return;
        }

        super.setScaleY(scaleY);
        Log.d(TAG, "New scale y   " + mScaleYFactor);
    }

    @Override
    public void setRotation(float rotation) {
        super.setRotation(rotation);
        Log.d(TAG, "New mRotation  " + rotation);

    }

    private float roundUpToClosetThirtyMultiplier(final float rotation) {
        float lowestDiff = Float.MAX_VALUE;
        float result = 0;
        for (final int i : mThirtyMultiples) {
            float diff = Math.abs(rotation - i); // use API to get absolute diff
            if (diff < lowestDiff) {
                lowestDiff = diff;
                result = i;
            }
        }
        return result;

    }

    public void setMaxScaleFactor(final float scaleFactor) {
        mMaxScaleFactor = scaleFactor;
    }

    public int getWidthWithScale() {
        return Math.round(getWidth() * mScaleXFactor);
    }

    public int getHeightWithScale() {
        return Math.round(getHeight() * mScaleYFactor);
    }

    @Override
    public OnTouchListener createStretchListener() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    drag2(event);
                    drag3(event);
                }

                /*else if (event.getAction() == MotionEvent.ACTION_DOWN){
                    setRotation(getRotation() + 90);
                }*/
                return true;
            }
        };
    }

    @Override
    public OnTouchListener createRotateListener() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                switch (event.getAction() & event.getActionMasked()) {

                    case MotionEvent.ACTION_DOWN:

                        mRotateLayoutParams = (LayoutParams) mRotateView.getLayoutParams();
                        mRotatePoint = new PointF(event.getRawX(), event.getRawY());
                        mLastX = event.getRawX();
                        mLastY = event.getRawY();
                        mLastImgAngle = getRotation();
                        refreshImageCenter();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float rawX = event.getRawX();
                        float rawY = event.getRawY();
                        if (mLastX != -1) {
                            if (Math.abs(rawX - mLastX) < 5 && Math.abs(rawY - mLastY) < 5) {
                                return false;
                            }
                        }
                        mLastX = rawX;
                        mLastY = rawY;

                        Point O = mViewCenter;
                        PointF A = mRotatePoint, B = new PointF(event.getRawX(), event.getRawY());
                        float dOA = getDistance(O, A);
                        float dOB = getDistance(O, B);
                        float f = dOB / dOA;

                        float fz = (((A.x - O.x) * (B.x - O.x)) + ((A.y - O.y) * (B.y - O.y)));
                        float fm = dOA * dOB;
                        double comAngle = (180 * Math.acos(fz / fm) / Math.PI);
                        if (Double.isNaN(comAngle)) {
                            comAngle = (mLastComAngle < 90 || mLastComAngle > 270) ? 0 : 180;
                        } else if ((B.y - O.y) * (A.x - O.x) < (A.y - O.y) * (B.x - O.x)) {
                            comAngle = 360 - comAngle;
                        }
                        mLastComAngle = comAngle;

                        float angle = (float) (mLastImgAngle + comAngle);
                        angle = angle % 360;
                        mRotation = angle;
                        invalidate();


                        break;

                    case MotionEvent.ACTION_UP:

                        break;

                }

                return true;
            }
        };
    }


    private void refreshImageCenter() {

        int x = getLeft() + getWidth() / 2;
        int y = getTop() + getHeight() / 2;

        mViewCenter = new Point(x, y);
    }

    private float getDistance(Point a, PointF b) {
        float v = ((a.x - b.x) * (a.x - b.x)) + ((a.y - b.y) * (a.y - b.y));
        return (float) (((Math.sqrt(v))));
    }


    public void setAspectRatio(final AspectRatio newAspectRatio) {
        mChosenAspectRatio = newAspectRatio;

        if (getWidthWithScale() != 0 && getHeightWithScale() != 0)
            resize(getWidthWithScale() + 10, getHeightWithScale() + 10);
    }


}
