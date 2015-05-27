package net.yazeed44.resizableviewlibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 5/3/15.
 */
public final class ResizeUtil {

    private ResizeUtil() {
        throw new AssertionError();
    }


    public static ArrayList<ResizeFactor> getResizeFactors(final Context context) {

        final ArrayList<ResizeFactor> resizeFactors = new ArrayList<>();


        final ResizeFactor free = new ResizeFactor();

        free.setName(context.getResources().getString(R.string.free_resize_factor_name));
        resizeFactors.add(free);

        resizeFactors.add(new ResizeFactor(1, 1));
        resizeFactors.add(new ResizeFactor(4, 3));
        resizeFactors.add(new ResizeFactor(3, 4));
        resizeFactors.add(new ResizeFactor(3, 2));
        resizeFactors.add(new ResizeFactor(16, 9));


        return resizeFactors;
    }

    public static int getPositionOfChild(final View child, final int childParentId, final RecyclerView recyclerView) {

        if (child.getId() == childParentId) {
            return recyclerView.getChildPosition(child);
        }


        View parent = (View) child.getParent();
        while (parent.getId() != childParentId) {
            parent = (View) parent.getParent();
        }
        return recyclerView.getChildPosition(parent);
    }
}
