package yazeed44.net.resizeableviewlibary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 10/11/14.
 *
 */
public abstract class ResizeFrameView extends ImageView {

    public PointF[] points = new PointF[4];

    public Rect resizeRect = new Rect();
    /**
     * point1 and point 3 are of same group and same as point 2 and point4
     */
    public int groupId = -1;
    public ArrayList<ResizeBall> resizeBalls = new ArrayList<ResizeBall>();
    // array that holds the balls
    public int ballId = 0;
    // variable to know what ball is being dragged
    public Paint paint;
   public Canvas canvas;
    public boolean draggingFrame = false;
    private Bitmap resizeBall;
    private String className = getClass().getSimpleName() + "  ";
    private int touchX , touchY;

    public ResizeFrameView(Context context) {
        super(context);
        initializeMembers();
    }

    public ResizeFrameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeMembers();
    }

    public ResizeFrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeMembers();
    }

    private void initializeMembers() {
        paint = new Paint();
        setFocusable(false); // necessary for getting the touch events
        canvas = new Canvas();
        initializeCircleBitmap();
    }

    private void initializeCircleBitmap() {

        final Drawable resizeBallDrawable = getResources().getDrawable(R.drawable.resize_circle);

        resizeBall = ((BitmapDrawable) resizeBallDrawable).getBitmap();
    }


    // the method that draws the balls
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (resizeBalls.isEmpty()) {
            initializeBalls();
        }


        initializeResizeRect();


        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(5);


        drawBallLines(canvas);


        drawRectFrame(canvas);


        drawBalls(canvas);

        onResizing(resizeRect);


    }

    private void initializeBalls() {

        //Reset
        resizeBalls.clear();
        ResizeBall.count = 0;


        final Rect viewRect = new Rect();
        getDrawingRect(viewRect);

        final int bw = getBallWidth();
        final int bh = getBallHeight();

        final int offset = 0;

        //initialize rectangle.
        points[0] = new PointF();
        points[0].y = getTop() + offset;
        points[0].x = getLeft();

        points[1] = new PointF();
        points[1].y = getBottom() + offset;
        points[1].x = getLeft();

        points[2] = new PointF();
        points[2].y = getBottom() + offset;
        points[2].x = getRight();

        points[3] = new PointF();
        points[3].y = getTop() + offset;
        points[3].x = getRight();

        ballId = 2;
        groupId = 1;
        // declare each ball with the ColorBall class


        for (PointF pt : points) {
            resizeBalls.add(new ResizeBall(pt));
        }

        initializeResizeRect();
    }


    private void initializeResizeRect() {

        float left, top, right, bottom;
        left = points[0].x;
        top = points[0].y;
        right = points[0].x;
        bottom = points[0].y;
        for (int i = 1; i < points.length; i++) {
            left = left > points[i].x ? points[i].x:left;
            top = top > points[i].y ? points[i].y:top;
            right = right < points[i].x ? points[i].x:right;
            bottom = bottom < points[i].y ? points[i].y:bottom;
        }
        resizeRect.left = (int) left;
        resizeRect.top = (int) top;
        resizeRect.bottom = (int) bottom;
        resizeRect.right = (int) right;


        if (draggingFrame) {

            onDraggingRect(new Point(touchX, touchY));

        }
    }


    //Draw the lines that connect the balls
    private void drawBallLines(final Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.ball_line));
        paint.setStrokeWidth(2);
        canvas.drawRect(
                resizeRect.left + getBallWidth() / 2,
                resizeRect.top + getBallWidth() / 2,
                resizeRect.right + getBallWidth() / 2,
                resizeRect.bottom + getBallWidth() / 2, paint);
    }

    //Draw the rectangle frame (Filling it)
    private void drawRectFrame(final Canvas canvas) {

        //fill the rectangle
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.rectangle_frame));
        paint.setStrokeWidth(0);
        canvas.drawRect(
                resizeRect.left + getBallWidth() / 2,
                resizeRect.top + getBallWidth() / 2,
                resizeRect.right + getBallWidth() / 2,
                resizeRect.bottom + getBallWidth() / 2, paint);
    }

    //Draw resize balls (Corners) and their ID
    private void drawBalls(final Canvas canvas) {
        // draw the balls on the canvas
        paint.setColor(Color.BLUE);
        paint.setTextSize(18);
        paint.setStrokeWidth(0);
        for (int i = 0; i < resizeBalls.size(); i++) {
            ResizeBall ball = resizeBalls.get(i);
            canvas.drawBitmap(resizeBall, ball.getX(), ball.getY(),
                    paint);

            canvas.drawText("" + (i+1), ball.getX(), ball.getY(), paint);
        }
    }


    // events when touching the screen
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();


        touchX = (int) event.getX();
        touchY = (int) event.getY();

        switch (eventAction) {

            case MotionEvent.ACTION_DOWN: // touch down so check if the finger is on


                //get TouchedBall
                ballId = -1;
                    groupId = -1;

                final ResizeBall touchedBall = getTouchedBall();

                    if (touchedBall != null) {
                        initializeTouchedBall(touchedBall);
                    }

                break;

            case MotionEvent.ACTION_MOVE: // touch drag with the ball

                if (ballId > -1) {
                    onDraggingBall(new Point(touchX, touchY));
                } else if (hasFrameTouched()) {
                    draggingFrame = true;
                    invalidate();
                }


                break;

            case MotionEvent.ACTION_UP:
                // touch drop - just do things here after dropping
                draggingFrame = false;

                break;
        }
        // redraw the canvas
        invalidate();
        return true;

    }

    private void initializeTouchedBall(final ResizeBall ball) {
        draggingFrame = false;
        ballId = ball.getID();
        if (ballId == 1 || ballId == 3) {
            groupId = 2;
        } else {
            groupId = 1;
        }
        invalidate();
    }

    private ResizeBall getTouchedBall() {
        ResizeBall touchedBall = null;

        int range;
        for (ResizeBall ball : resizeBalls) {

            range = getBallWidth();
            final boolean insideXRange = touchX + range > ball.getX() && touchX - range < ball.getX();

            range = getBallHeight();
            final boolean insideYRange = touchY + range > ball.getY() && touchY - range < ball.getY();

            if (insideXRange && insideYRange) {

                touchedBall = ball;
            }

        }

        return touchedBall;
    }


    //if ball two is above ball one that means the frame is inverted
    public boolean isFrameInverted() {
        final int ballOneY = resizeBalls.get(0).getY();
        final int ballTwoY = resizeBalls.get(1).getY();

        return ballTwoY > ballOneY;
    }


    private boolean hasFrameTouched() {
        return resizeRect.contains(touchX, touchY);

    }

    private boolean isAnyBallBeingTouched() {
        final boolean isAnyBallBeingTouched = ballId != -1;
        // Log.d(getClass().getSimpleName() + " is Any Ball being touched ? " , isAnyBallBeingTouched + "");
        return isAnyBallBeingTouched;
    }


    private int getBallWidth() {
        return resizeBall.getWidth();
    }

    private int getBallHeight() {
        return resizeBall.getHeight();
    }

    public PointF getTopLeftCorner() {
        float src[] = new float[8];
        float[] dst = new float[]{0, 0, getWidth(), 0, 0, getHeight(), getWidth(), getHeight()};
        getMatrix().mapPoints(src, dst);
        final PointF cornerPoint = new PointF(getX() + src[0], getY() + src[1]);
        return cornerPoint;
    }

    public PointF getTopRightCorner() {
        float src[] = new float[8];
        float[] dst = new float[]{0, 0, getWidth(), 0, 0, getHeight(), getWidth(), getHeight()};
        getMatrix().mapPoints(src, dst);
        final PointF cornerPoint = new PointF(getX() + src[2], getY() + src[3]);
        return cornerPoint;
    }

    public PointF getBottomLeftCorner() {
        float src[] = new float[8];
        float[] dst = new float[]{0, 0, getWidth(), 0, 0, getHeight(), getWidth(), getHeight()};
        getMatrix().mapPoints(src, dst);
        final PointF cornerPoint = new PointF(getX() + src[4], getY() + src[5]);
        return cornerPoint;
    }

    public PointF getBottomRightCorner() {
        float src[] = new float[8];
        float[] dst = new float[]{0, 0, getWidth(), 0, 0, getHeight(), getWidth(), getHeight()};
        getMatrix().mapPoints(src, dst);
        final PointF cornerPoint = new PointF(getX() + src[6], getY() + src[7]);
        return cornerPoint;
    }


    public abstract void onDraggingBall(final Point touches);

    public abstract void onDraggingRect(final Point touches);

    public abstract void onResizing(final Rect viewRect);
}
