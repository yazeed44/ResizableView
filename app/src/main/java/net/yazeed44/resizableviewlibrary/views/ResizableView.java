package net.yazeed44.resizableviewlibrary.views;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by yazeed44 on 10/11/14.
 */
public class ResizableView extends ResizeFrameView {

    public ResizableView(Context context, final ImageView resizeableImage) {
        super(context, resizeableImage);
    }


    public void onResizing(final Rect resizeRect) {
        final ViewGroup.LayoutParams params = this.getLayoutParams();

        params.height = resizeRect.height();
        params.width = resizeRect.width();
        this.setLayoutParams(params);

        Log.d(getClass().getSimpleName() + "  onResizing", "new Height  =  " + params.height + "   ,  new Width  =  " + params.width);

    }

    public void onDraggingBall(final PointF touches) {
        final PointF[] points = new PointF[4];

        // move the balls the same as the finger
        Log.d(getClass().getSimpleName() + "  onDraggingBall", "The ball  " + (ballId + 1) + "   is being dragged");
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


        points[0] = new PointF(newBallOneX, newBallOneY);
        points[1] = new PointF(newBallTwoX, newBallTwoY);
        points[2] = new PointF(newBallThreeX, newBallThreeY);
        points[3] = new PointF(newBallFourX, newBallFourY);

        for (int i = 0; i < resizeBalls.size(); i++) {
            final PointF xy = points[i];
            resizeBalls.get(i).setTranslationX(xy.x);
            resizeBalls.get(i).setTranslationY(xy.y);
        }

        final Rect resizeRect = getRect(points);

        onResizing(resizeRect);


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


}
