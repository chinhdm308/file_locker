package com.base.presentation.ui.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.presentation.databinding.ItemSafeboxBinding
import com.base.presentation.ui.main.SafeBoxMgr.SafeBox
import com.base.presentation.ui.setting.SettingActivity

class BoxAdapter(
    private val context: Context,
    private val hideImageCount: Int,
    private val hideVideoCount: Int,
    private val hideAudioCount: Int,
    private val hideFileCount: Int,
    private val itemClick: (Int) -> Unit
) : ListAdapter<SafeBox, BoxAdapter.ItemViewHolder>(SafeBoxDiffUtil()) {

    private var mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemSafeboxBinding.inflate(mInflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val box = getItem(position)
        holder.bind(box)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class ItemViewHolder(val binding: ItemSafeboxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(box: SafeBox) {
            binding.ivIcon.setImageResource(box.iconId)
            binding.tvTitle.setText(box.titleId)

            val count = when (box.id) {
                SafeBoxMgr.BOX_ID_PIC -> hideImageCount
                SafeBoxMgr.BOX_ID_VIDEO -> hideVideoCount
                SafeBoxMgr.BOX_ID_AUDIO -> hideAudioCount
                SafeBoxMgr.BOX_ID_FILE -> hideFileCount
                else -> 0
            }
            if (box.id != SafeBoxMgr.BOX_ID_SETTING) {
                val detail = context.getString(box.detailId, count)
                binding.tvDetail.text = detail
            } else {
                binding.tvDetail.isVisible = false
            }
            binding.root.setOnClickListener {
                if (box.id == SafeBoxMgr.BOX_ID_SETTING) {
                    context.startActivity(Intent(context, SettingActivity::class.java))
                    return@setOnClickListener
                }
                itemClick.invoke(box.id)
            }
        }
    }

    class SafeBoxDiffUtil : DiffUtil.ItemCallback<SafeBox>() {
        override fun areItemsTheSame(oldItem: SafeBox, newItem: SafeBox): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SafeBox, newItem: SafeBox): Boolean {
            return oldItem == newItem
        }
    }

}