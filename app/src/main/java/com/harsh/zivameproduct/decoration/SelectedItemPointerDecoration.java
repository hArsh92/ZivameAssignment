package com.harsh.zivameproduct.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.harsh.zivameproduct.R;

public class SelectedItemPointerDecoration extends RecyclerView.ItemDecoration {

    private Drawable mPointer;
    private int drawableWidth, drawableHeight;

    public SelectedItemPointerDecoration(Context context) {
        this.mPointer = ContextCompat.getDrawable(context, R.drawable.ic_triangle);
        if (mPointer != null) {
            drawableWidth = mPointer.getIntrinsicWidth();
            drawableHeight = mPointer.getIntrinsicHeight();
        }
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null || mPointer == null) {
            return;
        }

        canvas.save();

        final int childCount = parent.getChildCount();
        for (int index = 0; index < childCount - 1; index++) {
            final View child = parent.getChildAt(index);
            if (child.isSelected()) {
                int centerHorizontal = child.getLeft() + (child.getRight() - child.getLeft()) / 2;

                final int right = centerHorizontal + (drawableWidth / 2);
                final int bottom = child.getBottom() + drawableHeight;
                final int top = child.getBottom();
                final int left = centerHorizontal - (drawableWidth / 2);

                mPointer.setBounds(left, top, right, bottom);
                mPointer.draw(canvas);
            }
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (mPointer == null) {
            outRect.set(0, 0, 0, 0);
            return;
        }

        outRect.set(0, 0, drawableWidth, drawableHeight);
    }
}
