package yazeed44.net.resizeableviewlibary;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

/**
 * Created by yazeed44 on 10/11/14.
 */
public class ResizeableView extends ResizeFrameView {

    public ResizeableView(Context context){
        super(context);
    }
    public ResizeableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ResizeableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void onResizing(final Rect resizeRect) {
        final ViewGroup.LayoutParams params = this.getLayoutParams();

        params.height = resizeRect.height();
        params.width = resizeRect.width();
        this.setLayoutParams(params);
        Log.d(getClass().getSimpleName() + "  onResizing", "new Height  =  " + params.height + "   ,  new Width  =  " + params.width);

    }

    public void onDraggingBall(final Point touches) {

        draggingFrame = false;
        // move the balls the same as the finger
        resizeBalls.get(ballId).setX(touches.x);
        resizeBalls.get(ballId).setY(touches.y);

        paint.setColor(Color.CYAN);
        if (groupId == 1) {
            resizeBalls.get(1).setX(resizeBalls.get(0).getX());
            resizeBalls.get(1).setY(resizeBalls.get(2).getY());
            resizeBalls.get(3).setX(resizeBalls.get(2).getX());
            resizeBalls.get(3).setY(resizeBalls.get(0).getY());
        } else {
            resizeBalls.get(0).setX(resizeBalls.get(1).getX());
            resizeBalls.get(0).setY(resizeBalls.get(3).getY());
            resizeBalls.get(2).setX(resizeBalls.get(3).getX());
            resizeBalls.get(2).setY(resizeBalls.get(1).getY());
        }

        invalidate();
    }


    public void onDraggingRect(final Point touches) {

        //TEMP , the drag XY isn't complete
        //TODO
        final int dragX = (touches.x - resizeRect.width() / 2);

        final int dragY = (touches.y - resizeRect.height() / 2);

        resizeRect.offsetTo(dragX, dragY);


        if (!isFrameInverted()) {
            resizeBalls.get(0).setX(resizeRect.left);
            resizeBalls.get(0).setY(resizeRect.top);

            resizeBalls.get(1).setX(resizeRect.left);
            resizeBalls.get(1).setY(resizeRect.bottom);

            resizeBalls.get(2).setX(resizeRect.right);
            resizeBalls.get(2).setY(resizeRect.bottom);

            resizeBalls.get(3).setX(resizeRect.right);
            resizeBalls.get(3).setY(resizeRect.top);
        } else {
            //It's the same with above
            //just changed Y positions

            resizeBalls.get(0).setX(resizeRect.left);
            resizeBalls.get(0).setY(resizeRect.bottom);

            resizeBalls.get(1).setX(resizeRect.left);
            resizeBalls.get(1).setY(resizeRect.top);

            resizeBalls.get(2).setX(resizeRect.right);
            resizeBalls.get(2).setY(resizeRect.top);

            resizeBalls.get(3).setX(resizeRect.right);
            resizeBalls.get(3).setY(resizeRect.bottom);
        }

        setX(dragX);
        setY(dragY);
    }


}
