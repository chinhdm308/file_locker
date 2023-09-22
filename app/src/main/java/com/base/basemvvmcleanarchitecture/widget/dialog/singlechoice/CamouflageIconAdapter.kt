package com.base.basemvvmcleanarchitecture.widget.dialog.singlechoice

import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.base.basemvvmcleanarchitecture.R
import com.base.basemvvmcleanarchitecture.databinding.ItemCamouflageIconBinding
import com.base.basemvvmcleanarchitecture.utils.helper.CamouflageIconHelper

class CamouflageIconAdapter(onClickItem: (CamouflageIconHelper.CamouflageIconModel) -> Unit) :
    BaseSingleChoiceAdapter<CamouflageIconHelper.CamouflageIconModel>(
        CamouflageIconDiffUtil(),
        onClickItem
    )

class CamouflageIconDiffUtil : BaseDiffUtil<CamouflageIconHelper.CamouflageIconModel>() {
    override fun areContentsTheSame(
        oldItem: CamouflageIconHelper.CamouflageIconModel,
        newItem: CamouflageIconHelper.CamouflageIconModel
    ): Boolean {
        return oldItem == newItem
    }
}

class CamouflageIconHolder(
    private val binding: ItemCamouflageIconBinding,
    private val onClickItem: (CamouflageIconHelper.CamouflageIconModel) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: CamouflageIconHelper.CamouflageIconModel) {
        binding.imgAppIcon.setImageResource(data.icon)
        binding.textAppName.text = data.name
        binding.imgChecked.isInvisible = !data.isChecked
        if (data.isChecked) {
            binding.root.setBackgroundResource(R.drawable.ripple_item_single_choice)
        } else {
            binding.root.setBackgroundResource(0)
        }

        binding.root.setOnClickListener {
            onClickItem.invoke(data)
        }
    }
}