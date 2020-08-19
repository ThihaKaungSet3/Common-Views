package non.shahad.roundedviews.textview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import non.shahad.roundedviews.R
import non.shahad.roundedviews.helper.*
import non.shahad.roundedviews.helper.CanvasRounder
import non.shahad.roundedviews.helper.getCornerRadius
import non.shahad.roundedviews.helper.updateOutlineProvider
import non.shahad.utils.dpToPx

class RoundedTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    styleDef: Int = 0
): AppCompatTextView(context,attributeSet,styleDef) {

    private val paint: Paint = Paint()
    private val canvasRounder: CanvasRounder

    init {
        setWillNotDraw(false)
        val array = context.obtainStyledAttributes(attributeSet, R.styleable.RoundedTextView,0,0)
        val cornersHolder = array.getCornerRadius(
            R.styleable.RoundedTextView_corner_radius,
            R.styleable.RoundedTextView_top_left_corner_radius,
            R.styleable.RoundedTextView_top_right_corner_radius,
            R.styleable.RoundedTextView_bottom_right_corner_radius,
            R.styleable.RoundedTextView_bottom_left_corner_radius
        )

        val defaultPadding = context.dpToPx(4f)
        val paddingTopBottom = array.getDimension(R.styleable.RoundedTextView_padding_top_and_bottom,defaultPadding).toInt()
        val paddingLeftRight = array.getDimension(R.styleable.RoundedTextView_padding_left_and_right,defaultPadding).toInt()

        val backgroundColor = array.getColor(R.styleable.RoundedTextView_shape_background_color,ContextCompat.getColor(context,android.R.color.darker_gray))
        paint.color = backgroundColor
        paint.flags = Paint.ANTI_ALIAS_FLAG

        array.recycle()
        canvasRounder = CanvasRounder(cornersHolder)
        updateOutlineProvider(cornersHolder)
        setPadding(paddingLeftRight,paddingTopBottom,paddingLeftRight,paddingTopBottom)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasRounder.updateSize(w,h)
    }

    override fun draw(canvas: Canvas?){
        canvasRounder.round(canvas!!,paint){ super.draw(it) }
    }

    private fun setBackgroundColorShape(colorRes: Int){
        paint.color = ContextCompat.getColor(context, colorRes)
        paint.flags = Paint.ANTI_ALIAS_FLAG
        invalidate()
    }


    fun setCornerRadius(cornerRadius: Float, cornerType: CornerType = CornerType.ALL) {
        when (cornerType) {
            CornerType.ALL -> {
                canvasRounder.cornerRadius = cornerRadius
            }
            CornerType.TOP_LEFT -> {
                canvasRounder.topLeftCornerRadius = cornerRadius
            }
            CornerType.TOP_RIGHT -> {
                canvasRounder.topRightCornerRadius = cornerRadius
            }
            CornerType.BOTTOM_RIGHT -> {
                canvasRounder.bottomRightCornerRadius = cornerRadius
            }
            CornerType.BOTTOM_LEFT -> {
                canvasRounder.bottomLeftCornerRadius = cornerRadius
            }
        }
        updateOutlineProvider(cornerRadius)
        invalidate()
    }
}