package com.example.marketplacepuj.core.components.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.marketplacepuj.R

class ToggleLayout : ConstraintLayout {
    private var initialStrokeColor = ContextCompat.getColor(context, R.color.categoryStrokeColor)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    private fun animateStroke(start: Float, end: Float) {
        ValueAnimator.ofFloat(start, end).apply {
            duration = 300
            addUpdateListener {
                val strokeWidth = (animatedValue as Float).toInt()
                when (background) {
                    is InsetDrawable -> {
                        ((background as InsetDrawable).drawable as GradientDrawable).setStroke(
                            strokeWidth,
                            initialStrokeColor
                        )
                    }

                    is GradientDrawable -> {
                        (background as GradientDrawable).setStroke(strokeWidth, initialStrokeColor)
                    }
                }
            }
            start()
        }
    }

    fun toggleOn() {
        val strokeWidth =
            context.resources.getDimension(R.dimen.editText_stroke_width_1dp)
        animateStroke(0f, strokeWidth)
    }

    fun toggleOff() {
        val strokeWidth =
            context.resources.getDimension(R.dimen.editText_stroke_width_1dp)
        animateStroke(strokeWidth, 0f)
    }
}