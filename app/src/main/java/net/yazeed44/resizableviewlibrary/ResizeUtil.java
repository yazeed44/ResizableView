package net.yazeed44.resizableviewlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 5/3/15.
 */
public final class ResizeUtil {

    private ResizeUtil() {
        throw new AssertionError();
    }


    public static ArrayList<AspectRatio> getDefaultAspectRatios(final Context context) {

        final ArrayList<AspectRatio> aspectRatios = new ArrayList<>();


        final AspectRatio free = new AspectRatio();

        free.setName(context.getResources().getString(R.string.free_aspect_ratio_name));
        aspectRatios.add(free);

        aspectRatios.add(new AspectRatio(1, 1));
        aspectRatios.add(new AspectRatio(4, 3));
        aspectRatios.add(new AspectRatio(3, 4));
        aspectRatios.add(new AspectRatio(3, 2));
        aspectRatios.add(new AspectRatio(16, 9));


        return aspectRatios;
    }

    public static int getPositionOfChild(final View child, final int childParentId, final RecyclerView recyclerView) {

        if (child.getId() == childParentId) {
            return recyclerView.getChildPosition(child);
        }


        View parent = (View) child.getParent();
        while (parent.getId() != childParentId) {
            parent = (View) parent.getParent();
        }
        return recyclerView.getChildPosition(parent);
    }

    public static Bitmap castToBitmap(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        final Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /*
        public static Point getScreenDimension(final Context context) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            final Point dimension = new Point();
            display.getSize(dimension);
            return dimension;
        }
    */
    public static void handleDispatchTouchEvent(final MotionEvent event, final ResizableViewLayout resizableViewLayout) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            resizableViewLayout.setFrameVisibility(isTouchEventContained(event, resizableViewLayout));
        }
    }

    private static boolean isTouchEventContained(final MotionEvent event, final ResizableViewLayout resizableViewLayout) {

        final Rect viewRect = new Rect();
        resizableViewLayout.getGlobalVisibleRect(viewRect);

        return viewRect.contains(Math.round(event.getRawX()), Math.round(event.getRawY()));

    }

    public static Point getAnglePoint(Point O, PointF A, float angle) {

        int x, y;
        float dOA = getDistance(O, A);
        double p1 = angle * Math.PI / 180f;
        double p2 = Math.acos((A.x - O.x) / dOA);
        x = (int) (O.x + dOA * Math.cos(p1 + p2));

        double p3 = Math.acos((A.x - O.x) / dOA);
        y = (int) (O.y + dOA * Math.sin(p1 + p3));
        return new Point(x, y);
    }

    public static float getDistance(Point a, PointF b) {
        float v = ((a.x - b.x) * (a.x - b.x)) + ((a.y - b.y) * (a.y - b.y));
        return (float) (((Math.sqrt(v))));
    }
}
