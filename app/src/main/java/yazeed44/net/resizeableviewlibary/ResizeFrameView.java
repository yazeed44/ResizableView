package yazeed44.net.resizeableviewlibary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
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
public class ResizeFrameView extends ImageView {

    public Point[] points = new Point[4];

    public Rect resizeRect = new Rect();
    /**
     * point1 and point 3 are of same group and same as point 2 and point4
     */
    public int groupId = -1;
    public ArrayList<ColorBall> colorballs = new ArrayList<ColorBall>();
    // array that holds the balls
    public int balID = 0;
    // variable to know what ball is being dragged
    public Paint paint;
   public Canvas canvas;
    private boolean draggingFrame = false;
    private String className = getClass().getSimpleName() + "  ";
    private int touchX , touchY;

    public ResizeFrameView(Context context) {
        super(context);
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
    }

    public ResizeFrameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ResizeFrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
    }


    // the method that draws the balls
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        if (points[3] == null) { //point4 null when user did not touch and move on screen.
            return;
        }

        initializeResizeRect();


        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(5);


        drawBallLines(canvas);


        drawRectFrame(canvas);


        drawBalls(canvas);


    }

    private void initializeResizeRect() {

        int left, top, right, bottom;
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
        resizeRect.left = left;
        resizeRect.top = top;
        resizeRect.bottom = bottom;
        resizeRect.right = right;


        if (draggingFrame) {
            resizeRect.offsetTo(touchX - resizeRect.width() / 2, touchY - resizeRect.height() / 2);

            colorballs.get(0).setX(resizeRect.left);
            colorballs.get(0).setY(resizeRect.top);

            colorballs.get(1).setX(resizeRect.left);
            colorballs.get(1).setY(resizeRect.bottom);

            colorballs.get(2).setX(resizeRect.right);
            colorballs.get(2).setY(resizeRect.bottom);

            colorballs.get(3).setX(resizeRect.right);
            colorballs.get(3).setY(resizeRect.top);

        }
    }


    //Draw the lines that connect the balls
    private void drawBallLines(final Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.ball_line));
        paint.setStrokeWidth(2);
        canvas.drawRect(
                resizeRect.left + colorballs.get(0).getWidthOfBall() / 2,
                resizeRect.top + colorballs.get(0).getWidthOfBall() / 2,
                resizeRect.right + colorballs.get(2).getWidthOfBall() / 2,
                resizeRect.bottom + colorballs.get(2).getWidthOfBall() / 2, paint);
    }

    //Draw the rectangle frame (Filling it)
    private void drawRectFrame(final Canvas canvas) {

        //fill the rectangle
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.rectangle_frame));
        paint.setStrokeWidth(0);
        canvas.drawRect(
                resizeRect.left + colorballs.get(0).getWidthOfBall() / 2,
                resizeRect.top + colorballs.get(0).getWidthOfBall() / 2,
                resizeRect.right + colorballs.get(2).getWidthOfBall() / 2,
                resizeRect.bottom + colorballs.get(2).getWidthOfBall() / 2, paint);
    }

    //Draw resize balls (Corners) and their ID
    private void drawBalls(final Canvas canvas) {
        // draw the balls on the canvas
        paint.setColor(Color.BLUE);
        paint.setTextSize(18);
        paint.setStrokeWidth(0);
        for (int i =0; i < colorballs.size(); i ++) {
            ColorBall ball = colorballs.get(i);
            canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(),
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
                // a ball
                if (points[0] == null) {
                    initializeBalls();
                }

                else {
                    //get TouchedBall
                    balID = -1;
                    groupId = -1;

                    final ColorBall touchedBall = getTouchedBall();

                    if (touchedBall != null) {
                        initializeTouchedBall(touchedBall);
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE: // touch drag with the ball

                if (balID > -1) {
                    onDraggingBall();
                } else if (hasFrameTouched()) {
                    onDraggingFrame();
                }


                break;

            case MotionEvent.ACTION_UP:
                // touch drop - just do things here after dropping


                break;
        }
        // redraw the canvas
        invalidate();
        return true;

    }

    private void initializeTouchedBall(final ColorBall ball) {
        balID = ball.getID();
        if (balID == 1 || balID == 3) {
            groupId = 2;
        } else {
            groupId = 1;
        }
        invalidate();
    }

    private ColorBall getTouchedBall() {
        ColorBall touchedBall = null;

        int range;
        for (ColorBall ball : colorballs) {

            range = ball.getWidthOfBall();
            final boolean insideXRange = touchX + range > ball.getX() && touchX - range < ball.getX();

            range = ball.getHeightOfBall();
            final boolean insideYRange = touchY + range > ball.getY() && touchY - range < ball.getY();

            if (insideXRange && insideYRange) {

                touchedBall = ball;
            }

        }

        return touchedBall;
    }

    private void onDraggingBall() {
        onDraggingBall(touchX, touchY);
    }

    private void onDraggingBall(final int newX, final int newY) {

        draggingFrame = false;
        // move the balls the same as the finger
        colorballs.get(balID).setX(newX);
        colorballs.get(balID).setY(newY);

        paint.setColor(Color.CYAN);
        if (groupId == 1) {
            colorballs.get(1).setX(colorballs.get(0).getX());
            colorballs.get(1).setY(colorballs.get(2).getY());
            colorballs.get(3).setX(colorballs.get(2).getX());
            colorballs.get(3).setY(colorballs.get(0).getY());
        } else {
            colorballs.get(0).setX(colorballs.get(1).getX());
            colorballs.get(0).setY(colorballs.get(3).getY());
            colorballs.get(2).setX(colorballs.get(3).getX());
            colorballs.get(2).setY(colorballs.get(1).getY());
        }

        invalidate();
    }

    private void onDraggingFrame() {

        draggingFrame = true;


        invalidate();

    }

    private void initializeBalls() {

        //Reset
        colorballs.clear();
        ColorBall.count = 0;

        //initialize rectangle.
        points[0] = new Point();
        points[0].x = touchX;
        points[0].y = touchY;

        points[1] = new Point();
        points[1].x = touchX;
        points[1].y = touchY + 30;

        points[2] = new Point();
        points[2].x = touchX + 30;
        points[2].y = touchY + 30;

        points[3] = new Point();
        points[3].x = touchX +30;
        points[3].y = touchY;

        balID = 2;
        groupId = 1;
        // declare each ball with the ColorBall class

        final Drawable resizeCircle = getResources().getDrawable(R.drawable.resize_circle);

        final Bitmap resizeCircleBitmap = ((BitmapDrawable)resizeCircle).getBitmap();

        for (Point pt : points) {
            colorballs.add(new ColorBall(resizeCircleBitmap, pt));
        }

        invalidate();
    }


    private ColorBall getBall(final int ballId){
        final ColorBall ball = colorballs.get(ballId-1);

        //    Log.d(getClass().getSimpleName() + "  getBall" , "Ball " + ballId + "  is being called !!");
        return ball;
    }

    private int getBallX(final int ballId){
        return getBall(ballId).getX();
    }

    private int getBallY(final int ballId){
        return getBall(ballId).getY();
    }


    private boolean hasFrameTouched(){


        final int circleOneX = getBallX(1);
        final int circleOneY = getBallY(1);
        final int circleFourX = getBallX(4);
        final int circleTwoY = getBallY(2);

        //   Log.d(getClass().getSimpleName() +"  touches" , "X =  " + touchX + "   ,  Y  =  " + touchY);
        // Log.d(getClass().getSimpleName() + "circles X" , " 1  =  " +  circleOneX  +  "  , 4  =  " + circleFourX );
        // Log.d(getClass().getSimpleName() + "circles Y" ,  "1  =  " + circleOneY + "    ,   2  =  " + circleTwoY);


        boolean insideX = circleOneX <= touchX   && circleFourX >= touchX ;

        if (circleFourX < circleOneX){
            //reverse
            insideX = circleFourX <= touchX  && circleOneX >= touchX ;
        }


//        Log.d(getClass().getSimpleName() +"  insideX ?"  , ""+ insideX);

        boolean insideY = circleOneY <= touchY   && circleTwoY >= touchY  ;

        if (circleTwoY < circleOneY){
            //reverse
            insideY = circleTwoY <= touchY  && circleOneY >= touchY ;
        }

//        Log.d(getClass().getSimpleName() +"  insideY ?"  , ""+ insideY);


        final boolean hasFrameTouched = insideX && insideY && !isAnyBallBeingTouched() ;
        //   Log.d(getClass().getSimpleName() +  "  hasFrameTouched" , "FrameHasTouched = " + hasFrameTouched);
        return hasFrameTouched;
    }




    private boolean isAnyBallBeingTouched(){
        final boolean isAnyBallBeingTouched = balID != -1;
        // Log.d(getClass().getSimpleName() + " is Any Ball being touched ? " , isAnyBallBeingTouched + "");
        return isAnyBallBeingTouched;
    }
}
