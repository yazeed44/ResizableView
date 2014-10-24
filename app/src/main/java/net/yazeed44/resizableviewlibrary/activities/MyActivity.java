package net.yazeed44.resizableviewlibrary.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.yazeed44.resizableviewlibrary.R;
import net.yazeed44.resizableviewlibrary.views.ResizableView;

public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        //final Bitmap testBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.photo_resizeable)).getBitmap();
        final ImageView resizableImage = new ImageView(this);
        resizableImage.setBackgroundColor(Color.BLACK);


        final ResizableView view = new ResizableView(getBaseContext(), resizableImage);
        addContentView(view, new LinearLayout.LayoutParams(700, 700));
        view.setTranslationX(300);
        view.setTranslationY(800);


    }

}
