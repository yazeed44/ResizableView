package net.yazeed44.resizeableviewlibary.views;

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

        // move the balls the same as the finger
        resizeBalls.get(ballId).setX(touches.x);
        resizeBalls.get(ballId).setY(touches.y);

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


    }


}
