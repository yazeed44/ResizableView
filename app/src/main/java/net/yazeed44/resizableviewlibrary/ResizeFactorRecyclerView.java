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
public class ResizeFactorRecyclerView extends RecyclerView {

    private ArrayList<ResizeFactor> mResizeFactors;

    private ResizeFactor mChosenResizeFactor;

    private float mResizeFactorElementWidth;

    public ResizeFactorRecyclerView(Context context) {
        super(context);
        init();
    }

    public ResizeFactorRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ResizeFactorRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        mResizeFactors = ResizeUtil.getResizeFactors(getContext());
        mChosenResizeFactor = mResizeFactors.get(0);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(HORIZONTAL);


        final int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.resize_factor_spacing);


        addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        setLayoutManager(gridLayoutManager);
        setAdapter(new ResizeFactorAdapter());
        setBackgroundColor(Color.GRAY);


    }

    private interface OnClickResizeFactor {
        void onClickResizeFactor(View layout);
    }

    private class ResizeFactorAdapter extends Adapter<ResizeFactorViewHolder> implements OnClickResizeFactor {
        @Override
        public ResizeFactorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            final View resizeFactorElement = LayoutInflater.from(getContext()).inflate(R.layout.resize_factor_element, parent, false);

            return new ResizeFactorViewHolder(resizeFactorElement, this);

        }

        @Override
        public void onBindViewHolder(ResizeFactorViewHolder viewHolder, int position) {

            final ResizeFactor resizeFactor = mResizeFactors.get(position);

            final int width = getResources().getDimensionPixelSize(R.dimen.resize_factor_element_width);
            final int height = FrameLayout.LayoutParams.MATCH_PARENT;
            viewHolder.mLayout.setLayoutParams(new RecyclerView.LayoutParams(width, height));

            drawElement(resizeFactor, viewHolder);


        }

        private void drawElement(final ResizeFactor resizeFactor, final ResizeFactorViewHolder viewHolder) {
            if (isChosen(resizeFactor)) {
                viewHolder.mLayout.setBackgroundColor(getResources().getColor(R.color.chosen_resize_factor_background));
                viewHolder.mText.setTextColor(getResources().getColor(R.color.chosen_resize_factor_text_color));


            } else {

                viewHolder.mLayout.setBackgroundColor(getResources().getColor(R.color.regular_resize_factor_background));
                viewHolder.mText.setTextColor(getResources().getColor(R.color.regular_resize_factor_text_color));
            }

            viewHolder.mText.setText(resizeFactor.getName());
        }

        private boolean isChosen(final ResizeFactor resizeFactor) {

            return mChosenResizeFactor.equals(resizeFactor);
        }

        @Override
        public int getItemCount() {
            return mResizeFactors.size();
        }

        @Override
        public void onClickResizeFactor(View layout) {
            final int oldResizeFactorPosition = mResizeFactors.indexOf(mChosenResizeFactor);

            final int newResizeFactorPosition = ResizeUtil.getPositionOfChild(layout, R.id.resize_factor_layout, ResizeFactorRecyclerView.this);
            mChosenResizeFactor = mResizeFactors.get(newResizeFactorPosition);

            notifyItemChanged(oldResizeFactorPosition);
            notifyItemChanged(newResizeFactorPosition);

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
