package net.yazeed44.resizableviewlibrary;

import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yazeed44 on 10/11/14.
 */
public class ResizableViewLayout extends ResizeFrameView {


    private int mOrgX;
    private int mOrgY;

    public ResizableViewLayout(Context context) {
        super(context);
        init();
    }

    public ResizableViewLayout(final Context context, AttributeSet set) {
        super(context, set);
        init();
    }

    public ResizableViewLayout(final Context context, AttributeSet set, int defStyle) {
        super(context, set, defStyle);
        init();
    }


    private void init() {

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

        //TODO Implement this correctly


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
        //TODO Implement this correctly

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
        //TODO Implement this correctly

        final int factor;
        if (shapeEvent.getAction() == MotionEvent.ACTION_DOWN) {
            factor = 90;
        } else {
            factor = 0;
        }

        final int newTopMargin = (int) (getTop() + (shapeEvent.getRawY() - getTop())) - factor;
        final int newBottomMargin = (int) getBottom();
        final int newHeight = newBottomMargin - newTopMargin;

        new Resizer(this)
                .topMargin(newTopMargin)
                .bottomMargin(newBottomMargin)
                .height(newHeight)
                .resize();
    }



    private void drag3(final MotionEvent shapeEvent) {
        //TODO Implement this correctly

        final int newTopMargin = getTop();
        final int newBottomMargin = (int) (getBottom() + (shapeEvent.getRawY() - getBottom()));
        final int newHeight = newBottomMargin - newTopMargin;

        new Resizer(this)
                .topMargin(newTopMargin)
                .bottomMargin(newBottomMargin)
                .height(newHeight)
                .resize();


    }


    private void startDragging() {
        ClipData clipData = ClipData.newPlainText("", "");

        View.DragShadowBuilder myShadow = new DragShadowBuilder(this);


        // Starts the drag
        startDrag(clipData,  // the data to be dragged
                myShadow,  // the drag shadow builder
                this,      // no need to use local data
                0          // flags (not currently used, set to 0)
        );

    }

    private void changePosition(final DragEvent dragEvent) {
        //TODO Improve dragging
        final int dragX = (int) dragEvent.getX(), dragY = (int) dragEvent.getY();

        // Log.d(className + "Change position", "Drag XY    " + "  X  =  " + dragX + "   ,  Y  =  " + dragY);

        final LayoutParams layoutParams = (LayoutParams) getLayoutParams();

        layoutParams.leftMargin = dragX - (getWidth() / 2);
        layoutParams.topMargin = dragY - (getHeight() / 2);

        setLayoutParams(layoutParams);

    }

    private void assignShapeId(final View view) {


        mShapeId = ((ResizeShapeView) view).getShapeId();

        // Log.d(className + "assignShapeId", "The new Shape is " + mShapeId + " Shape  !!");

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
    public OnTouchListener createDragListener() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int touchX = (int) event.getRawX();
                final int touchY = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        mOrgX = touchX - getLeft();
                        mOrgY = touchY - getTop();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        final LayoutParams newParams = (LayoutParams) getLayoutParams();
                        newParams.leftMargin = touchX - mOrgX;
                        newParams.topMargin = touchY - mOrgY;
                        newParams.rightMargin = -250;
                        newParams.bottomMargin = -250;
                        setLayoutParams(newParams);

                        invalidate();
                        break;

                }


                return true;
            }
        };
    }


}
