package com.base.presentation.ui.language.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.base.presentation.R
import com.base.presentation.databinding.ItemLanguageStartBinding
import com.base.presentation.ui.language.model.LanguageModel

class LanguageAdapter(
    private val context: Context,
    private val languageModelList: List<LanguageModel> = listOf(),
    private val itemSelected: (code: String) -> Unit,
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding = ItemLanguageStartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LanguageViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val languageModel = languageModelList[position]
        holder.tvLang.text = languageModel.name
        if (languageModel.active) {
            holder.layoutItem.background = ContextCompat.getDrawable(context, R.drawable.bg_border_item_lang_selected)
        } else {
            holder.layoutItem.background = ContextCompat.getDrawable(context, R.drawable.bg_border_item_lang_normal)
            holder.tvLang.setTextColor(context.resources.getColor(R.color.black, null))
        }

        holder.ivFlag.setImageResource(languageModel.flag)


        holder.layoutItem.setOnClickListener {
            setCheck(languageModel.code)
            itemSelected.invoke(languageModel.code)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return languageModelList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCheck(code: String) {
        for (item in languageModelList) {
            item.active = item.code == code
        }
        notifyDataSetChanged()
    }

    inner class LanguageViewHolder(val binding: ItemLanguageStartBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvLang: AppCompatTextView = binding.tvLang
        val layoutItem: LinearLayoutCompat = binding.layoutItemLanguage
        val ivFlag: ImageView = binding.ivFlag
    }
}
