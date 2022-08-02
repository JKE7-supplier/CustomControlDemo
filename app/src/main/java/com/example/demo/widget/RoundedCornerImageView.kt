package com.example.demo.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import com.example.demo.R

/**
 * 支持圆形/圆角/变框线的ImageView
 * Created by Jack Ke on 2021/11/19
 */
class RoundedCornerImageView : AppCompatImageView {

    private val imageTypeCircle = 0
    private val imageTypeRound = 1
    private val imageTypeOval = 2

    private var leftTopCornerRadius: Float = 0f
    private var rightTopCornerRadius: Float = 0f
    private var leftBottomCornerRadius: Float = 0f
    private var rightBottomCornerRadius: Float = 0f
    private var roundRadius: Float = 0f
    private var borderWidth: Float = dp2px(2).toFloat()
    private var cornerRadius: Float =0f

    private var progressColor: Int = Color.TRANSPARENT
    private var borderColor: Int = Color.TRANSPARENT
    private var viewWidth: Int = 0
    private var borderProgress: Int = 0
    private var imageViewType: Int = imageTypeRound

    private lateinit var bitmapPaint: Paint
    private lateinit var borderPaint: Paint
    private lateinit var bitmapShader: BitmapShader
    private lateinit var roundRect: RectF
    private lateinit var matrixClass: Matrix

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    private fun initView(attributeSet: AttributeSet?){
        context.obtainStyledAttributes(attributeSet, R.styleable.RoundedCornerImageView).apply {
            imageViewType = getInt(R.styleable.RoundedCornerImageView_imageViewStyle, imageTypeRound)
            borderColor = getColor(R.styleable.RoundedCornerImageView_border_color, Color.TRANSPARENT)
            borderWidth = getDimension(R.styleable.RoundedCornerImageView_border_width, dp2px(4).toFloat())
            cornerRadius = getDimension(R.styleable.RoundedCornerImageView_corner_radius, dp2px(2).toFloat())
            leftTopCornerRadius = getDimension(R.styleable.RoundedCornerImageView_leftTop_corner_radius, 0f)
            leftBottomCornerRadius = getDimension(R.styleable.RoundedCornerImageView_leftBottom_corner_radius, 0f)
            rightTopCornerRadius = getDimension(R.styleable.RoundedCornerImageView_rightTop_corner_radius, 0f)
            rightBottomCornerRadius = getDimension(R.styleable.RoundedCornerImageView_rightBottom_corner_radius, 0f)
            progressColor = getColor(R.styleable.RoundedCornerImageView_progress_color, Color.WHITE)
            borderProgress = getInteger(R.styleable.RoundedCornerImageView_progress_border, 0)
            recycle()
        }
    }

    private fun initPaint() {
        bitmapPaint = Paint()
        bitmapPaint.isAntiAlias = true
        borderPaint = Paint()
        borderPaint.isAntiAlias = true
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeCap = Paint.Cap.ROUND
        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidth
    }

    private fun getHalfBorderWidth(): Float = borderWidth / 2

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (imageViewType == imageTypeCircle) {
            viewWidth = Math.min(
                MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec)
            )
            roundRadius = viewWidth / 2 - getHalfBorderWidth()
            setMeasuredDimension(viewWidth, viewWidth)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (imageViewType == imageTypeRound || imageViewType == imageTypeOval) {
            roundRect = RectF(getHalfBorderWidth(), getHalfBorderWidth(), w - getHalfBorderWidth(), h - getHalfBorderWidth())
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable == null) return
        initPaint()
        setUpShader()
        when (imageViewType) {
            imageTypeRound -> {
                val roundPath = Path()
                setRoundPath(roundPath)
                canvas.drawPath(roundPath, bitmapPaint)
                canvas.drawPath(roundPath, borderPaint)
            }
            imageTypeCircle -> {
                canvas.drawCircle(roundRadius + getHalfBorderWidth(), roundRadius + getHalfBorderWidth(), roundRadius, bitmapPaint)
                canvas.drawCircle(roundRadius + getHalfBorderWidth(), roundRadius + getHalfBorderWidth(), roundRadius, borderPaint)
                borderPaint.color = progressColor
                val rect = RectF(getHalfBorderWidth(), getHalfBorderWidth(), roundRadius * 2 + getHalfBorderWidth(), roundRadius * 2 + getHalfBorderWidth())
                canvas.drawArc(rect, -90f, borderProgress.toFloat(), false, borderPaint)
            }
            else -> {
                canvas.drawOval(roundRect, bitmapPaint)
                canvas.drawOval(roundRect, borderPaint)
            }
        }
    }

    private fun setUpShader() {
        val drawable = drawable ?: return
        val bitmap: Bitmap = drawableToBitmap(drawable) ?: return
        bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        matrixClass = Matrix()
        var scale = 1.0f
        if (imageViewType == imageTypeCircle) {
            val bSize = Math.min(bitmap.width, bitmap.height)
            scale = viewWidth * 1.0f / bSize
            val dx = (bitmap.width * scale - viewWidth) / 2
            val dy = (bitmap.height * scale - viewWidth) / 2
            matrixClass.setTranslate(-dx, -dy)
        } else if (imageViewType == imageTypeRound || imageViewType == imageTypeOval) {
            if (!(bitmap.width == width && bitmap.height == height)) {
                scale = Math.max(
                    width * 1.0f / bitmap.width,
                    height * 1.0f / bitmap.height
                )
                val dx = (scale * bitmap.width - width) / 2
                val dy = (scale * bitmap.height - height) / 2
                matrixClass.setTranslate(-dx, -dy)
                matrixClass.setTranslate(-dx, -dy)
            }
        }
        matrixClass.preScale(scale, scale)
        bitmapShader.setLocalMatrix(matrixClass)
        bitmapShader.setLocalMatrix(matrixClass)
        bitmapPaint.shader = bitmapShader
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap? {
        return try {
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }
            val width = drawable.intrinsicWidth
            val height = drawable.intrinsicHeight
            val bitmap = Bitmap.createBitmap(width, height, if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_4444 else Bitmap.Config.RGB_565)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, width, height)
            drawable.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun setRoundPath(roundPath: Path) {
        roundPath.reset()
        if (leftTopCornerRadius == 0f && leftBottomCornerRadius == 0f && rightTopCornerRadius == 0f && rightBottomCornerRadius == 0f) {
            roundPath.addRoundRect(
                roundRect, floatArrayOf(
                    cornerRadius, cornerRadius,
                    cornerRadius, cornerRadius,
                    cornerRadius, cornerRadius,
                    cornerRadius, cornerRadius
                ),
                Path.Direction.CW
            )
        } else {
            roundPath.addRoundRect(
                roundRect, floatArrayOf(
                    leftTopCornerRadius, leftTopCornerRadius,
                    rightTopCornerRadius, rightTopCornerRadius,
                    rightBottomCornerRadius, rightBottomCornerRadius,
                    leftBottomCornerRadius, leftBottomCornerRadius
                ),
                Path.Direction.CW
            )
        }
    }

    private fun dp2px(dpVal: Int): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal.toFloat(), resources.displayMetrics).toInt()

    fun setType(imageType: Int): RoundedCornerImageView {
        if (imageViewType != imageType) {
            imageViewType = imageType
            if (imageViewType != imageTypeRound && imageViewType != imageTypeCircle && imageViewType != imageTypeOval) {
                imageViewType = imageTypeRound
            }
            requestLayout()
        }
        return this
    }

    fun setCornerRadius(cornerRadius: Int): RoundedCornerImageView {
        if (this.cornerRadius != dp2px(cornerRadius).toFloat()) {
            this.cornerRadius = dp2px(cornerRadius).toFloat()
            invalidate()
        }
        return this
    }

    fun setLeftTopCornerRadius(cornerRadius: Int): RoundedCornerImageView {
        if (leftTopCornerRadius != dp2px(cornerRadius).toFloat()) {
            leftTopCornerRadius = dp2px(cornerRadius).toFloat()
            invalidate()
        }
        return this
    }

    fun setRightTopCornerRadius(cornerRadius: Int): RoundedCornerImageView {
        if (rightTopCornerRadius != dp2px(cornerRadius).toFloat()) {
            rightTopCornerRadius = dp2px(cornerRadius).toFloat()
            invalidate()
        }
        return this
    }

    fun setLeftBottomCornerRadius(cornerRadius: Int): RoundedCornerImageView {
        if (leftBottomCornerRadius != dp2px(cornerRadius).toFloat()) {
            leftBottomCornerRadius = dp2px(cornerRadius).toFloat()
            invalidate()
        }
        return this
    }

    fun setRightBottomCornerRadius(cornerRadius: Int): RoundedCornerImageView {
        if (rightBottomCornerRadius != dp2px(cornerRadius).toFloat()) {
            rightBottomCornerRadius = dp2px(cornerRadius).toFloat()
            invalidate()
        }
        return this
    }

    fun setBorderWidth(borderWidth: Int): RoundedCornerImageView {
        if (this.borderWidth != dp2px(borderWidth).toFloat()) {
            this.borderWidth = dp2px(borderWidth).toFloat()
            invalidate()
        }
        return this
    }

    fun setBorderColor(borderColor: Int): RoundedCornerImageView {
        if (this.borderColor != borderColor) {
            this.borderColor = borderColor
            invalidate()
        }
        return this
    }

    fun setProgress(progress: Int, @ColorRes color: Int) {
        borderProgress = progress
        progressColor = color
        invalidate()
    }
}