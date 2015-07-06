package net.yazeed44.resizableviewlibrary;

import android.content.Context;
import android.widget.ImageView;


/**
 * Created by yazeed44 on 10/18/14.
 */
class ResizeShapeView extends ImageView {

    private int shapeId;

    public ResizeShapeView(final Context context) {
        super(context);

    }


   public void setShapeId(int id) {
       this.shapeId = id;
   }


    public int getShapeId() {
        return this.shapeId;
    }

}
