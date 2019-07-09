package com.example.myframework.ui.adapter.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 98733 on 2018/8/14.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int mLeft;
    private int mRight;
    private int mTop;
    private int mBottom;

    /**
     * Retrieve any offsets for the given item. Each field of <code>outRect</code> specifies
     * the number of pixels that the item view should be inset by, similar to padding or margin.
     * The default implementation sets the bounds of outRect to 0 and returns.
     * <p>
     * <p>
     * If this ItemDecoration does not affect the positioning of item views, it should set
     * all four fields of <code>outRect</code> (left, top, right, bottom) to zero
     * before returning.
     * <p>
     * <p>
     * If you need to access Adapter for additional data, you can call
     * {@link RecyclerView#getChildAdapterPosition(View)} to get the adapter position of the
     * View.
     *
     * @param outRect Rect to receive the output.
     * @param view    The child view to decorate
     * @param parent  RecyclerView this ItemDecoration is decorating
     * @param state   The current state of RecyclerView.
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = mLeft;
        outRect.right = mRight;
        outRect.bottom = mBottom;
       /* if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = mSpace;
        }*/

    }

    public SpaceItemDecoration(int mLeft, int mRight, int mBottom) {
        this.mLeft = mLeft;
        this.mRight = mRight;
        this.mBottom = mBottom;
    }

    public SpaceItemDecoration(int mLeft) {
        this.mLeft = mLeft;
        this.mRight = mLeft;
        this.mBottom = mLeft;
    }
}
