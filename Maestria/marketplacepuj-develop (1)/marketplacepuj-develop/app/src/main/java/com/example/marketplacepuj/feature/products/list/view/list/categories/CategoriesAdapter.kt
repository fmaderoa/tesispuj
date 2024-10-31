package com.example.marketplacepuj.feature.products.list.view.list.categories

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplacepuj.feature.products.list.view.list.categories.entities.Category
import com.example.marketplacepuj.feature.products.list.view.list.categories.factory.CategoriesTypeFactory
import com.example.marketplacepuj.feature.products.list.view.list.categories.factory.CategoriesTypeFactoryForList
import com.example.marketplacepuj.feature.products.list.view.list.categories.viewholder.AbstractViewHolder
import com.example.marketplacepuj.feature.products.list.view.list.categories.viewmodels.AbstractViewModel
import com.example.marketplacepuj.feature.products.list.view.list.subcategories.viewmodels.SubCategoryViewModel
import kotlin.properties.Delegates

class CategoriesAdapter :
    RecyclerView.Adapter<AbstractViewHolder<AbstractViewModel>>(),
    AbstractViewHolder.OnItemClickedListener {

    private var listener: CategoriesAdapterListener? = null
    private lateinit var context: Context
    private val typeFactory: CategoriesTypeFactory = CategoriesTypeFactoryForList()
    private var lastSelectedPosition = -1

    var items: List<AbstractViewModel> by Delegates.observable(ArrayList()) { _, _, _ ->
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: AbstractViewHolder<AbstractViewModel>, position: Int) {
        holder.setOnItemClickListener(this)
        val item = items[position]
        holder.bind(item)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AbstractViewHolder<AbstractViewModel> {
        context = parent.context
        val binding = typeFactory.createViewBinding(parent, viewType)
        return typeFactory.createViewHolder(
            binding,
            viewType
        ) as AbstractViewHolder<AbstractViewModel>
    }

    override fun getItemViewType(position: Int): Int = items[position].type(typeFactory)

    override fun getItemCount() = items.count()

    override fun onCategoryClicked(categories: Category, adapterPosition: Int) {
        if (lastSelectedPosition != -1) {
            items[lastSelectedPosition].isSelected = false
            notifyItemChanged(lastSelectedPosition)
        }
        items[adapterPosition].isSelected = true
        notifyItemChanged(adapterPosition)
        lastSelectedPosition = adapterPosition
        val subCategories = categories.subCategories.map {
            SubCategoryViewModel(it)
        }
        listener?.onSelectCategory(categories.id, subCategories)
    }

    fun setListener(listener: CategoriesAdapterListener) {
        this.listener = listener
    }

    interface CategoriesAdapterListener {
        fun onSelectCategory(name: String, categories: List<SubCategoryViewModel>)
    }
}

