package net.yazeed44.resizableviewlibrary;

import android.content.Context;
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


    private static final float ROTATE_RADIO = 1.5f;
    private float mScaleFactor = 0.4F;
    private ScaleGestureDetector mScaleDetector;
    private MoveGestureDetector mMoveDetector;
    // private RotationGestureDetector mRotateDetector;
    private float mFocusX = 0.f;
    private float mFocusY = 0.f;
    private float mAngle = 0;

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
        // mRotateDetector = new RotationGestureDetector(new RotateListener());
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



        final int newMarginRight = getRight();
        final int newMarginLeft = (int) (getLeft() + (shapeEvent.getRawX() - getLeft()));

        final int newWidth = newMarginRight - newMarginLeft;

        new Resizer(this)
                .rightMargin(newMarginRight)
                .leftMargin(newMarginLeft)
                .width(newWidth)
                .resize();

    }

    private void drag2(final MotionEvent shapeEvent) {

        final int newRightMargin = (int) (getRight() + (shapeEvent.getRawX() - getRight()));

        final int newLeftMargin = (int) (getLeft() + (0));
        final int newWidth = newRightMargin - newLeftMargin;




        new Resizer(this)
                .leftMargin(newLeftMargin)
                .rightMargin(newRightMargin)
                .width(newWidth)
                .resize();

    }


    private void drag1(final MotionEvent shapeEvent) {
        //TODO Fix over sizing on the first touch

        final int newTopMargin = (int) (getTop() + (shapeEvent.getRawY() - getTop()));
        final int newBottomMargin = getBottom();
        final int newHeight = newBottomMargin - newTopMargin;

        new Resizer(this)
                .topMargin(newTopMargin)
                .bottomMargin(newBottomMargin)
                .height(newHeight)
                .resize();
    }



    private void drag3(final MotionEvent shapeEvent) {
        //TODO Fix over sizing on the first touch

        final int newTopMargin = getTop();
        final int newBottomMargin = (int) (getBottom() + (shapeEvent.getRawY() - getBottom()));
        final int newHeight = newBottomMargin - newTopMargin;

        new Resizer(this)
                .topMargin(newTopMargin)
                .bottomMargin(newBottomMargin)
                .height(newHeight)
                .resize();


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

                mScaleDetector.onTouchEvent(event);
                //mRotateDetector.onTouchEvent(event);
                mMoveDetector.onTouchEvent(event);
                //   mShoveDetector.onTouchEvent(event);
                display();

                return true;
            }
        };
    }

    private void display() {

        final int width = getWidth();
        final int height = getHeight();

        setX(mFocusX - width / 2);
        setY(mFocusY - height / 2);
        setRotation(mAngle);

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

    private void scaleLayout() {

        final float newMarginLeft = getLeft() * mScaleFactor;
        final float newMarginRight = getRight() * mScaleFactor;
        final float newWidth = newMarginRight - newMarginLeft;

        final float newMarginTop = getTop() * mScaleFactor;
        final float newMarginBottom = getBottom() * mScaleFactor;
        final float newHeight = newMarginBottom - newMarginTop;


        new Resizer(this)
                .leftMargin((int) newMarginLeft)
                .rightMargin((int) newMarginRight)

                .width((int) newWidth)
                .topMargin((int) newMarginTop)
                .bottomMargin((int) newMarginBottom)
                .height((int) newHeight)
                .resize();

    }


    private void dragLayout(final int newX, final int newY) {
        final LayoutParams newParams = (LayoutParams) getLayoutParams();
        newParams.leftMargin = newX;
        newParams.topMargin = newY;
        newParams.rightMargin = -250;
        newParams.bottomMargin = -250;
        setLayoutParams(newParams);

        //  invalidate();
    }


    private class ScaleViewListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {


            mScaleFactor = detector.getScaleFactor();

            scaleLayout();

            Log.d("ScaleListener", "Scale factor  " + mScaleFactor);
            return true;
        }


    }

    private class MoveViewListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
        @Override
        public boolean onMove(MoveGestureDetector detector) {


            mFocusX += detector.getFocusX();
            mFocusY += detector.getFocusY();

            return true;
        }


    }

    private class RotateListener extends RotateGestureDetector.SimpleOnRotateGestureListener {


        @Override
        public boolean onRotate(RotateGestureDetector detector) {
            float t = detector.getRotationDegreesDelta();
            if (t > 0) {
                mAngle -= t * ROTATE_RADIO;
            }
            return true;
        }
    }


}
