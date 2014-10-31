package net.yazeed44.resizableviewlibrary.views;

import android.content.ClipData;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by yazeed44 on 10/11/14.
 */
public class ResizableView extends ResizeFrameView implements View.OnDragListener {

    private static final String FRAME_TAG = "resizeFrame";

    public ResizableView(Context context, final ImageView resizeableImage) {
        super(context, resizeableImage);


        setTag(FRAME_TAG);
        setOnDragListener(this);
        //setupLongClick();
    }


    public void onResizing(final Rect resizeRect) {
        final LayoutParams params = new LayoutParams(getLayoutParams());

        params.rightMargin = resizeRect.right;
        params.leftMargin = resizeRect.left;
        params.topMargin = resizeRect.top;
        params.bottomMargin = resizeRect.bottom;
        params.width = resizeRect.width();
        params.height = resizeRect.height();




        this.setLayoutParams(params);

        Log.d(getClass().getSimpleName() + "  onResizing", "new Height  =  " + params.height + "   ,  new Width  =  " + params.width);

    }

    public Rect onDraggingBall(final PointF touches) {
        final PointF[] ballsXY = new PointF[4];

        // move the balls the same as the finger
        resizeBalls.get(ballId).setX(touches.x);
        resizeBalls.get(ballId).setY(touches.y);

        float newBallOneX = resizeBalls.get(0).getX();
        float newBallOneY = resizeBalls.get(0).getY();


        float newBallTwoX = resizeBalls.get(1).getX();
        float newBallTwoY = resizeBalls.get(1).getY();


        float newBallThreeX = resizeBalls.get(2).getX();
        float newBallThreeY = resizeBalls.get(2).getY();

        float newBallFourX = resizeBalls.get(3).getX();
        float newBallFourY = resizeBalls.get(3).getY();


        if (groupId == 1) {
            newBallTwoX = newBallOneX;
            newBallTwoY = newBallThreeY;
            newBallFourX = newBallThreeX;
            newBallFourY = newBallOneY;
        } else {
            newBallOneX = newBallTwoX;
            newBallOneY = newBallFourY;
            newBallThreeX = newBallFourX;
            newBallThreeY = newBallTwoY;

        }


        ballsXY[0] = new PointF(newBallOneX, newBallOneY);
        ballsXY[1] = new PointF(newBallTwoX, newBallTwoY);
        ballsXY[2] = new PointF(newBallThreeX, newBallThreeY);
        ballsXY[3] = new PointF(newBallFourX, newBallFourY);

        for (int i = 0; i < resizeBalls.size(); i++) {
            final PointF xy = ballsXY[i];
            resizeBalls.get(i).setX(xy.x);
            resizeBalls.get(i).setY(xy.y);
        }

        final Rect afterDragRect = getRect(ballsXY);

        return afterDragRect;


    }

    private Rect getRect(final PointF[] points) {
        final Rect resizeRect = new Rect();
        float left, top, right, bottom;
        left = points[0].x;
        top = points[0].y;
        right = points[0].x;
        bottom = points[0].y;
        for (int i = 1; i < points.length; i++) {
            left = left > points[i].x ? points[i].x : left;
            top = top > points[i].y ? points[i].y : top;
            right = right < points[i].x ? points[i].x : right;
            bottom = bottom < points[i].y ? points[i].y : bottom;
        }
        resizeRect.left = (int) left;
        resizeRect.top = (int) top;
        resizeRect.bottom = (int) bottom;
        resizeRect.right = (int) right;


        return resizeRect;

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


    @Override
    public boolean onDrag(View v, DragEvent event) {


        final String msg = className + " On drag event";

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:

                Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
                // Do nothing

                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                //Do nothing


                break;
            case DragEvent.ACTION_DRAG_EXITED:
                Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");

                changePosition(new PointF(event.getX(), event.getY()));

                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");

                //Do nothing

                break;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");
                // Do nothing


                break;
            case DragEvent.ACTION_DROP:
                Log.d(msg, "ACTION_DROP event");
                // Do nothing


                break;
            default:
                break;
        }
        return true;
    }

    private void changePosition(final PointF drag) {
        final int dragX = (int) drag.x, dragY = (int) drag.y;

        Log.d(className + "Change position", "Drag XY    " + "  X  =  " + dragX + "   ,  Y  =  " + dragY);

        final LayoutParams layoutParams = (LayoutParams) getLayoutParams();

        layoutParams.leftMargin = dragX - (getWidth() / 2);
        layoutParams.topMargin = dragY - (getHeight() / 2);

        setLayoutParams(layoutParams);

    }
}
