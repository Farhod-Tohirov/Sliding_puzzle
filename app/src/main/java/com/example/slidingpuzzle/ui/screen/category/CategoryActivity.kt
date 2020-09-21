package com.example.slidingpuzzle.ui.screen.category

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.slidingpuzzle.utils.Adapter.CategoryAdapter
import com.example.slidingpuzzle.utils.Adapter.CategoryItemAdapter
import com.example.slidingpuzzle.model.CategoryData
import com.example.slidingpuzzle.model.CategoryItemData
import com.example.slidingpuzzle.R
import com.example.slidingpuzzle.ui.screen.main.MainActivity
import kotlinx.android.synthetic.main.activity_category.*

class CategoryActivity : AppCompatActivity() {

    private val categories = ArrayList<CategoryData>()
    private val categoryImages = ArrayList<CategoryItemData>()
    private val categoryAdapter = CategoryAdapter(categories)
    private val categoryImagesAdapter = CategoryItemAdapter(categoryImages)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        listCategory.adapter = categoryAdapter
        listCategory.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        listCategoryItems.adapter = categoryImagesAdapter
        listCategoryItems.layoutManager =
            GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)

        loadViews()

    }

    private fun loadViews() {
        changingWindow.setLockDrag(true)
        fillCategory()
        categoryAdapter.setOnSelectCategoryListener {
            fillItems(it)
            textCategory.text = categories[it].name
            changingWindow.open(true)
        }
        categoryAdapter.setOnCategoryListener {
            fillItems(it)
            textCategory.text = categories[it].name
            changingWindow.open(true)
        }

        categoryImagesAdapter.setOnSelectImageListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("IMAGE", it)
            startActivity(intent)
        }
        backToMain.setOnClickListener { finish() }
        openCategories.setOnClickListener { changingWindow.close(true) }
    }

    private fun fillItems(pos: Int) {
        when (pos) {
            0 -> {
                categoryImages.clear()
                categoryImages.add(CategoryItemData(R.drawable.cat1_original))
                categoryImages.add(CategoryItemData(R.drawable.cat2_original))
                categoryImages.add(CategoryItemData(R.drawable.cat3))
                categoryImages.add(CategoryItemData(R.drawable.cat4))
                categoryImages.add(CategoryItemData(R.drawable.cat5))
                categoryImages.add(CategoryItemData(R.drawable.cat6))
                categoryImagesAdapter.notifyDataSetChanged()
            }

            1 -> {
                categoryImages.clear()
                categoryImages.add(CategoryItemData(R.drawable.animal1_original))
                categoryImages.add(CategoryItemData(R.drawable.animal1))
                categoryImages.add(CategoryItemData(R.drawable.animal2))
                categoryImages.add(CategoryItemData(R.drawable.animal4))
                categoryImages.add(CategoryItemData(R.drawable.animal5))
                categoryImagesAdapter.notifyDataSetChanged()
            }

            2 -> {
                categoryImages.clear()
                categoryImages.add(CategoryItemData(R.drawable.city1))
                categoryImages.add(CategoryItemData(R.drawable.city2))
                categoryImages.add(CategoryItemData(R.drawable.city3))
                categoryImages.add(CategoryItemData(R.drawable.city4))
                categoryImages.add(CategoryItemData(R.drawable.city5))
                categoryImages.add(CategoryItemData(R.drawable.city6))
                categoryImagesAdapter.notifyDataSetChanged()
            }


            3 -> {
                categoryImages.clear()
                categoryImages.add(CategoryItemData(R.drawable.flower1))
                categoryImages.add(CategoryItemData(R.drawable.flower2))
                categoryImages.add(CategoryItemData(R.drawable.flower3))
                categoryImages.add(CategoryItemData(R.drawable.flower4))
                categoryImages.add(CategoryItemData(R.drawable.flower5))
                categoryImagesAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun fillCategory() {
        categories.add(CategoryData(R.drawable.cat1_original, "Cats"))
        categories.add(CategoryData(R.drawable.animal1_original, "Animals"))
        categories.add(CategoryData(R.drawable.city2, "Cities"))
        categories.add(CategoryData(R.drawable.flower4, "Flowers"))
        categoryAdapter.notifyDataSetChanged()
    }
}
