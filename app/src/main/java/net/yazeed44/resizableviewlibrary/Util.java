package net.yazeed44.resizableviewlibrary;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by yazeed44 on 5/3/15.
 */
public final class Util {

    private Util() {
        throw new AssertionError();
    }

    public static Point getScreenDimension(final Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        final Point dimension = new Point();

        display.getSize(dimension);

        return dimension;
    }

}
