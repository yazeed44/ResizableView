package net.yazeed44.resizableviewlibrary;

import android.graphics.Point;

/**
 * Created by yazeed44 on 5/26/15.
 */
public class ResizeFactor {

    private int mWidthFactor = -1;
    private int mHeightFactor = -1;
    private String mName = "";

    public ResizeFactor(final int widthFactor, final int heightFactor) {
        mWidthFactor = widthFactor;
        mHeightFactor = heightFactor;
        mName = mWidthFactor + ":" + mHeightFactor;

    }

    public ResizeFactor() {

    }

    public String getName() {
        return mName;
    }

    public void setName(final String name) {
        mName = name;
    }

    public Point calculateDimension(final Point baseDimension) {

        //TODO Calculate dimension
        return baseDimension;
    }

    public Point calculateDimension(final int baseWidth, final int baseHeight) {

        return calculateDimension(new Point(baseWidth, baseHeight));
    }

    @Override
    public boolean equals(Object o) {

        return o instanceof ResizeFactor && getName().equals(((ResizeFactor) o).getName());

    }
}
