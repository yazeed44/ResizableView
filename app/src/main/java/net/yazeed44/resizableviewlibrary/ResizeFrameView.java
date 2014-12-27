package net.yazeed44.resizableviewlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;


/**
 * Created by yazeed44 on 10/11/14.
 */
abstract class ResizeFrameView extends FrameLayout {


    int groupId = -1;
    ArrayList<ResizeShapeView> resizeShapes = new ArrayList<>();
    // array that holds the balls
    int shapeId = 0;
    ImageView resizableImageView;
    String className = getClass().getSimpleName() + "   ";
    private Bitmap mResizeShapeBitmap;

    public ResizeFrameView(final Context context) {
        super(context);
    }

    public ResizeFrameView(final Context context, AttributeSet set) {
        super(context, set);
    }

    public ResizeFrameView(final Context context, AttributeSet set, int defStyle) {
        super(context, set, defStyle);
    }


    public void setImage(final ImageView resizableImage) {
        initializeFrame(resizableImage);
    }

    private void initializeFrame(final ImageView resizableImage) {
        this.resizableImageView = resizableImage;


        final LayoutParams imageParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        addView(resizableImageView, imageParams);

        initShapeBitmap();


        //  setFocusable(true);


    }


    private void initShapeBitmap() {

        final Drawable resizeBallDrawable = getResources().getDrawable(R.drawable.resize_shape);

        mResizeShapeBitmap = castToBitmap(resizeBallDrawable);
    }

    private Bitmap castToBitmap(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    private void initShapes() {

        if (!resizeShapes.isEmpty()) {
            return; // No need to initialize
        }

        attachShapes();

        createShapesListener();


    }


    private void attachShapes() {

        final int[] positions = new int[4];

        positions[0] = Gravity.CENTER | Gravity.LEFT;
        positions[1] = Gravity.CENTER | Gravity.TOP;
        positions[2] = Gravity.CENTER | Gravity.RIGHT;
        positions[3] = Gravity.CENTER | Gravity.BOTTOM;

        final int width = mResizeShapeBitmap.getWidth(), height = mResizeShapeBitmap.getHeight();

        for (int position : positions) {
            final LayoutParams params = new LayoutParams(width, height, position);

            final ResizeShapeView resizeShapeView = new ResizeShapeView(getContext());
            resizeShapeView.setImageBitmap(mResizeShapeBitmap);
            resizeShapeView.setOnTouchListener(createShapesListener());
            resizeShapeView.setFocusable(true);
            addView(resizeShapeView, params);


            resizeShapes.add(resizeShapeView);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initShapes();
    }

    public abstract OnTouchListener createShapesListener();

}
