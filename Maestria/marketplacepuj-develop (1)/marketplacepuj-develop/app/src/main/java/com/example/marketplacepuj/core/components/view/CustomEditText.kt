package com.example.marketplacepuj.core.components.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.marketplacepuj.R


class CustomEditText : AppCompatEditText,
    View.OnFocusChangeListener, TextWatcher {

    @ColorInt
    private var strokeColor: Int? = null
    private var isEmpty: Boolean = true

    private val linkedButtons: MutableSet<Button> = mutableSetOf()
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        strokeColor = ContextCompat.getColor(context, R.color.inputBorderColor)
        onFocusChangeListener = this
        addTextChangedListener(this)
        setOnTouchListener(object : OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val drawableRight = 2
                val drawable = compoundDrawables[drawableRight]

                if (drawable != null && event.rawX >= (right - drawable.bounds.width()) &&
                    (inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                ) {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            transformationMethod = HideReturnsTransformationMethod.getInstance()
                        }
                        MotionEvent.ACTION_UP -> {
                            transformationMethod = PasswordTransformationMethod.getInstance()
                        }
                    }

                    return true
                }
                return false
            }
        })
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeTextChangedListener(this)
        onFocusChangeListener = null
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int,
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
//        val strokeWidth = context.resources.getDimension(R.dimen.editText_stroke_width_1dp)
//        when (hasFocus) {
//            true -> animateStroke(strokeWidth, strokeWidth)
//            false -> animateStroke(strokeWidth, strokeWidth)
//        }
    }

    override fun afterTextChanged(s: Editable) {
        isEmpty = s.toString().isEmpty()
        when (isEmpty) {
            true -> {
                val color = ContextCompat.getColor(context, R.color.inputBorderColor)
                changeTinColorLeftDrawable(color)
            }
            false -> {
                val color = ContextCompat.getColor(context, R.color.iconEdtColorDefault)
                changeTinColorLeftDrawable(color)
            }
        }
        changeButtonsLinkedSate(isEmpty)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    private fun changeTinColorLeftDrawable(color: Int) {
        compoundDrawables[0]?.setTint(color)
    }

    private fun animateStroke(start: Float, end: Float) {
        ValueAnimator.ofFloat(start, end).apply {
            duration = 300
            addUpdateListener {
                val strokeWidth = (animatedValue as Float).toInt()
                ((background as InsetDrawable).drawable as GradientDrawable).setStroke(
                    strokeWidth,
                    strokeColor!!
                )
            }
            start()
        }
    }

    fun getString() = text.toString()

    override fun setError(error: CharSequence?) {
        super.setError(error)
    }

    fun linkButton(button: Button) {
        linkedButtons.add(button)
    }

    private fun changeButtonsLinkedSate(isEmpty: Boolean) {
        linkedButtons.forEach { button ->
            when (isEmpty) {
                false -> {
                    button.isEnabled = true
                }
                true -> {
                    button.isEnabled = false
                }
            }
        }
    }
}