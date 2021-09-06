/* (c) Disney. All rights reserved. */
package com.example.video_image_banner;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * Created by Jack Ke on 2021/9/1
 */
public class HorizontalGridView extends GridView {
    private static final String TAG = "AutoGridView";
    private int previousFirstVisible;
    private int numColumns = 1;

    public HorizontalGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HorizontalGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalGridView(Context context) {
        super(context);
    }


    @Override
    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        super.setNumColumns(numColumns);
        Log.d(TAG, "setSelection --> " + previousFirstVisible);
        setSelection(previousFirstVisible);
    }

    @Override
    protected void onLayout(boolean changed, int leftPos, int topPos, int rightPos, int bottomPos) {
        super.onLayout(changed, leftPos, topPos, rightPos, bottomPos);
        setHeights();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        setNumColumns(this.numColumns);
    }

    @Override
    protected void onScrollChanged(int newHorizontal, int newVertical, int oldHorizontal, int oldVertical) {
        // Check if the first visible position has changed due to this scroll
        int firstVisible = getFirstVisiblePosition();
        if (previousFirstVisible != firstVisible) {
            // Update position, and update heights
            previousFirstVisible = firstVisible;
            setHeights();
        }

        super.onScrollChanged(newHorizontal, newVertical, oldHorizontal, oldVertical);
    }

    /**
     * Sets the height of each view in a row equal to the height of the tallest view in this row.
     */
    private void setHeights() {
        ListAdapter adapter = getAdapter();
        if (adapter != null) {
            for (int i = 0; i < getChildCount(); i += numColumns) {
                // Determine the maximum height for this row
                int maxHeight = 0;
                for (int j = i; j < i + numColumns; j++) {
                    View view = getChildAt(j);
                    if (view != null && view.getHeight() > maxHeight) {
                        maxHeight = view.getHeight();
                    }
                }
                // Set max height for each element in this row
                if (maxHeight > 0) {
                    for (int j = i; j < i + numColumns; j++) {
                        View view = getChildAt(j);
                        if (view != null && view.getHeight() != maxHeight) {
                            view.setMinimumHeight(maxHeight);
                        }
                    }
                }
            }
        }
    }
}
