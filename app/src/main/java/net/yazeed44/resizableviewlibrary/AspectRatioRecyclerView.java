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
        setAdapter(new ResizeFactorAdapter());
        setBackgroundColor(Color.GRAY);


    }

    public void attachToResizeLayout(ResizableViewLayout resizeLayout) {
        mResizeLayout = resizeLayout;
        mResizeLayout.setAspectRatio(mChosenAspectRatio);

    }

    private interface OnClickResizeFactor {
        void onClickResizeFactor(View layout);
    }

    private class ResizeFactorAdapter extends Adapter<ResizeFactorViewHolder> implements OnClickResizeFactor {
        @Override
        public ResizeFactorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            final View resizeFactorElement = LayoutInflater.from(getContext()).inflate(R.layout.aspect_ratio_element, parent, false);

            return new ResizeFactorViewHolder(resizeFactorElement, this);

        }

        @Override
        public void onBindViewHolder(ResizeFactorViewHolder viewHolder, int position) {

            final AspectRatio aspectRatio = mAspectRatios.get(position);

            final int width = getResources().getDimensionPixelSize(R.dimen.aspect_ratio_element_width);
            final int height = FrameLayout.LayoutParams.MATCH_PARENT;
            viewHolder.mLayout.setLayoutParams(new RecyclerView.LayoutParams(width, height));

            drawElement(aspectRatio, viewHolder);


        }

        private void drawElement(final AspectRatio aspectRatio, final ResizeFactorViewHolder viewHolder) {
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
        public void onClickResizeFactor(View layout) {
            final int oldAspectRatioPosition = mAspectRatios.indexOf(mChosenAspectRatio);

            final int newAspectRatioPosition = ResizeUtil.getPositionOfChild(layout, R.id.resize_factor_layout, AspectRatioRecyclerView.this);
            mChosenAspectRatio = mAspectRatios.get(newAspectRatioPosition);

            notifyItemChanged(oldAspectRatioPosition);
            notifyItemChanged(newAspectRatioPosition);

            mResizeLayout.setAspectRatio(mChosenAspectRatio);

        }
    }

    private class ResizeFactorViewHolder extends ViewHolder {

        private LinearLayout mLayout;
        private TextView mText;

        private OnClickResizeFactor mListener;

        public ResizeFactorViewHolder(View itemView, final OnClickResizeFactor listener) {
            super(itemView);

            mText = (TextView) itemView.findViewById(R.id.resize_factor_text);
            mLayout = (LinearLayout) itemView.findViewById(R.id.resize_factor_layout);
            mListener = listener;

            mLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickResizeFactor(v);
                }
            });
        }
    }
}
