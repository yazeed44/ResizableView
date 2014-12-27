package net.yazeed44.resizableviewlibrary;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yazeed44 on 10/11/14.
 */
public class ResizableImageView extends ResizeFrameView implements View.OnDragListener {


    private int mOrgX;
    private int mOrgY;

    public ResizableImageView(Context context) {
        super(context);
        setOnDragListener(this);
    }


    private void onDraggingShape(final MotionEvent shapeMotionEvent) {

        for (ResizeShapeView shapeView : resizeShapes) {
            recomputeViewAttributes(shapeView);
        }


        switch (shapeId) {

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

        final int offsetX = (int) (shapeEvent.getRawX() - mOrgX);

        final int newLeftMargin = getLeft() - offsetX;

        final int newRightMargin = getRight() - offsetX;

        final int newWidth = newRightMargin - newLeftMargin + offsetX;

        new Resizer(this)
                //  .leftMargin(newLeftMargin)
                //  .rightMargin(newRightMargin)
                .width(newWidth)
                .resize();

    }

    private void drag1(final MotionEvent shapeEvent) {

    }

    private void drag2(final MotionEvent shapeEvent) {
        final double resizeFactor = ((shapeEvent.getRawX() / mOrgX) / 2);

        final int offsetX = (int) (shapeEvent.getRawX() - mOrgX);

        final int newWidth = (int) (getWidth() * resizeFactor);

        // final int newLeftMargin = getLeft() - offsetX;

        // final int newRightMargin = getLeft() - offsetX;


        new Resizer(this)
                .width(newWidth)
                        //             .leftMargin(newLeftMargin)
                        //               .rightMargin(newRightMargin)
                .resize();

    }

    private void drag3(final MotionEvent shapeEvent) {

    }


    private void startDragging() {
        ClipData clipData = ClipData.newPlainText("", "");


        // Instantiates the drag shadow builder.
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

        Log.d(className + "Change position", "Drag XY    " + "  X  =  " + dragX + "   ,  Y  =  " + dragY);

        final LayoutParams layoutParams = (LayoutParams) getLayoutParams();

        layoutParams.leftMargin = dragX - (getWidth() / 2);
        layoutParams.topMargin = dragY - (getHeight() / 2);

        setLayoutParams(layoutParams);

    }

    private void assignShapeId(final View view) {


        shapeId = ((ResizeShapeView) view).getShapeId();


        if (shapeId == 1 || shapeId == 3) {
            groupId = 2;
        } else {
            groupId = 1;
        }

        Log.d(className + "assignShapeId", "The new Shape is " + shapeId + " Shape  !!");

    }


    @Override
    public boolean onTouchEvent(final MotionEvent event) {

        final int eventAction = event.getAction();


        if (eventAction == MotionEvent.ACTION_DOWN) {

            startDragging();

            return true;
        } else {
            return false;
        }


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
                        mOrgX = (int) getX();
                        mOrgY = (int) getY();
                        break;
                    }


                    case MotionEvent.ACTION_MOVE: {
                        //Moving the finger on screen

                        onDraggingShape(motionEvent);

                        break;
                    }


                    case MotionEvent.ACTION_UP: {
                        //Finger off the screen
                        shapeId = -1;

                        break;
                    }

                }

                return true;
            }
        };


    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        switch (event.getAction()) {

            case DragEvent.ACTION_DRAG_EXITED:

                changePosition(event);

                break;
            default:
                break;
        }
        return true;
    }


}
