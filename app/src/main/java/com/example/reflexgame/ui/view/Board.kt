package com.example.reflexgame.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.reflexgame.R
import kotlin.math.ceil

const val TABLE_LENGTH = 4
const val PADDING_SCALE_RATIO = 32

class BoardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var squareWidth = 0f
    private var squarePadding = 0f
    private val paint = Paint()
    private val highlightPaint = Paint()
    private var highlightedSquare: Pair<Int, Int>? = null
    var clickPositionListener: ((Pair<Int, Int>) -> Unit)? = null

    init {
        paint.color = ContextCompat.getColor(context, R.color.light_gray)
        highlightPaint.color = ContextCompat.getColor(context, R.color.blue)

        setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                view.performClick()
                handleClickPosition(event.x, event.y)
            }
            true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        squareWidth = MeasureSpec.getSize(widthMeasureSpec) / TABLE_LENGTH.toFloat()
        squarePadding = squareWidth / PADDING_SCALE_RATIO
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (y in 0 until TABLE_LENGTH) {
            for (x in 0 until TABLE_LENGTH) {
                val left = (squareWidth * x) + squarePadding
                val right = (squareWidth * (x + 1)) - squarePadding
                val top = (squareWidth * y) + squarePadding
                val bottom = (squareWidth * (y + 1)) - squarePadding
                canvas?.drawRect(left, top, right, bottom, getPaint(x, y))
            }
        }
    }

    fun highlightSquare(position: Pair<Int, Int>?) {
        highlightedSquare = position
        invalidate()
    }

    private fun getPaint(x: Int, y: Int) =
        if (highlightedSquare?.first == x && highlightedSquare?.second == y) highlightPaint else paint

    private fun handleClickPosition(x: Float, y: Float) {
        val xPosition = ceil(x / squareWidth.toDouble()).toInt() - 1
        val yPosition = ceil(y / squareWidth.toDouble()).toInt() - 1
        clickPositionListener?.invoke(Pair(xPosition, yPosition))
    }
}
