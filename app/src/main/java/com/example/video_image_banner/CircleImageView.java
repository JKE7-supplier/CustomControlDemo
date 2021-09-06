/* (c) Disney. All rights reserved. */
package com.example.video_image_banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;


/**
 * Created by Jack Ke on 2021/8/27
 */
@SuppressLint("AppCompatCustomView")
public class CircleImageView extends ImageView {
    private static final int RECT = 1;
    private static final int CIRCLE = 2;
    private Paint paint;
    private Bitmap rawBitmap;
    private BitmapShader shader;
    private Matrix matrix;
    private Paint borderPaint;
    private RectF rectBorder;
    private RectF rectBitmap;
    //边框的宽度
    private int borderWidth = 3;
    //边框的颜色
    private int borderColor = Color.WHITE;
    //是否设置边框，默认不设置 false
    private boolean createBorder = false;

    /**
     * 0 ClAMP ： Bitmap以其内容的最后一行像素填充剩余的高的空白或者最后一列像素填充剩余宽空白
     * 1 MIRROR ：Bitmap以其内容以镜像的方式填充剩余空白
     * 2 REPEAT ：Bitmap以其内容以重复的方式填充剩余空白
     */
    private int tileY = 0;

    private int tileX = 0;

    //边角得半径
    private int roundRadius = 20;

    /**
     * circle 圆形 默认
     * rect 带圆角得形状
     */
    private int shapeType = RECT;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
//        //获取自定以的属性
//        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
//        mTileX = array.getInt(R.styleable.CircleImageView_mTileX, 0);
//        mTileY = array.getInt(R.styleable.CircleImageView_mTileY, 0);
//        mBorderColor = array.getColor(R.styleable.CircleImageView_mBorderColor, 0xFF0080FF);
//        mBorderWidth = array.getDimensionPixelOffset(R.styleable.CircleImageView_mBorderWidth, 4);
//        createBorder = array.getBoolean(R.styleable.CircleImageView_createBorder, false);
//        mShapeType = array.getInt(R.styleable.CircleImageView_mShapeType, CIRCLE);
//        mRoundRadius = array.getDimensionPixelOffset(R.styleable.CircleImageView_mRoundRadius, 10);
//        array.recycle();
    }

    /**
     * 在onDraw中不要有过多复杂的逻辑，和过于复杂多余的计算，否则会导致绘制不全的现象
     * 在onDraw方法中如果一个值是多次使用的，就通过变量先计算好，不要每次用的时候才计算，影响计算的效率
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //获取设置图片的Bitmap对象。
        Bitmap bitmap = getBitmap(getDrawable());
        if (bitmap != null) {
            //支持Padding的属性
            final int paddingLeft = getPaddingLeft();
            final int paddingRight = getPaddingRight();
            final int paddingTop = getPaddingTop();
            final int paddingBottom = getPaddingBottom();
            //通过减去padding的属性值，获得图片正真的高度
            float width = getWidth() - paddingLeft - paddingRight;
            float height = getHeight() - paddingTop - paddingBottom;
            float diameter = Math.min(width, height);
            //如果是矩形，则直接是宽高，是圆形为半径
            float dstWidth = shapeType == RECT ? width : diameter;
            float dstHeight = shapeType == RECT ? height : diameter;
            float doubleBorderWidth = borderWidth * 2.0f;
            float halfBorderWidth = borderWidth / 2.0f;
            //判断是否已经创建，或者复用
            if (shader == null || !bitmap.equals(rawBitmap)) {
                rawBitmap = bitmap;
                shader = createBitmapShader(rawBitmap, tileX, tileY);
            }
            //设置缩放比例，缩放的比例应该是正式的宽度与原来的宽度的比。而宽度和高度还需要减去边框的宽度*2
            matrix.setScale((dstWidth - doubleBorderWidth) / rawBitmap.getWidth(), (dstHeight - doubleBorderWidth) / rawBitmap.getHeight());
            //为着色器色器设置矩阵
            shader.setLocalMatrix(matrix);
            //画笔设置着色器
            paint.setShader(shader);
            //如果设置了边框的，对边框的画笔进行设置
            if (createBorder) {
                //设置的样式是边框
                borderPaint.setStyle(Paint.Style.STROKE);
                //边框的宽度
                borderPaint.setStrokeWidth(borderWidth);
                //如果是不设置边框得 使边框得画笔变为透明
                borderPaint.setColor(createBorder ? borderColor : Color.WHITE);
            }
            if (shapeType == RECT) {
                //画矩形
                createRoundRect(canvas, width, height, doubleBorderWidth, halfBorderWidth);
            } else {
                //画圆
                createCircle(canvas, diameter / 2.0f, halfBorderWidth);
            }
        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * 画矩形
     *
     * @param canvas            画布
     * @param width             图形的宽度
     * @param height            图形的高度
     * @param doubleBorderWidth 边框的宽度*2
     * @param halfBorderWidth   边框的宽度/2
     */
    private void createRoundRect(Canvas canvas, float width, float height, float doubleBorderWidth, float halfBorderWidth) {
        //边框的矩形，至于为什么要减去一半的原因是
        // 绘制带边框的矩形（其他形状同理），矩形的边界是边框的中心，而不是边框的边界，
        // 所以在绘制这些带边框的形状时，需要减去边框宽度的一半
        rectBorder.set(halfBorderWidth, halfBorderWidth, width - halfBorderWidth, height - halfBorderWidth);
        //图形的矩形
        rectBitmap.set(0.0f, 0.0f, width - doubleBorderWidth, height - doubleBorderWidth);
        float bitmapRadius = Math.max((roundRadius - borderWidth), 0.0f);
        if (createBorder) {
            float rectRadius = Math.max(roundRadius - halfBorderWidth, 0.0f);
            //画边边框
            canvas.drawRoundRect(rectBorder, rectRadius, rectRadius, borderPaint);
            //画布平移
            canvas.translate(borderWidth, borderWidth);
        }
        //画图像得
        canvas.drawRoundRect(rectBitmap, bitmapRadius, bitmapRadius, paint);
    }

    /**
     * 画圆形
     *
     * @param canvas
     * @param radius
     * @param halfBorderWidth
     */
    private void createCircle(Canvas canvas, float radius, float halfBorderWidth) {
        //图形正真的半径还要减去边框的宽度
        float realRadius = radius - borderWidth;
        if (createBorder) {
            //画边框，画边框的半径一定要减去边框的一边
            canvas.drawCircle(radius, radius, radius - halfBorderWidth, borderPaint);
            //平移画布
            canvas.translate(borderWidth, borderWidth);
        }
        canvas.drawCircle(realRadius, realRadius, realRadius, paint);
    }


    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);//设置抗锯齿
        //该方法千万别放到onDraw()方法里面调用，否则会不停的重绘的，因为该方法调用了invalidate() 方法
        //View Layer 绘制所消耗的实际时间是比不使用 View Layer 时要高的，所以要慎重使用。所以我们将View Layer关闭
        //否则会出现黑色背景的现象
        setLayerType(View.LAYER_TYPE_NONE, null);
        matrix = new Matrix();
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setAntiAlias(true);
        rectBitmap = new RectF();
        rectBorder = new RectF();
    }

    /**
     * 根据不同的类型获取Bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap getBitmap(Drawable drawable) {
        //如果是图片类型则直接返回
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof ColorDrawable) {
            //颜色类型
            Rect rect = drawable.getBounds();
            int width = rect.right - rect.left;
            int height = rect.bottom - rect.top;
            int color = ((ColorDrawable) drawable).getColor();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawARGB(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * 根据不同的mTileX，mTileY创建BitmapShader
     *
     * @param mTileX
     * @param mTileY
     * @return
     */
    private BitmapShader createBitmapShader(Bitmap bitmap, int mTileX, int mTileY) {
        BitmapShader.TileMode tileModeX;
        BitmapShader.TileMode tileModeY;
        switch (mTileX) {
            case 1:
                tileModeX = BitmapShader.TileMode.MIRROR;
                break;
            case 2:
                tileModeX = BitmapShader.TileMode.REPEAT;
                break;
            default:
                tileModeX = BitmapShader.TileMode.CLAMP;
        }

        switch (mTileY) {
            case 1:
                tileModeY = BitmapShader.TileMode.MIRROR;
                break;
            case 2:
                tileModeY = BitmapShader.TileMode.REPEAT;
                break;
            default:
                tileModeY = BitmapShader.TileMode.CLAMP;
                break;
        }
        return new BitmapShader(bitmap, tileModeX, tileModeY);

    }

    public Bitmap getRawBitmap() {
        return rawBitmap;
    }

    public void setRawBitmap(Bitmap rawBitmap) {
        this.rawBitmap = rawBitmap;
    }

    public BitmapShader getShader() {
        return shader;
    }

    public void setShader(BitmapShader Shader) {
        this.shader = shader;
    }

    @Override
    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public Paint getBorderPaint() {
        return borderPaint;
    }

    public void setBorderPaint(Paint borderPaint) {
        this.borderPaint = borderPaint;
    }

    public RectF getRectBorder() {
        return rectBorder;
    }

    public void setRectBorder(RectF rectBorder) {
        this.rectBorder = rectBorder;
    }

    public RectF getRectBitmap() {
        return rectBitmap;
    }

    public void setRectBitmap(RectF rectBitmap) {
        this.rectBitmap = rectBitmap;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public boolean isCreateBorder() {
        return createBorder;
    }

    public void setCreateBorder(boolean createBorder) {
        this.createBorder = createBorder;
    }

    public int getRoundRadius() {
        return roundRadius;
    }

    public void setRoundRadius(int roundRadius) {
        this.roundRadius = roundRadius;
    }

    public int getShapeType() {
        return shapeType;
    }

    public void setShapeType(int shapeType) {
        this.shapeType = shapeType;
    }
}



