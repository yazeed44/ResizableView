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
        final int padding = 10;
        resizableImage.setPadding(padding, padding, padding, padding);
        resizableImage.setImageDrawable(getResources().getDrawable(R.drawable.photo_resizeable));


        final ResizableImageView view = new ResizableImageView(getBaseContext());
        view.setImage(resizableImage);

        addContentView(view, new LinearLayout.LayoutParams(400, 400));
        view.setTranslationX(0);
        view.setTranslationY(0);


    }

}
