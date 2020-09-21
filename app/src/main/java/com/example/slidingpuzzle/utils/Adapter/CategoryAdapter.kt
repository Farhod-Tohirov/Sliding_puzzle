package com.example.slidingpuzzle.utils.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slidingpuzzle.model.CategoryData
import com.example.slidingpuzzle.R
import kotlinx.android.synthetic.main.category_item.view.*

class CategoryAdapter(private val data: List<CategoryData>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var selectCourse: ItemListener? = null
    private var selectCategory: ItemListener? = null

    fun setOnCategoryListener(f: ItemListener) {
        selectCourse = f
    }

    fun setOnSelectCategoryListener(f: ItemListener){
        selectCategory = f
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.apply {
                buttonCategory.setOnClickListener {
                    selectCourse?.invoke(adapterPosition)
                }

                itemFull.setOnClickListener {
                    selectCategory?.invoke(adapterPosition)
                }
            }
        }

        fun bind() {
            val d = data[adapterPosition]
            itemView.apply {
                imageCategory.setImageResource(d.image)
                buttonCategory.text = d.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        )

    override fun getItemCount(): Int = data.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()
}

typealias ItemListener = (Int) -> Unit