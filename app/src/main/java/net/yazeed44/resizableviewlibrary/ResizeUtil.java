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


    public static ArrayList<AspectRatio> getResizeFactors(final Context context) {

        final ArrayList<AspectRatio> aspectRatios = new ArrayList<>();


        final AspectRatio free = new AspectRatio();

        free.setName(context.getResources().getString(R.string.free_aspect_ratio_name));
        aspectRatios.add(free);

        aspectRatios.add(new AspectRatio(1, 1));
        aspectRatios.add(new AspectRatio(4, 3));
        aspectRatios.add(new AspectRatio(3, 4));
        aspectRatios.add(new AspectRatio(3, 2));
        aspectRatios.add(new AspectRatio(16, 9));


        return aspectRatios;
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
