package net.yazeed44.resizableviewlibrary;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 5/26/15.
 */
public class AspectRatioRecyclerView extends RecyclerView {

    private ArrayList<AspectRatio> mAspectRatios;

    private AspectRatio mChosenAspectRatio;

    private ResizableViewLayout mResizeLayout;

    public AspectRatioRecyclerView(Context context) {
        super(context);
        init();
    }

    public AspectRatioRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AspectRatioRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        mAspectRatios = ResizeUtil.getResizeFactors(getContext());
        mChosenAspectRatio = mAspectRatios.get(0);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(HORIZONTAL);


        final int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.aspect_ratio_spacing);


        addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        setLayoutManager(gridLayoutManager);
        setAdapter(new AspectRatioAdapter());
        setBackgroundColor(Color.GRAY);


    }

    public void attachToResizeLayout(ResizableViewLayout resizeLayout) {
        mResizeLayout = resizeLayout;
        mResizeLayout.setAspectRatio(mChosenAspectRatio);

    }

    private interface onClickAspectRatio {
        void onClickAspectRatio(View layout);
    }

    private class AspectRatioAdapter extends Adapter<AspectRatioViewHolder> implements onClickAspectRatio {
        @Override
        public AspectRatioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            final View aspectFactorElement = LayoutInflater.from(getContext()).inflate(R.layout.aspect_ratio_element, parent, false);

            return new AspectRatioViewHolder(aspectFactorElement, this);

        }

        @Override
        public void onBindViewHolder(AspectRatioViewHolder viewHolder, int position) {

            final AspectRatio aspectRatio = mAspectRatios.get(position);

            final int width = getResources().getDimensionPixelSize(R.dimen.aspect_ratio_element_width);
            final int height = FrameLayout.LayoutParams.MATCH_PARENT;
            viewHolder.mLayout.setLayoutParams(new RecyclerView.LayoutParams(width, height));

            drawElement(aspectRatio, viewHolder);

            //TODO Draw rectangle as background

        }

        private void drawElement(final AspectRatio aspectRatio, final AspectRatioViewHolder viewHolder) {
            if (isChosen(aspectRatio)) {
                viewHolder.mLayout.setBackgroundColor(getResources().getColor(R.color.chosen_aspect_ratio_background));
                viewHolder.mText.setTextColor(getResources().getColor(R.color.chosen_aspect_ratio_text_color));


            } else {

                viewHolder.mLayout.setBackgroundColor(getResources().getColor(R.color.regular_aspect_ratio_background));
                viewHolder.mText.setTextColor(getResources().getColor(R.color.regular_aspect_ratio_text_color));
            }

            viewHolder.mText.setText(aspectRatio.getName());
        }

        private boolean isChosen(final AspectRatio aspectRatio) {

            return mChosenAspectRatio.equals(aspectRatio);
        }

        @Override
        public int getItemCount() {
            return mAspectRatios.size();
        }

        @Override
        public void onClickAspectRatio(View layout) {
            final int oldAspectRatioPosition = mAspectRatios.indexOf(mChosenAspectRatio);

            final int newAspectRatioPosition = ResizeUtil.getPositionOfChild(layout, R.id.aspect_ratio_layout, AspectRatioRecyclerView.this);
            mChosenAspectRatio = mAspectRatios.get(newAspectRatioPosition);

            notifyItemChanged(oldAspectRatioPosition);
            notifyItemChanged(newAspectRatioPosition);

            mResizeLayout.setAspectRatio(mChosenAspectRatio);

        }
    }

    private class AspectRatioViewHolder extends ViewHolder {

        private LinearLayout mLayout;
        private TextView mText;

        private onClickAspectRatio mListener;

        public AspectRatioViewHolder(View itemView, final onClickAspectRatio listener) {
            super(itemView);

            mText = (TextView) itemView.findViewById(R.id.aspect_ratio_text);
            mLayout = (LinearLayout) itemView.findViewById(R.id.aspect_ratio_layout);
            mListener = listener;

            mLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickAspectRatio(v);
                }
            });
        }
    }
}
