package net.yazeed44.resizableviewlibrary.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import net.yazeed44.resizableviewlibrary.R;

import java.util.ArrayList;


/**
 * Created by yazeed44 on 10/11/14.
 */
public abstract class ResizeFrameView extends FrameLayout {

    private final static String FRAME_TAG = "frameTag";
    public int[] positions = new int[4]; // Balls Positions on the frame
    public int groupId = -1;
    public ArrayList<ResizeBallView> resizeBalls = new ArrayList<>();
    // array that holds the balls
    public int ballId = 0;
    public ImageView resizableImage;
    public String className = getClass().getSimpleName() + "   ";
    private Bitmap resizeBall;
    public boolean isDraggingFrame;


    public ResizeFrameView(final Context context, final ImageView resizableImage) {
        super(context);
        initializeFrame(resizableImage);


    }


    private void initializeFrame(final ImageView resizableImage) {
        this.resizableImage = resizableImage;


        final LayoutParams imageParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        addView(resizableImage, imageParams);

        initializeMembers();
        initializeBalls();
        setBackgroundResource(R.color.ball_line);

        setFocusable(true);


    }


    private void initPositions(boolean hasBallsInitialized) {

        final boolean frameInverted = isFrameInverted(hasBallsInitialized);

        //initialize positions for circles.

        if (!frameInverted) {
            positions[0] = Gravity.LEFT | Gravity.TOP; // Circle 1

            positions[1] = Gravity.LEFT | Gravity.BOTTOM; // Circle 2

            positions[2] = Gravity.RIGHT | Gravity.BOTTOM; // Circle 3

            positions[3] = Gravity.RIGHT | Gravity.TOP; // Circle 4
        } else {
            positions[0] = Gravity.LEFT | Gravity.BOTTOM;

            positions[1] = Gravity.LEFT | Gravity.TOP;

            positions[2] = Gravity.RIGHT | Gravity.TOP;

            positions[3] = Gravity.RIGHT | Gravity.BOTTOM;
        }
    }

    private boolean isFrameInverted(boolean hasBallsInitialized) {

        if (hasBallsInitialized) {
            final ResizeBallView ballOne = resizeBalls.get(0), ballTwo = resizeBalls.get(1);

            return ballTwo.getY() > ballOne.getY();
        } else {
            return false;
        }

    }


    private void initializeMembers() {
        setFocusable(true); // necessary for getting the touch events
        initializeCircleBitmap();
        initPositions(false);
    }

    private void initializeCircleBitmap() {

        final Drawable resizeBallDrawable = getResources().getDrawable(R.drawable.resize_circle);

        resizeBall = ((BitmapDrawable) resizeBallDrawable).getBitmap();
    }


    private void initializeBalls() {

        resetBalls();


        initPositions(false);

        reattachBalls();

        setBallsListener();


    }


    private void resetBalls() {
        for (int index = 0; index < resizeBalls.size(); index++) {
            removeView(resizeBalls.get(index));
        }


        resizeBalls.clear();
        ResizeBallView.count = 0;

        ballId = 2;
        groupId = 1;
    }

    private void reattachBalls() {
        for (int position : positions) {
            final LayoutParams params = new LayoutParams(getBallWidth(), getBallHeight(), position);

            final ResizeBallView resizeBallView = new ResizeBallView(getContext());
            addView(resizeBallView, params);


            resizeBalls.add(resizeBallView);
        }
    }

    private void setBallsListener() {

        for (ResizeBallView ballView : resizeBalls) {


            ballView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    final int eventAction = motionEvent.getAction();

                    switch (eventAction) {

                        case MotionEvent.ACTION_DOWN: {
                            //First touch
                            initBallId(view);
                            isDraggingFrame = false;
                            break;
                        }


                        case MotionEvent.ACTION_MOVE: {
                            //Moving the finger on screen

                            final Rect afterDragRect = onDraggingBall(new PointF(motionEvent.getX(), motionEvent.getY()));
                            onResizing(afterDragRect);
                            isDraggingFrame = false;
                            break;
                        }


                        case MotionEvent.ACTION_UP: {
                            //Finger off the screen

                            break;
                        }

                    }


                    //rePostBalls();
                   // initializeBalls();
                    invalidate();



                    return true;
                }
            });

        }

    }

    private void rePostBalls() {
        initPositions(true);
        resetBalls();
        reattachBalls();
        setBallsListener();

    }

    private void initBallId(final View view) {


        ballId = ((ResizeBallView)view).getBallId();


        if (ballId == 1 || ballId == 3) {
            groupId = 2;
        } else {
            groupId = 1;
        }

        Log.d(className + "initializeBall", "The new ball is " + (ballId + 1) + " ball !!");

    }


    private int getBallWidth() {
        return resizeBall.getWidth();
    }

    private int getBallHeight() {
        return resizeBall.getHeight();
    }


    public abstract Rect onDraggingBall(final PointF touches);

    public abstract void onResizing(final Rect viewRect);
}
