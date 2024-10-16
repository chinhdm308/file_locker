package com.base.presentation.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.base.presentation.R
import com.base.presentation.databinding.LayoutAppHeaderBinding

class LayoutAppHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutAppHeaderBinding.inflate(LayoutInflater.from(context))

    private var title: String?

    init {
        addView(binding.root)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LayoutAppHeaderAttributes,
            0, 0
        ).apply {
            try {
                title = getString(R.styleable.LayoutAppHeaderAttributes_title)
                binding.ivBack.isVisible = getBoolean(R.styleable.LayoutAppHeaderAttributes_showBackButton, true)

            } finally {
                recycle()
            }
        }

        setTitle(title)
    }

    fun setTitle(title: String?) {
        binding.tvTitle.text = title
    }

    fun setOnBackPress(onClick: OnClickListener) {
        binding.ivBack.setOnClickListener(onClick)
    }
}