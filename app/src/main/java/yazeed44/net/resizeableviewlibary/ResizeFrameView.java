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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 10/11/14.
 *
 */
public class ResizeFrameView extends View {

    public Point[] points = new Point[4];

    public Rect resizeRectangle = new Rect();

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
        resizeRectangle.left = left;
        resizeRectangle.top = top;
        resizeRectangle.bottom = bottom;
        resizeRectangle.right = right;
    }


    //Draw the lines that connect the balls
    private void drawBallLines(final Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.ball_line));
        paint.setStrokeWidth(2);
        canvas.drawRect(
                resizeRectangle.left + colorballs.get(0).getWidthOfBall() / 2,
                resizeRectangle.top + colorballs.get(0).getWidthOfBall() / 2,
                resizeRectangle.right + colorballs.get(2).getWidthOfBall() / 2,
                resizeRectangle.bottom + colorballs.get(2).getWidthOfBall() / 2, paint);
    }

    //Draw the rectangle frame (Filling it)
    private void drawRectFrame(final Canvas canvas) {

        //fill the rectangle
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.rectangle_frame));
        paint.setStrokeWidth(0);
        canvas.drawRect(
                resizeRectangle.left + colorballs.get(0).getWidthOfBall() / 2,
                resizeRectangle.top + colorballs.get(0).getWidthOfBall() / 2,
                resizeRectangle.right + colorballs.get(2).getWidthOfBall() / 2,
                resizeRectangle.bottom + colorballs.get(2).getWidthOfBall() / 2, paint);
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
                } else if (hasFrameTouched()) {
                    onDraggingFrame();
                }

                else {
                    //resize rectangle
                    balID = -1;
                    groupId = -1;
                    for (int i = colorballs.size()-1; i>=0; i--) {
                        final ColorBall ball = colorballs.get(i);
                        // check if inside the bounds of the ball (circle)
                        // get the center for the ball
                        int centerX = ball.getX() + ball.getWidthOfBall();
                        int centerY = ball.getY() + ball.getHeightOfBall();
                        paint.setColor(Color.CYAN);
                        // calculate the radius from the touch to the center of the
                        // ball
                        double radCircle = Math
                                .sqrt((double) (((centerX - touchX) * (centerX - touchX)) + (centerY - touchY)
                                        * (centerY - touchY)));

                        if (radCircle < ball.getWidthOfBall()) {

                            initializeTouchedBall(ball);
                            break;
                        }

                        invalidate();

                    }
                }
                break;

            case MotionEvent.ACTION_MOVE: // touch drag with the ball


                Log.d(getClass().getSimpleName() + " ACTION_MOVE", "Started dragging");

                if (hasFrameTouched() && balID == -1) {
                    onDraggingFrame();
                    break;
                } else if (balID > -1) {
                    // move the balls the same as the finger


                    colorballs.get(balID).setX(touchX);
                    colorballs.get(balID).setY(touchY);

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


    private void onDraggingFrame() {

      /*  for (int i = 0 ; i < points.length;i++){
            final int newX = touchX + points[i].x;
            final int newY = touchY + points[i].y;
            points[i].y = newY;
            points[i].x = newX;
        }

        invalidate();*/
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

        Log.d(getClass().getSimpleName() + "  getBall" , "Ball " + ballId + "  is being called !!");
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

        Log.d(getClass().getSimpleName() +"  touches" , "X =  " + touchX + "   ,  Y  =  " + touchY);
        Log.d(getClass().getSimpleName() + "circles X" , " 1  =  " +  circleOneX  +  "  , 4  =  " + circleFourX );
        Log.d(getClass().getSimpleName() + "circles Y" ,  "1  =  " + circleOneY + "    ,   2  =  " + circleTwoY);


        boolean insideX = circleOneX <= touchX   && circleFourX >= touchX ;

        if (circleFourX < circleOneX){
            //reverse
            insideX = circleFourX <= touchX  && circleOneX >= touchX ;
        }



        Log.d(getClass().getSimpleName() +"  insideX ?"  , ""+ insideX);

        boolean insideY = circleOneY <= touchY   && circleTwoY >= touchY  ;

        if (circleTwoY < circleOneY){
            //reverse
            insideY = circleTwoY <= touchY  && circleOneY >= touchY ;
        }

        Log.d(getClass().getSimpleName() +"  insideY ?"  , ""+ insideY);


        final boolean hasFrameTouched = insideX && insideY && !isAnyBallBeingTouched() ;
        Log.d(getClass().getSimpleName() +  "  hasFrameTouched" , "FrameHasTouched = " + hasFrameTouched);
        return hasFrameTouched;
    }


    private ColorBall getTouchedBall(){
        ColorBall touchedBall = null;
        for(ColorBall ball : colorballs){
            if (touchX == ball.getX() && touchY == ball.getY()){
                touchedBall = ball;
            }
        }

        return touchedBall;
    }

    private boolean isAnyBallBeingTouched(){
        final boolean isAnyBallBeingTouched = getTouchedBall() != null;
        Log.d(getClass().getSimpleName() + " is Any Ball being touched ? " , isAnyBallBeingTouched + "");
        return isAnyBallBeingTouched;
    }
}
