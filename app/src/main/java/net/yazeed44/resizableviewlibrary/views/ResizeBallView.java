package net.yazeed44.resizableviewlibrary.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import net.yazeed44.resizableviewlibrary.R;


/**
 * Created by yazeed44 on 10/18/14.
 */
public class ResizeBallView extends ImageView {

    public static int count = 0;
    private int id;

    private String className = getClass().getSimpleName() + "   ";

    public ResizeBallView(final Context context) {
        super(context);
        initializeCircle();
        initializeId();

    }


    private void initializeId() {
        if (count > 4) {
            throw new IllegalStateException("There's should be four balls only !!" + "\n" + id);
        }


        this.id = count++;
    }


    private void initializeCircle() {
        final Drawable resizeBallDrawable = getResources().getDrawable(R.drawable.resize_circle);

        final Bitmap resizeBall = ((BitmapDrawable) resizeBallDrawable).getBitmap();

        setImageBitmap(resizeBall);


    }

    public int getId() {
        return this.id;
    }


}
