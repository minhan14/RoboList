package com.chicohan.samplelistapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chicohan.samplelistapp.R
import com.chicohan.samplelistapp.data.entity.SampleListItem
import com.chicohan.samplelistapp.databinding.ItemMyListItemsBinding

class MyListAdapter(private val onMoreClickCallback: ((item: SampleListItem) -> Unit)? = null) :
    ListAdapter<SampleListItem, MyListAdapter.MyListItemViewHolder>(
        ListDiffCallBack()
    ) {
    class ListDiffCallBack : DiffUtil.ItemCallback<SampleListItem>() {
        override fun areItemsTheSame(oldItem: SampleListItem, newItem: SampleListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SampleListItem, newItem: SampleListItem): Boolean {
            return oldItem == newItem
        }

    }

    inner class MyListItemViewHolder(val binding: ItemMyListItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListItemViewHolder {
        val binding = ItemMyListItemsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyListItemViewHolder, position: Int) {
        with(holder.binding) {
            val myItem = getItem(position)
            root.setOnClickListener {
                onMoreClickCallback?.invoke(myItem)
            }
            val isEven: Boolean = position % 2 == 0
            val myTextColor =
                ContextCompat.getColor(root.context, if (isEven) R.color.dim else R.color.white)
            if (isEven) {
                root.rotation = -4f
//                val layoutParams = root.layoutParams as RecyclerView.LayoutParams
//                layoutParams.width =
//                    (holder.itemView.context.resources.displayMetrics.widthPixels * 1).toInt()
//                root.layoutParams = layoutParams
//                root.translationX =
//                    holder.itemView.context.resources.displayMetrics.widthPixels * 0.02f
                root.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.white))
            } else {
                root.rotation = 2f
                val layoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
                layoutParams.width = RecyclerView.LayoutParams.MATCH_PARENT
                root.layoutParams = layoutParams
                root.translationX = 0f
            }

            txtHeader.apply {
                setTextColor(myTextColor)
                text = myItem.name
            }
            txtBody.text = myItem.description
            txtDate.setTextColor(myTextColor)
            txtBody.setTextColor(myTextColor)
            Glide.with(this.root.context).load(myItem.imageUri).into(ivMyPhoto)
        }
    }

}