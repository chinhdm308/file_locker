package com.base.basemvvmcleanarchitecture.presentation.ui.intruders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.basemvvmcleanarchitecture.databinding.ItemIntrudersPhotoBinding
import com.base.basemvvmcleanarchitecture.utils.loadImage
import javax.inject.Inject

class IntrudersListAdapter @Inject constructor() :
    RecyclerView.Adapter<IntrudersListAdapter.IntrudersListItemViewHolder>() {

    private val intruderList = arrayListOf<IntruderPhotoItemViewState>()

    fun updateIntruderList(intruderList: List<IntruderPhotoItemViewState>) {
        this.intruderList.clear()
        this.intruderList.addAll(intruderList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = intruderList.size

    override fun onBindViewHolder(holder: IntrudersListItemViewHolder, position: Int) =
        holder.bind(intruderList[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntrudersListItemViewHolder =
        IntrudersListItemViewHolder.create(parent)

    class IntrudersListItemViewHolder(private val binding: ItemIntrudersPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.squareLayoutItem.setOnClickListener {

            }
        }

        fun bind(intruderPhotoItemViewState: IntruderPhotoItemViewState) {
            binding.imageView.loadImage(intruderPhotoItemViewState.getFilePath())
        }

        companion object {
            fun create(
                parent: ViewGroup,
            ): IntrudersListItemViewHolder {
                val binding: ItemIntrudersPhotoBinding = ItemIntrudersPhotoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return IntrudersListItemViewHolder(binding)
            }
        }
    }
}