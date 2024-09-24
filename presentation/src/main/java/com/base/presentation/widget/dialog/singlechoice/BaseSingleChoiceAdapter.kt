package com.base.presentation.widget.dialog.singlechoice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.presentation.databinding.ItemCamouflageIconBinding
import com.base.presentation.utils.helper.CamouflageIconHelper

abstract class BaseSingleChoiceAdapter<T : BaseSingleChoiceModel>(
    diffUtil: BaseDiffUtil<T>,
    private val onClickItem: (T) -> Unit
) : ListAdapter<T, RecyclerView.ViewHolder>(diffUtil) {

    private var mInflater: LayoutInflater? = null

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (mInflater == null) mInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SingleChoiceType.CAMOUFLAGE_ICON.value -> {
                val binding = ItemCamouflageIconBinding.inflate(mInflater!!, parent, false)
                CamouflageIconHolder(binding, onClickItem as (CamouflageIconHelper.CamouflageIconModel) -> Unit)
            }

            else -> {
                val binding = ItemCamouflageIconBinding.inflate(mInflater!!, parent, false)
                CamouflageIconHolder(binding, onClickItem as (CamouflageIconHelper.CamouflageIconModel) -> Unit)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CamouflageIconHolder) {
            holder.bind(getItem(position) as CamouflageIconHelper.CamouflageIconModel)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).singleChoiceType) {
            SingleChoiceType.CAMOUFLAGE_ICON -> SingleChoiceType.CAMOUFLAGE_ICON.value
        }
    }
}

abstract class BaseDiffUtil<T : BaseSingleChoiceModel> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.id == newItem.id
    }
}