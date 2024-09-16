package com.demo.fluid.utils;

import android.content.Context;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CenterZoomLinearLayoutManager extends LinearLayoutManager {
    private float minScale = 0.1f;
    private float percentDiffFromCenter = 0.5f;

    public CenterZoomLinearLayoutManager(Context context) {
        super(context);
    }

    public CenterZoomLinearLayoutManager(Context context, int i, boolean z) {
        super(context, i, z);
    }

    @Override
    public int scrollHorizontallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrollHorizontallyBy = super.scrollHorizontallyBy(i, recycler, state);
        float width = ((float) getWidth()) / 2.0f;
        float f = this.percentDiffFromCenter * width;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            View childAt = getChildAt(i2);
            float min = (((this.minScale * -1.0f) * Math.min(f, Math.abs(width - ((((float) getDecoratedLeft(childAt)) + ((float) getDecoratedRight(childAt))) / 2.0f)))) / f) + 1.0f;
            childAt.setScaleX(min);
            childAt.setScaleY(min);
        }
        return scrollHorizontallyBy;
    }

    @Override
    public int scrollVerticallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrollHorizontallyBy = super.scrollHorizontallyBy(i, recycler, state);
        float width = ((float) getWidth()) / 2.0f;
        float f = this.percentDiffFromCenter * width;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            View childAt = getChildAt(i2);
            float min = (((this.minScale * -1.0f) * Math.min(f, Math.abs(width - ((((float) getDecoratedTop(childAt)) + ((float) getDecoratedBottom(childAt))) / 2.0f)))) / f) + 1.0f;
            childAt.setScaleX(min);
            childAt.setScaleY(min);
        }
        return scrollHorizontallyBy;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        if (getOrientation() == 0) {
            scrollHorizontallyBy(0, recycler, state);
        } else {
            scrollVerticallyBy(0, recycler, state);
        }
    }
}
