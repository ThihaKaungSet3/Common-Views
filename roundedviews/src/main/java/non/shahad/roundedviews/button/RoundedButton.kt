package non.shahad.roundedviews.button

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import non.shahad.roundedviews.R
import non.shahad.roundedviews.helper.CanvasRounder
import non.shahad.roundedviews.helper.CornerType
import non.shahad.roundedviews.helper.getCornerRadius
import non.shahad.roundedviews.helper.updateOutlineProvider
import non.shahad.shrink.applyClickShrink
import non.shahad.utils.dpToPx

class RoundedButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    styleDef: Int = 0
): AppCompatButton(context,attributeSet,styleDef) {

    private val paint: Paint = Paint()
    private val canvasRounder: CanvasRounder

    init {
        setWillNotDraw(false)
        val array = context.obtainStyledAttributes(attributeSet, R.styleable.RoundedButton,0,0)
        val cornersHolder = array.getCornerRadius(
            R.styleable.RoundedButton_button_corner_radius,
            R.styleable.RoundedButton_button_top_left_corner_radius,
            R.styleable.RoundedButton_button_top_right_corner_radius,
            R.styleable.RoundedButton_button_bottom_right_corner_radius,
            R.styleable.RoundedButton_button_bottom_left_corner_radius
        )

        val isShrinkEnabled = array.getBoolean(R.styleable.RoundedButton_isShrinkEnabled,false)
        val shrinkValue = array.getFloat(R.styleable.RoundedButton_shrinkValue,0.97f)
        if (isShrinkEnabled){
            this.applyClickShrink(shrinkValue)
        }

        val padding = array.getDimension(R.styleable.RoundedButton_button_padding,context.dpToPx(8f)).toInt()
        this.setPadding(padding,padding,padding,padding)

        val backgroundColor = array.getColor(
            R.styleable.RoundedButton_button_shape_background_color,
            ContextCompat.getColor(context,android.R.color.darker_gray))

        paint.color = backgroundColor
        paint.flags = Paint.ANTI_ALIAS_FLAG

        array.recycle()
        canvasRounder = CanvasRounder(cornersHolder)
        updateOutlineProvider(cornersHolder)

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