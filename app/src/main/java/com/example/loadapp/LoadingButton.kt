package com.example.loadapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.content_main.view.*
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonText: String
    private var buttonBackgroundColor = R.attr.buttonBackgroundColor
    private var progress: Float = 0f
    private var valueAnimator = ValueAnimator()
    private val textRect = Rect()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { property, oldValue, newValue ->
        when(newValue) {
            ButtonState.Loading -> {
                setText("We are Downloading")
                setBgColor("#004349")
                valueAnimator= ValueAnimator.ofFloat(0f, 1f).apply {
                    addUpdateListener {
                        progress = animatedValue as Float
                        invalidate()
                    }
                    repeatMode = ValueAnimator.REVERSE
                    repeatCount = ValueAnimator.INFINITE
                    duration = 3000
                    start()
                }
                disableLoadingButton()
            }

            ButtonState.Completed -> {
                setText("Downloaded")
                setBgColor("#07C2AA")
                valueAnimator.cancel()
                resetProgress()
                enableLoadingButton()
            }

            ButtonState.Clicked -> {
            }
        }
        invalidate()
    }

    init {
        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.LoadingButton,
                0, 0).apply {

            try {
                buttonText = getString(R.styleable.LoadingButton_text).toString()
                buttonBackgroundColor = ContextCompat.getColor(context, R.color.colorPrimary)
            } finally {
                recycle()
            }
        }
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 50.0f
        color = Color.WHITE
    }

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.colorPrimary)
    }

    private val inProgressBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
    }

    private val inProgressArcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cornerRadius = 10.0f
        val backgroundWidth = measuredWidth.toFloat()
        val backgroundHeight = measuredHeight.toFloat()

        canvas.drawColor(buttonBackgroundColor)
        textPaint.getTextBounds(buttonText, 0, buttonText.length, textRect)
        canvas.drawRoundRect(0f, 0f, backgroundWidth, backgroundHeight, cornerRadius, cornerRadius, backgroundPaint)

        if (buttonState == ButtonState.Loading) {
            var progressVal = progress * measuredWidth.toFloat()
            canvas.drawRoundRect(0f, 0f, progressVal, backgroundHeight, cornerRadius, cornerRadius, inProgressBackgroundPaint)

            val arcDiameter = cornerRadius * 2
            val arcRectSize = measuredHeight.toFloat() - paddingBottom.toFloat() - arcDiameter

            progressVal = progress * 360f
            canvas.drawArc(paddingStart + arcDiameter,
                    paddingTop.toFloat() + arcDiameter,
                    arcRectSize,
                    arcRectSize,
                    0f,
                    progressVal,
                    true,
                    inProgressArcPaint)
        }
        val centerX = measuredWidth.toFloat() / 2
        val centerY = measuredHeight.toFloat() / 2 - textRect.centerY()

        canvas.drawText(buttonText,centerX, centerY, textPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val width: Int = resolveSizeAndState(minWidth, widthMeasureSpec, 1)
        val height: Int = resolveSizeAndState(
                MeasureSpec.getSize(width),
                heightMeasureSpec,
                0
        )
        widthSize = width
        heightSize = height
        setMeasuredDimension(width, height)
    }

    private fun disableLoadingButton() {
        loading_button.isEnabled = false
    }

    private fun enableLoadingButton() {
        loading_button.isEnabled = true
    }

    fun setLoadingButtonState(state: ButtonState) {
        buttonState = state
    }

    private fun setText(buttonText: String) {
        this.buttonText = buttonText
        invalidate()
        requestLayout()
    }

    private fun setBgColor(backgroundColor: String) {
        buttonBackgroundColor = Color.parseColor(backgroundColor)
        invalidate()
        requestLayout()
    }

    private fun resetProgress() {
        progress = 0f
    }
}