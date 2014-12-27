package net.yazeed44.resizableviewlibrary;

import android.content.Context;
import android.widget.ImageView;


/**
 * Created by yazeed44 on 10/18/14.
 */
class ResizeShapeView extends ImageView {

    public static int count = 0;
    private int shapeId;

    public ResizeShapeView(final Context context) {
        super(context);
        initId();

    }


    private void initId() {
        if (count > 4) {
            throw new IllegalStateException("There's should be four balls only !!" + "\n" + shapeId);
        }


        this.shapeId = count++;
    }


    public int getShapeId() {
        return this.shapeId;
    }

}
