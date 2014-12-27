package net.yazeed44.resizableviewlibrary;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.FrameLayout;

class Resizer {


    public static final double RESIZE_FACTOR = 1.34;

    private int mCounter = 0;
    private FrameLayout.LayoutParams mNewParams;
    private ResizableImageView resizableView;

    public Resizer(final ResizableImageView resizableView) {
        this.mNewParams = (FrameLayout.LayoutParams) resizableView.getLayoutParams();
        this.resizableView = resizableView;
    }

    public Resizer width(final int width) {
        mNewParams.width = width;
        return this;
    }

    public Resizer height(final int height) {
        mNewParams.height = height;
        return this;
    }

    public Resizer topMargin(final int top) {
        mNewParams.topMargin = top;
        return this;
    }

    public Resizer leftMargin(final int left) {
        mNewParams.leftMargin = left;
        return this;
    }

    public Resizer rightMargin(final int right) {
        mNewParams.rightMargin = right;
        return this;
    }

    public Resizer bottomMargin(final int bottom) {
        mNewParams.bottomMargin = bottom;
        return this;
    }

    private Point getScreenSize() {
        final Display display = ((WindowManager) resizableView.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        return size;
    }

    public void resize() {

        if (mCounter > 0) {
            throw new IllegalStateException("It's resized already !!");

        }


        mCounter++;

        final int maxWidth = getMaxWidth();
        final int maxHeight = getMaxHeight();

        if (mNewParams.width > maxWidth) {
            mNewParams.width = maxWidth;
        }

        if (mNewParams.height > maxHeight) {
            mNewParams.height = maxHeight;
        }


        //  mNewParams.width /= RESIZE_FACTOR;


        Log.d("resize", "new Right margin   " + mNewParams.rightMargin);
        Log.d("resize", "new left margin    " + mNewParams.leftMargin);
        Log.d("resize", "new width  " + mNewParams.width);

        resizableView.setLayoutParams(mNewParams);

    }

    private int getMaxWidth() {
        final Point screenSize = getScreenSize();

        return screenSize.x - 45;

    }

    private int getMaxHeight() {
        final Point screenSize = getScreenSize();

        return screenSize.y - 45;
    }

}