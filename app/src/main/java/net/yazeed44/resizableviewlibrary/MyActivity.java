package net.yazeed44.resizableviewlibrary;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyActivity extends Activity {


    private ResizableViewLayout mResizeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        final ImageView resizableImage = new ImageView(this);
        resizableImage.setImageResource(R.drawable.photo_resizeable);
        resizableImage.setScaleType(ImageView.ScaleType.FIT_XY);


        mResizeLayout = new ResizableViewLayout(getBaseContext());

        mResizeLayout.setResizableView(resizableImage);

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(600, 600);
        addContentView(mResizeLayout, params);

        mResizeLayout.setLayoutParams(new FrameLayout.LayoutParams(600, 600));
        mResizeLayout.setTranslationX(400);
        mResizeLayout.setTranslationY(600);


        final AspectRatioRecyclerView aspectRatioRecyclerView = (AspectRatioRecyclerView) findViewById(R.id.resize_factor_recycler);
        aspectRatioRecyclerView.attachToResizeLayout(mResizeLayout);


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {


        ResizableViewLayout.handleDispatchTouchEvent(ev, mResizeLayout);
        return super.dispatchTouchEvent(ev);
    }


}
