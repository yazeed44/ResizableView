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

    private float mScaleFactor = 1.0f;
    private ScaleGestureDetector mScaleDetector;
    private MoveGestureDetector mMoveDetector;
    private RotateGestureDetector mRotateDetector;
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
        mRotateDetector = new RotateGestureDetector(getContext(), new RotateViewListener());


        // mFocusX = Util.getScreenDimension(getContext()).x / 2;
        // mFocusY = Util.getScreenDimension(getContext()).y / 2;



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
                drag2(shapeMotionEvent, shape);
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

    private void drag2(final MotionEvent shapeEvent, final View shape) {

        //TODO Fix over sizing on the first touch



        /*if (true){
            setScaleX(shapeEvent.getRawX() / mOrgX  );

            return;
        }*/


        final int currentLeftMargin = ((LayoutParams) getLayoutParams()).leftMargin;
        final int currentRightMargin = currentLeftMargin + getWidth();


        final int rightOffset = Math.round(shapeEvent.getRawX() - currentRightMargin);
        final int newRightMargin = currentRightMargin + rightOffset;
        final int newLeftMargin = currentLeftMargin;

        final int newWidth = Math.round((newRightMargin - newLeftMargin));

       /* final int widthOffset = rightOffset > 0 ? 15 : -15;
        final int newWidth = getWidth() + widthOffset;*/
        new Resizer(this)
                .leftMargin(newLeftMargin)
                .rightMargin(newRightMargin)
                .width(newWidth)
                .resize();
    }

    private int getWidthOffset(final int rightOffset) {

        return 0;
    }


    private void drag1(final MotionEvent shapeEvent) {
        //TODO Fix over sizing on the first touch

        final int currentTopMargin = ((LayoutParams) getLayoutParams()).topMargin;
        final int currentBottomMargin = ((LayoutParams) getLayoutParams()).bottomMargin;


        final int newTopMargin = (int) (currentTopMargin + (shapeEvent.getRawY() - currentTopMargin));
        final int newBottomMargin = currentBottomMargin == 0 ? getHeight() + currentTopMargin : currentBottomMargin;
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


                        onDraggingShape(motionEvent, v);

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

                mRotateDetector.onTouchEvent(event);


                mMoveDetector.onTouchEvent(event);
                display();


                return true;
            }
        };
    }

    private void display() {

        final int width = getWidth();
        final int height = getHeight();

        final float scaledViewCenterX = (width * mScaleFactor) / 2f;
        final float scaledViewCenterY = (height * mScaleFactor) / 2f;

        //setScaleX(mScaleFactor);
        //setScaleY(mScaleFactor);


        setTranslationX(getTranslationX() + mFocusX);
        setTranslationY(getTranslationY() + mFocusY);


        //TODO Implement rotating view by two finger gesture


    }

    private void applyRotation() {
        setRotation(mAngle);
    }

    @Override
    public OnTouchListener createStretchListener() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    drag2(event, v);
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


            mScaleFactor *= detector.getScaleFactor();

            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10f));




            Log.d("ScaleListener", "Scale factor  " + mScaleFactor);
            return true;
        }

    }

    private class MoveViewListener extends MoveGestureDetector.SimpleOnMoveGestureListener {

        @Override
        public boolean onMoveBegin(MoveGestureDetector detector) {

           /*if (mFocusX == 0.0){
                mFocusX -= Util.getScreenDimension(getContext()).x/2;
            }
            else if (mFocusY == 0.0){
                mFocusY -= Util.getScreenDimension(getContext()).y/2;
            }*/

            return true;
        }

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
        public boolean onRotateBegin(RotateGestureDetector detector) {

            /*final float scaledViewCenterX = (getWidth() * mScaleFactor) / 2f;
            final float scaledViewCenterY = (getHeight() * mScaleFactor) / 2f;
            setPivotX(scaledViewCenterX);
            setPivotY(scaledViewCenterY);
*/


            return super.onRotateBegin(detector);
        }

        @Override
        public boolean onRotate(RotateGestureDetector detector) {

            mAngle -= detector.getRotationDegreesDelta();
            Log.d(TAG, "New Angle  " + mAngle);

            applyRotation();
            return true;
        }

        @Override
        public void onRotateEnd(RotateGestureDetector detector) {
            super.onRotateEnd(detector);
        }
    }


}
