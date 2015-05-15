package net.yazeed44.resizableviewlibrary;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.almeros.android.multitouch.MoveGestureDetector;
import com.almeros.android.multitouch.RotateGestureDetector;

/**
 * Created by yazeed44 on 10/11/14.
 */
public class ResizableViewLayout extends ResizeFrameView {

    public static final String TAG = ResizableViewLayout.class.getSimpleName();

    private float mScaleXFactor = 1.0f;
    private float mScaleYFactor = 1.0f;
    private ScaleGestureDetector mScaleDetector;
    private MoveGestureDetector mMoveDetector;
    private RotateGestureDetector mRotateDetector;
    private float mFocusX = 0.f;
    private float mFocusY = 0.f;
    private float mAngle = 0;

    private float mOrgX = 1;
    private float mOrgY;

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
        mRotateDetector = new RotateGestureDetector(getContext(), new RotateViewListener());


    }

    private void onDraggingShape(final MotionEvent shapeMotionEvent, View shape) {
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

        final float newWidth = (getWidth() * mScaleXFactor) + (xy[0] - shapeEvent.getRawX());


        final float scaleX = (newWidth / (float) getWidth());


        mScaleXFactor = scaleX;


        setScaleX(mScaleXFactor);

    }

    private void drag2(final MotionEvent shapeEvent) {


        final int[] xy = new int[2];
        getLocationOnScreen(xy);


        final float newWidth = shapeEvent.getRawX() - xy[0];

        final float scaleX = newWidth / (float) getWidth();

        mScaleXFactor = scaleX;

        setScaleX(scaleX);
    }


    private void drag1(final MotionEvent shapeEvent) {
        //TODO Fix over sizing on the first touch

        final int[] xy = new int[2];
        getLocationOnScreen(xy);


        final float newHeight = (getHeight() * mScaleYFactor) + (xy[1] - shapeEvent.getRawY());

        final float scaleY = newHeight / (float) getHeight();

        mScaleYFactor = scaleY;

        setScaleY(mScaleYFactor);
    }



    private void drag3(final MotionEvent shapeEvent) {

        final int[] xy = new int[2];
        getLocationOnScreen(xy);

        final float newHeight = shapeEvent.getRawY() - xy[1];

        final float scaleY = newHeight / (float) getHeight();

        setScaleY(scaleY);


    }

    private void assignShapeId(final View view) {



        mShapeId = ((ResizeShapeView) view).getShapeId();



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


                        onDraggingShape(motionEvent, v);
                        /*final int[] xy = new int[2];
                        v.getLocationOnScreen(xy);
                        mOrgX = xy[0];
                        mOrgY = xy[1];*/
                        mOrgX = motionEvent.getRawX();

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

                mScaleDetector.onTouchEvent(event);
                // mRotateDetector.onTouchEvent(event);


                mMoveDetector.onTouchEvent(event);
                display();


                return true;
            }
        };
    }

    private void display() {


        setTranslationX(getTranslationX() + mFocusX);
        setTranslationY(getTranslationY() + mFocusY);


        //TODO Implement rotating view by two finger gesture


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
    public OnTouchListener createRotateListener() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        };
    }


    private class ScaleViewListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {

            setPivotX(detector.getFocusX());
            setPivotY(detector.getFocusY());

            return super.onScaleBegin(detector);
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {


            mScaleXFactor *= detector.getScaleFactor();
            mScaleYFactor *= detector.getScaleFactor();

            mScaleXFactor = Math.max(0.1f, Math.min(mScaleXFactor, 10f));
            mScaleYFactor = Math.max(0.1f, Math.min(mScaleYFactor, 10f));


            setScaleX(mScaleXFactor);
            setScaleY(mScaleYFactor);


            Log.d("ScaleListener", "Scale factor , x  " + mScaleXFactor + "  , y  " + mScaleYFactor);
            return true;
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


            mFocusX += delta.x;
            mFocusY += delta.y;

            Log.d(TAG, "New focus , x : " + mFocusX + "  ,  y : " + mFocusY);


            return true;
        }


    }

    private class RotateViewListener extends RotateGestureDetector.SimpleOnRotateGestureListener {

        @Override
        public boolean onRotate(RotateGestureDetector detector) {


            mAngle -= detector.getRotationDegreesDelta();
            setRotation(mAngle);


            Log.d(TAG, "Angle delta  " + detector.getRotationDegreesDelta());
            Log.d(TAG, "New Angle  " + mAngle);

            //applyRotation();

            return false;
        }


    }
}
