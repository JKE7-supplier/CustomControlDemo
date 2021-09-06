/* (c) Disney. All rights reserved. */
package com.example.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 带圆角的ConstraintLayout
 * Created by Jack Ke on 2021/8/30
 */
public class RoundedConstraintLayoutView extends ConstraintLayout {
    private float round; //圆角弧度

    public RoundedConstraintLayoutView(Context context) {
        super(context);
    }

    public RoundedConstraintLayoutView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedConstraintLayoutView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (round > 0) {
            Path path = new Path();
            RectF rectF = new RectF(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
            path.addRoundRect(rectF, round, round, Path.Direction.CW);
            canvas.clipPath(path, Region.Op.INTERSECT);
        }
        super.dispatchDraw(canvas);
    }

    public void setRound(float round) {
        this.round = round;
    }
}
