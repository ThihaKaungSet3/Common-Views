package non.shahad.shrink

import android.view.MotionEvent
import android.view.View
import java.lang.ref.WeakReference
import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder

class Shrinker(view: View,private val shrinkValue: Float) {
    private val weakRefView = WeakReference(view)

    init {
        if (!view.hasOnClickListeners()) view.setOnClickListener { }
        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> buildShrinkAnimator()?.start()
                MotionEvent.ACTION_UP -> buildGrowAnimator()?.start()
            }
            return@setOnTouchListener false
        }
    }

    private fun buildShrinkAnimator(): Animator? {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, shrinkValue)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, shrinkValue)
        weakRefView.get()?.apply {
            val animator = ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY)
            animator.duration = 100
            return animator
        }
        return null
    }


    private fun buildGrowAnimator(): Animator? {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, shrinkValue, 1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, shrinkValue, 1f)
        weakRefView.get()?.apply {
            val animator = ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY)
            animator.duration = 100
            return animator
        }
        return null
    }
}

fun View.applyClickShrink(shrinkValue: Float): View {
    return this.apply {
        Shrinker(this,shrinkValue)
    }
}