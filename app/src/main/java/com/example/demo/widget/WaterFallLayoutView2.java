package com.example.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/**
 * 瀑布流视图
 * 还有缺陷 子布局（格子布局）的高度计算存在问题，会导致子布局中的最后一个元素显示不全。
 */
public class WaterFallLayoutView2 extends ViewGroup {

    private final String tag = WaterFallLayoutView2.class.getName();
    private int columns = 2;
    private int rows = 1;
    private int margin = 20;
    private int mMaxChildWidth = 0;

    public WaterFallLayoutView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public WaterFallLayoutView2(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public WaterFallLayoutView2(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMaxChildWidth = 0;
        int childViewCount = getChildCount();
        if (childViewCount == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        rows = childViewCount % columns == 0 ? childViewCount / columns : childViewCount / columns + 1;// 行数
        int[] columnsArray = new int[columns];
        for (int i = 0; i < rows; i++) {// 遍历行
            for (int j = 0; j < columns; j++) {// 遍历每一行的元素
                View child = this.getChildAt(i * columns + j);
                if (child == null)
                    break;
                //测量子View的宽和高
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                //子View占据的高度
                int childHeight = child.getMeasuredHeight();

                if (child.getVisibility() == GONE) {
                    continue;
                }
                child.measure(MeasureSpec.makeMeasureSpec(
                        MeasureSpec.getSize(widthMeasureSpec),
                        MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                        childHeight, MeasureSpec.AT_MOST));
                columnsArray[j] += childHeight + margin;
                mMaxChildWidth = Math.max(mMaxChildWidth, child.getMeasuredWidth());
            }
        }
        setMeasuredDimension(resolveSize(mMaxChildWidth, widthMeasureSpec),
                resolveSize(getMax(columnsArray) + margin, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l;// 布局区域宽度
        if (getChildCount() == 0) return;
        int gridWidth = (width - margin * (columns + 1)) / columns;// 格子宽度
        int gridHeight = 0;// 格子高度
        int left = 0;
        int[] columnsArray = new int[columns];

        for (int i = 0; i < rows; i++) {// 遍历行
            for (int j = 0; j < columns; j++) {// 遍历每一行的元素
                View child = this.getChildAt(i * columns + j);
                if (child == null)
                    return;
                int childHeight = child.getMeasuredHeight();
                child.measure(MeasureSpec.makeMeasureSpec(gridWidth,
                        MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                        childHeight, MeasureSpec.AT_MOST));

                // 如果最后有一个对其的标志，为了底部对其
                if (child.getTag() != null && child.getTag().equals(tag)) {
                    child.measure(MeasureSpec.makeMeasureSpec(gridWidth,
                            MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                            getMax(columnsArray) - columnsArray[j], MeasureSpec.AT_MOST));
                    gridHeight = getMax(columnsArray) - columnsArray[j];
                    left = j * gridWidth + margin * (j + 1);
                    child.layout(left, columnsArray[j] + margin, left + gridWidth, columnsArray[j]
                            + gridHeight);
                    break;
                }

                gridHeight = childHeight;
                left = j * gridWidth + margin * (j + 1);
                columnsArray[j] += margin;
                child.layout(left, columnsArray[j], left + gridWidth, columnsArray[j] + gridHeight);
                columnsArray[j] += gridHeight;
            }
        }
    }

    /**
     * 计算整体布局高度，为了在嵌套在scrollview中能显示出来
     */
    private int getMax(int[] array) {
        int max = array[0];
        for (int i : array) {
            if (max < i)
                max = i;
        }
        return max;
    }

}
