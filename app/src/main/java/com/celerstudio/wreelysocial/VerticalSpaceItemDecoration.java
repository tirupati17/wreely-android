package com.celerstudio.wreelysocial;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;
    private boolean firstItemSpace;

    public VerticalSpaceItemDecoration(int verticalSpaceHeight, boolean firstItemSpace) {
        this.verticalSpaceHeight = verticalSpaceHeight;
        this.firstItemSpace = firstItemSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (firstItemSpace && parent.getChildAdapterPosition(view) == 0) {
            outRect.top = verticalSpaceHeight;
        }
        outRect.bottom = verticalSpaceHeight;

//        if (lastItemSpace && parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
//            outRect.bottom = verticalSpaceHeight;
//        }


    }
}