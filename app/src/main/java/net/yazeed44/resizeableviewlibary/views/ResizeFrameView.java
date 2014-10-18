package net.yazeed44.resizeableviewlibary.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import net.yazeed44.resizeableviewlibary.R;

import java.util.ArrayList;


/**
 * Created by yazeed44 on 10/11/14.
 */
public abstract class ResizeFrameView extends FrameLayout {

    public int[] positions = new int[4];

    public Rect resizeRect = new Rect();
    /**
     * point1 and point 3 are of same group and same as point 2 and point4
     */
    public int groupId = -1;
    public ArrayList<ResizeBallView> resizeBalls = new ArrayList<ResizeBallView>();
    // array that holds the balls
    public int ballId = 0;
    // variable to know what ball is being dragged

    public boolean draggingFrame = false;
    public ImageView resizableImage;
    private Bitmap resizeBall;

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


    }


    private void initializeMembers() {
        setFocusable(true); // necessary for getting the touch events
        initializeCircleBitmap();
    }

    private void initializeCircleBitmap() {

        final Drawable resizeBallDrawable = getResources().getDrawable(R.drawable.resize_circle);

        resizeBall = ((BitmapDrawable) resizeBallDrawable).getBitmap();
    }


    private void initializeBalls() {

        //Reset
        resizeBalls.clear();
        ResizeBallView.count = 0;


        //initialize positions for circles.
        positions[0] = Gravity.LEFT | Gravity.TOP; // Circle 1

        positions[1] = Gravity.LEFT | Gravity.BOTTOM; // Circle 2

        positions[2] = Gravity.RIGHT | Gravity.BOTTOM; // Circle 3

        positions[3] = Gravity.RIGHT | Gravity.TOP; // Circle 4

        ballId = 2;
        groupId = 1;


        for (int position : positions) {
            final LayoutParams params = new LayoutParams(getBallWidth(), getBallHeight(), position);

            final ResizeBallView resizeBallView = new ResizeBallView(getContext());
            addView(resizeBallView, params);


            resizeBalls.add(resizeBallView);
        }


        setBallsListener();


    }

    private void setBallsListener() {

        for (ResizeBallView ballView : resizeBalls) {


            ballView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    initializeBallId(view);
                    onDraggingBall(new PointF(motionEvent.getX(), motionEvent.getY()));


                    return false;
                }
            });

        }

    }

    private void initializeBallId(final View view) {
        final ResizeBallView ballView = (ResizeBallView) view;

        ballId = ballView.getId();


        if (ballId == 1 || ballId == 3) {
            groupId = 2;
        } else {
            groupId = 1;
        }

    }


    private int getBallWidth() {
        return resizeBall.getWidth();
    }

    private int getBallHeight() {
        return resizeBall.getHeight();
    }


    public abstract void onDraggingBall(final PointF touches);

    public abstract void onResizing(final Rect viewRect);
}
