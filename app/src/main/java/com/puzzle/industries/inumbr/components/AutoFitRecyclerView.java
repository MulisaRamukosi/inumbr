package com.puzzle.industries.inumbr.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AutoFitRecyclerView extends RecyclerView {

    private int columnWidth = -1;
    private GridLayoutManager gridLayoutManager;

    public AutoFitRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public AutoFitRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AutoFitRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArr = {android.R.attr.columnWidth};
            TypedArray a = context.obtainStyledAttributes(attrs, attrsArr);
            columnWidth = a.getDimensionPixelSize(0, -1);
            a.recycle();
        }

        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (columnWidth > 0) {
            final int spanCount = Math.max(1, getMeasuredWidth() / columnWidth);
            gridLayoutManager.setSpanCount(spanCount);
        }
    }
}

