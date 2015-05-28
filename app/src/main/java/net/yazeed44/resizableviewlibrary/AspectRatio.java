package net.yazeed44.resizableviewlibrary;

import android.graphics.Point;

/**
 * Created by yazeed44 on 5/26/15.
 */
public class AspectRatio {

    private int mWidthRatio = -1;
    private int mHeightRatio = -1;
    private String mName = "";

    public AspectRatio(final int widthRatio, final int heightRatio) {
        mWidthRatio = widthRatio;
        mHeightRatio = heightRatio;
        mName = mWidthRatio + ":" + mHeightRatio;

    }

    public AspectRatio() {

    }

    public String getName() {
        return mName;
    }

    public void setName(final String name) {
        mName = name;
    }

    public Point calculateDimension(final Point baseDimension, final Point newDimension) {


        if (mWidthRatio == -1 && mHeightRatio == -1) {
            return newDimension;
        }


        if (baseDimension.x != newDimension.x) {
            //The width is changing values
            final int newHeight = Math.round((float) newDimension.x / getRatio());
            return new Point(newDimension.x, newHeight);
        } else {
            //The height is changing values
            final int newWidth = Math.round((float) newDimension.y * getRatio());
            return new Point(newWidth, newDimension.y);
        }



    }

    public Point calculateDimension(final int baseWidth, final int baseHeight, final int newWidth, final int newHeight) {

        return calculateDimension(new Point(baseWidth, baseHeight), new Point(newWidth, newHeight));
    }

    @Override
    public boolean equals(Object o) {

        return o instanceof AspectRatio && getName().equals(((AspectRatio) o).getName());

    }

    public float getRatio() {
        return (float) mWidthRatio / (float) mHeightRatio;
    }

    public int getWidthRatio() {
        return mWidthRatio;
    }

    public int getHeightRatio() {
        return mHeightRatio;
    }
}
