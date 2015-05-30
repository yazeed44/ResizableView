package net.yazeed44.resizableviewlibrary;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;

import com.almeros.android.multitouch.MoveGestureDetector;

/**
 * Created by yazeed44 on 10/11/14.
 */
public class ResizableViewLayout extends ResizeFrameView {

    public static final String TAG = ResizableViewLayout.class.getSimpleName();

    private float mScaleXFactor = 1.0f;
    private float mScaleYFactor = 1.0f;
    private ScaleGestureDetector mScaleDetector;
    private MoveGestureDetector mMoveDetector;
    private float mFocusX = 0.f;
    private float mFocusY = 0.f;
    private float mAngle = 0;
    private float mMaxScaleFactor = 3;
    private AspectRatio mChosenAspectRatio;
    private int xDelta;
    private int yDelta;


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


        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleViewListener());
        mMoveDetector = new MoveGestureDetector(getContext(), new MoveViewListener());


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

        float newWidth = (getWidth() * mScaleXFactor) + (xy[0] - shapeEvent.getRawX());

        resize(newWidth, getHeightWithScale());


    }


    private void drag2(final MotionEvent shapeEvent) {


        final int[] xy = new int[2];
        getLocationOnScreen(xy);


        final float newWidth = shapeEvent.getRawX() - xy[0];


        resize(newWidth, getHeightWithScale());
    }


    private void drag1(final MotionEvent shapeEvent) {

        final int[] xy = new int[2];
        getLocationOnScreen(xy);


        final float newHeight = (getHeight() * mScaleYFactor) + (xy[1] - shapeEvent.getRawY());

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
        final int width = newDimension.x;
        final int height = newDimension.y;

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
                        assignShapeId(v);
                        break;
                    }


                    case MotionEvent.ACTION_MOVE: {
                        //Moving the finger on screen


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


                // mScaleDetector.onTouchEvent(event);
                // mRotateDetector.onTouchEvent(event);


                // mScaleDetector.onTouchEvent(event);
                //mMoveDetector.onTouchEvent(event);

                // display();

                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) getLayoutParams();
                        xDelta = X - lParams.leftMargin;
                        yDelta = Y - lParams.topMargin;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
                        layoutParams.leftMargin = X - xDelta;
                        layoutParams.topMargin = Y - yDelta;
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
                return true;
            }
        };
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public OnTouchListener createRotateListener() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        };
    }

    public void setAspectRatio(final AspectRatio newAspectRatio) {
        mChosenAspectRatio = newAspectRatio;

        if (getWidthWithScale() != 0 && getHeightWithScale() != 0)
            resize(getWidthWithScale(), getHeightWithScale());
    }


    private class ScaleViewListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
       /* @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {

            setPivotX(detector.getFocusX());
            setPivotY(detector.getFocusY());

            return super.onScaleBegin(detector);
        }*/

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            //TODO Improve scale gesture (Makes it more smooth)

            mScaleXFactor *= detector.getScaleFactor();
            mScaleYFactor *= detector.getScaleFactor();

            resize(getWidth() * mScaleXFactor, getHeight() * mScaleYFactor);
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);

        }
    }

    private class MoveViewListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
        @Override
        public boolean onMove(MoveGestureDetector detector) {

            final PointF delta = detector.getFocusDelta();

            if (mFocusX == 0.0 && mFocusY == 0.0) {
                //This is supposed to be the first move event
                //In the first move event the value of delta will be so big since there's no past events , so we will skip the first event
                mFocusX = 0.1f;
                mFocusY = 0.1f;
                return true;
            }

            if (mScaleXFactor < 0.0) {
                mFocusX -= delta.x;
            } else {
                mFocusX += delta.x;
            }

            if (mScaleYFactor < 0.0) {
                mFocusY -= delta.y;
            } else {
                mFocusY += delta.y;
            }



            Log.d(TAG, "New focus , x : " + mFocusX + "  ,  y : " + mFocusY);

            setX(getX() + mFocusX);
            setY(getY() + mFocusY);

            return true;
        }


    }


}
