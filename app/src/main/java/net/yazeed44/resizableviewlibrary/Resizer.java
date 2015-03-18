package net.yazeed44.resizableviewlibrary;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.FrameLayout;

class Resizer {

    public static final String TAG = "Resizer";
    private int mCounter = 0;
    private FrameLayout.LayoutParams mNewParams;
    private ResizableViewLayout mResizableView;

    public Resizer(final ResizableViewLayout resizableView) {
        this.mNewParams = (FrameLayout.LayoutParams) resizableView.getLayoutParams();
        this.mResizableView = resizableView;
    }

    public Resizer width(final int width) {
        mNewParams.width = width;
        Log.d(TAG, "new width  " + mNewParams.width);
        return this;
    }

    public Resizer height(final int height) {
        mNewParams.height = height;
        Log.d(TAG, "new height  " + height);
        return this;
    }

    public Resizer topMargin(final int top) {
        mNewParams.topMargin = top;
        Log.d(TAG, "new top  " + top);
        return this;
    }

    public Resizer leftMargin(final int left) {
        mNewParams.leftMargin = left;
        Log.d(TAG, "new left  " + left);
        return this;
    }

    public Resizer rightMargin(final int right) {
        mNewParams.rightMargin = right;
        Log.d(TAG, "new right  " + right);
        return this;
    }

    public Resizer bottomMargin(final int bottom) {
        mNewParams.bottomMargin = bottom;
        Log.d(TAG, "new bottom  " + bottom);
        return this;
    }

    private Point getScreenSize() {
        final Display display = ((WindowManager) mResizableView.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        return size;
    }

    public void resize() {

        if (mCounter > 0) {
            throw new IllegalStateException("It's resized already !!");

        }


        mCounter++;

        final int shapeWidth = mResizableView.getResources().getDimensionPixelSize(R.dimen.shape_width);
        final int shapeHeight = mResizableView.getResources().getDimensionPixelSize(R.dimen.shape_height);

        final int maxWidth = getMaxWidth() - shapeWidth;
        final int maxHeight = getMaxHeight() - shapeHeight;

        if (mNewParams.width > maxWidth) {
            mNewParams.width = maxWidth;
        }

        if (mNewParams.height > maxHeight) {
            mNewParams.height = maxHeight;
        }


        mResizableView.setLayoutParams(mNewParams);

    }

    private int getMaxWidth() {
        final Point screenSize = getScreenSize();

        return screenSize.x;

    }

    private int getMaxHeight() {
        final Point screenSize = getScreenSize();

        return screenSize.y;
    }

}