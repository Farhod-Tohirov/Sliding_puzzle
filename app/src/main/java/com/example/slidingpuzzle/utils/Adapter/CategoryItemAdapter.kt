package com.example.slidingpuzzle.utils.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slidingpuzzle.model.CategoryItemData
import com.example.slidingpuzzle.R
import kotlinx.android.synthetic.main.category_item_images.view.*

class CategoryItemAdapter(private val data: List<CategoryItemData>) :
    RecyclerView.Adapter<CategoryItemAdapter.ViewHolder>() {

    private var selectImage: ImageListener? = null

    fun setOnSelectImageListener(f: ImageListener) {
        selectImage = f
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val d = data[adapterPosition]
            itemView.apply {
                imageCategoryItem.setImageResource(d.image)
                imageCategoryItem.setOnClickListener {
                    selectImage?.invoke(d.image)
                    Log.d("T12T","${it.id}")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.category_item_images, parent, false)
        )

    override fun getItemCount(): Int = data.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()
}

typealias ImageListener = (Int) -> Unit