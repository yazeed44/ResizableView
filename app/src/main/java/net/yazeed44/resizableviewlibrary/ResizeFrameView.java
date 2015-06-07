package net.yazeed44.resizableviewlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;


/**
 * Created by yazeed44 on 10/11/14.
 */
abstract class ResizeFrameView extends FrameLayout {

    protected ArrayList<ResizeShapeView> mResizeShapes = new ArrayList<>(4);
    // array that holds the balls
    protected int mShapeId = 0;
    protected View mResizableView;
    protected ImageView mStretchView;
    protected ImageView mRotateView;
    protected boolean mShouldDrawFrame = true;
    private Bitmap mResizeShapeBitmap;
    private Paint mFramePaint;

    public ResizeFrameView(final Context context) {
        super(context);
    }

    public ResizeFrameView(final Context context, AttributeSet set) {
        super(context, set);
    }

    public ResizeFrameView(final Context context, AttributeSet set, int defStyle) {
        super(context, set, defStyle);
    }

    public void setResizableView(final View resizableView) {
        initFrame(resizableView);
    }

    private void initFrame(final View resizableView) {
        this.mResizableView = resizableView;
        mResizableView.setOnTouchListener(createResizableViewListener());

        setWillNotDraw(false);
        initShapeBitmap();
        initPaint();
        addResizableViewToLayout();

    }

    private void initPaint() {
        mFramePaint = new Paint();
        mFramePaint.setColor(getResources().getColor(R.color.frame_background_line));
        //mFramePaint.setAlpha(100);
        mFramePaint.setAntiAlias(true);
        mFramePaint.setDither(true);
        mFramePaint.setStyle(Paint.Style.STROKE);
        mFramePaint.setStrokeWidth(5.5f);
        mFramePaint.setStrokeCap(Paint.Cap.SQUARE);
    }

    private void addResizableViewToLayout() {


        final LinearLayout nestedFrameLayout = new LinearLayout(getContext());
        nestedFrameLayout.setOrientation(LinearLayout.VERTICAL);
        final LayoutParams nestedFrameLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        final int horizontalMargin = mResizeShapeBitmap.getWidth();
        final int verticalMargin = mResizeShapeBitmap.getHeight();
        nestedFrameLayoutParams.setMargins(horizontalMargin, verticalMargin, horizontalMargin, verticalMargin);
        addView(nestedFrameLayout, nestedFrameLayoutParams);


        final LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mResizableView.setLayoutParams(imageParams);

        nestedFrameLayout.addView(mResizableView, imageParams);

    }

    private void initShapeBitmap() {

        final Drawable resizeBallDrawable = getResources().getDrawable(R.drawable.resize_shape);

        mResizeShapeBitmap = ResizeUtil.castToBitmap(resizeBallDrawable);
    }

    private void initStretchView() {
        if (mStretchView != null) {
            return;
        }

        mStretchView = new ImageView(getContext());
        mStretchView.setImageResource(R.drawable.ic_stretch);
        mStretchView.setBackgroundResource(R.drawable.stretch_shape_background);
        mStretchView.setOnTouchListener(createStretchListener());

        final int stretchGravity = Gravity.RIGHT | Gravity.BOTTOM;
        final LayoutParams stretchViewLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, stretchGravity);

        addView(mStretchView, stretchViewLayoutParams);
    }

    private void initShapes() {

        if (!mResizeShapes.isEmpty()) {
            return; // No need to initialize
        }

        attachShapes();


    }

    private void initRotateView() {

        if (mRotateView != null) {
            return;
        }

        mRotateView = new ImageView(getContext());
        mRotateView.setImageResource(R.drawable.ic_rotate);
        mRotateView.setBackgroundResource(R.drawable.stretch_shape_background);
        mRotateView.setOnTouchListener(createRotateListener());

        final int rotateViewGravity = Gravity.RIGHT | Gravity.TOP;
        final LayoutParams rotateViewLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, rotateViewGravity);

        addView(mRotateView, rotateViewLayoutParams);

    }

    private void attachShapes() {

        final int[] positions = new int[4];

        positions[0] = Gravity.CENTER_VERTICAL | Gravity.LEFT;
        positions[1] = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        positions[2] = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        positions[3] = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;

        final int width = mResizeShapeBitmap.getWidth(), height = mResizeShapeBitmap.getHeight();


        for (final int position : positions) {
            final LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = position;

            final ResizeShapeView resizeShapeView = new ResizeShapeView(getContext());
            resizeShapeView.setImageBitmap(mResizeShapeBitmap);
            resizeShapeView.setOnTouchListener(createShapesListener());
            resizeShapeView.setFocusable(true);
            resizeShapeView.setClickable(true);
            resizeShapeView.setPadding(width / 2, height / 2, width / 2, height / 2);
            addView(resizeShapeView, params);

            mResizeShapes.add(resizeShapeView);

        }
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initShapes();
        initStretchView();
        initRotateView();
    }

    public void setFrameVisibility(final boolean frameVisibility) {
        mShouldDrawFrame = frameVisibility;
        redrawFrame();
    }

    private void redrawFrame() {
        invalidate();

        if (mShouldDrawFrame) {
            showResizeShapes();
            mRotateView.setVisibility(VISIBLE);
            mStretchView.setVisibility(VISIBLE);

        } else {
            hideResizeShapes();
            mRotateView.setVisibility(INVISIBLE);
            mStretchView.setVisibility(INVISIBLE);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!mShouldDrawFrame) {
            return;
        }


        final View resizableViewParent = (View) mResizableView.getParent();


        canvas.drawRect(resizableViewParent.getLeft(), resizableViewParent.getTop(), resizableViewParent.getRight(), resizableViewParent.getBottom(), mFramePaint);


    }

    private void hideResizeShapes() {

        for (final ResizeShapeView view : mResizeShapes) {
            view.setVisibility(INVISIBLE);
        }
    }

    private void showResizeShapes() {
        for (final ResizeShapeView view : mResizeShapes) {
            view.setVisibility(VISIBLE);
        }
    }

    public abstract OnTouchListener createShapesListener();

    public abstract OnTouchListener createResizableViewListener();

    public abstract OnTouchListener createStretchListener();

    public abstract OnTouchListener createRotateListener();

}
