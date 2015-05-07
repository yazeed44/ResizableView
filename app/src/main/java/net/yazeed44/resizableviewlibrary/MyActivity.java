package net.yazeed44.resizableviewlibrary;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        final ImageView resizableImage = new ImageView(this);
        resizableImage.setImageResource(R.drawable.photo_resizeable);
        resizableImage.setScaleType(ImageView.ScaleType.FIT_XY);
        resizableImage.setBackgroundColor(Color.WHITE);



        final ResizableViewLayout view = new ResizableViewLayout(getBaseContext());
        view.setClipChildren(false);
        view.setMinimumWidth(600);
        view.setMinimumHeight(600);
        view.setResizableView(resizableImage);

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(600, 600, 1);
        addContentView(view, params);
        //view.setTranslationX(400);
        //view.setTranslationY(600);


    }

}
