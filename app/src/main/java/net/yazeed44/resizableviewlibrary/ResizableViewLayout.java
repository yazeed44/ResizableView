package net.yazeed44.resizableviewlibrary;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by yazeed44 on 10/11/14.
 */
public class ResizableViewLayout extends ResizeFrameView {

    public static final String TAG = ResizableViewLayout.class.getSimpleName();
    public static final float DEFAULT_MAX_SCALE_FACTOR = 1000;//Like no limit

    private float mMaxScaleFactor = DEFAULT_MAX_SCALE_FACTOR;
    private AspectRatio mCurrentAspectRatio;
    private int xDelta;
    private int yDelta;
    private PointF mRotatePoint;
    private double mLastComAngle;
    private Point mViewCenter;
    private double mLastImgAngle;
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
                        onDraggingResizeShape(motionEvent);
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
                        //Drag the layout in the screen
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
            //If the current scale factor is smaller than the negative max , equalize current with negative max
            mScaleXFactor = -mMaxScaleFactor;
            super.setScaleX(mScaleXFactor);
            Log.d(TAG, "New scale x   " + mScaleXFactor);
            return;
        } else if (mScaleXFactor > mMaxScaleFactor) {
            //If the current scale factor is bigger than the max , equalize current with max
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
            //If the current scale factor is smaller than the negative max , equalize current with negative max
            mScaleYFactor = -mMaxScaleFactor;
            super.setScaleY(mScaleYFactor);
            Log.d(TAG, "New scale y   " + mScaleYFactor);
            return;
        } else if (mScaleYFactor > mMaxScaleFactor) {
            //If the current scale factor is bigger than the max , equalize current with max
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

    @Override
    public OnTouchListener createStretchListener() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    dragResizeShapeNumber2(event);
                    dragResizeShapeNumber3(event);
                }


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

                        mRotatePoint = new PointF(event.getRawX(), event.getRawY());
                        mLastX = event.getRawX();
                        mLastY = event.getRawY();
                        mLastImgAngle = getRotation();
                        initializeViewCenter();
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

                        //This code is taken from ryanch741

                        Point viewCenter = mViewCenter;
                        PointF rotateViewPoint = mRotatePoint, touchPoint = new PointF(event.getRawX(), event.getRawY());
                        float distanceBetweenViewCenterAndRotatePoint = ResizeUtil.getDistance(viewCenter, rotateViewPoint);
                        float distanceBetweenCenterPointAndTouchPoint = ResizeUtil.getDistance(viewCenter, touchPoint);

                        float fz = (((rotateViewPoint.x - viewCenter.x) * (touchPoint.x - viewCenter.x)) + ((rotateViewPoint.y - viewCenter.y) * (touchPoint.y - viewCenter.y)));
                        float fm = distanceBetweenViewCenterAndRotatePoint * distanceBetweenCenterPointAndTouchPoint;
                        double comAngle = (180 * Math.acos(fz / fm) / Math.PI);
                        if (Double.isNaN(comAngle)) {
                            comAngle = (mLastComAngle < 90 || mLastComAngle > 270) ? 0 : 180;
                        } else if ((touchPoint.y - viewCenter.y) * (rotateViewPoint.x - viewCenter.x) < (rotateViewPoint.y - viewCenter.y) * (touchPoint.x - viewCenter.x)) {
                            comAngle = 360 - comAngle;
                        }
                        mLastComAngle = comAngle;

                        float angle = (float) (mLastImgAngle + comAngle);
                        angle = angle % 360;
                        setRotation(angle);


                        break;

                    case MotionEvent.ACTION_UP:

                        break;

                }

                return true;
            }
        };
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

    public void setAspectRatio(final AspectRatio newAspectRatio) {
        mCurrentAspectRatio = newAspectRatio;

        if (getWidthWithScale() != 0 && getHeightWithScale() != 0)
            scaleLayout(getWidthWithScale() + 10, getHeightWithScale() + 10);
    }

    private void onDraggingResizeShape(final MotionEvent shapeMotionEvent) {
        switch (mShapeId) {

            case 0:
                dragResizeShapeNumber0(shapeMotionEvent);
                break;

            case 1:
                dragResizeShapeNumber1(shapeMotionEvent);
                break;

            case 2:
                dragResizeShapeNumber2(shapeMotionEvent);
                break;

            case 3:
                dragResizeShapeNumber3(shapeMotionEvent);
                break;

            default:
                throw new IllegalStateException("Shape Id is  -1 !!");
        }


    }

    //The shape in left | center of the view
    private void dragResizeShapeNumber0(final MotionEvent shapeEvent) {
        final int[] xy = new int[2];
        mResizeShapes.get(0).getLocationOnScreen(xy);


        final float newWidth = (float) ((getWidthWithScale()) + (xy[0] - shapeEvent.getRawX()));

        scaleLayout(newWidth, getHeightWithScale());


    }


    //The shape in right|center of the view
    private void dragResizeShapeNumber2(final MotionEvent shapeEvent) {

        final int[] xy = new int[2];
        mResizeShapes.get(2).getLocationOnScreen(xy);

        final float newWidth = (getWidthWithScale() + (shapeEvent.getRawX()) - xy[0]);
        scaleLayout(newWidth, getHeightWithScale());

    }


    //The shape in top | center of the view
    private void dragResizeShapeNumber1(final MotionEvent shapeEvent) {

        final int[] xy = new int[2];
        mResizeShapes.get(1).getLocationOnScreen(xy);


        final float newHeight = (getHeightWithScale()) + (xy[1] - shapeEvent.getRawY());

        scaleLayout(getWidthWithScale(), newHeight);
    }


    //The shape in bottom | center of the view
    private void dragResizeShapeNumber3(final MotionEvent shapeEvent) {

        final int[] xy = new int[2];
        mResizeShapes.get(3).getLocationOnScreen(xy);

        final float newHeight = getHeightWithScale() + (shapeEvent.getRawY() - xy[1]);

        scaleLayout(getWidthWithScale(), newHeight);


    }

    private void assignShapeId(final View view) {
        mShapeId = ((ResizeShapeView) view).getShapeId();

    }

    private void scaleLayout(final float newWidth, final float newHeight) {

        final Point newDimension = mCurrentAspectRatio.calculateDimension(getWidthWithScale(), getHeightWithScale(), Math.round(newWidth), Math.round(newHeight));
        Log.d(TAG, "New dimension    " + newDimension);
        final float width = newDimension.x;
        final float height = newDimension.y;

        mScaleXFactor = (width / (float) getWidth());
        mScaleYFactor = (height / (float) getHeight());

        setScaleX(mScaleXFactor);
        setScaleY(mScaleYFactor);

    }

    private void initializeViewCenter() {

        int x = getLeft() + getWidth() / 2;
        int y = getTop() + getHeight() / 2;

        mViewCenter = new Point(x, y);
    }


}
