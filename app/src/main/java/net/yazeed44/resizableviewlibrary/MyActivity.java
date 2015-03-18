package net.yazeed44.resizableviewlibrary;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        final ImageView resizableImage = new ImageView(this);
        resizableImage.setScaleType(ImageView.ScaleType.FIT_XY);
        //  resizableImage.setClickable(false);
        //  resizableImage.setFocusable(false);
        resizableImage.setImageDrawable(getResources().getDrawable(R.drawable.photo_resizeable));

        final ResizableViewLayout view = new ResizableViewLayout(getBaseContext());
        view.setResizableView(resizableImage);
        addContentView(view, new LinearLayout.LayoutParams(400, 400));
        view.setTranslationX(0);
        view.setTranslationY(0);


    }

}
