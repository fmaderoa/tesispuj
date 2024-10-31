package com.example.marketplacepuj.feature.products.list.view.list.subcategories

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplacepuj.feature.products.list.view.list.subcategories.entities.SubCategory
import com.example.marketplacepuj.feature.products.list.view.list.subcategories.factory.SubCategoryTypeFactory
import com.example.marketplacepuj.feature.products.list.view.list.subcategories.factory.SubCategoryTypeFactoryForList
import com.example.marketplacepuj.feature.products.list.view.list.subcategories.viewholder.AbstractViewHolder
import com.example.marketplacepuj.feature.products.list.view.list.subcategories.viewmodels.AbstractViewModel
import kotlin.properties.Delegates

class SubCategoryAdapter :
    RecyclerView.Adapter<AbstractViewHolder<AbstractViewModel>>(),
    AbstractViewHolder.OnItemClickedListener {

    private var listener: SubCategoryAdapterListener? = null
    private lateinit var context: Context
    private val typeFactory: SubCategoryTypeFactory = SubCategoryTypeFactoryForList()
    private var lastSelectedPosition = -1

    var items: List<AbstractViewModel> by Delegates.observable(ArrayList()) { _, _, _ ->
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: AbstractViewHolder<AbstractViewModel>, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.setOnItemClickListener(this)
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

    override fun onSubCategoryClicked(subCategory: SubCategory, adapterPosition: Int) {
        if (lastSelectedPosition != -1) {
            items[lastSelectedPosition].isSelected = false
            notifyItemChanged(lastSelectedPosition)
        }
        items[adapterPosition].isSelected = true
        notifyItemChanged(adapterPosition)
        lastSelectedPosition = adapterPosition
        listener?.onSelectSubCategory(subCategory.id)
    }

    fun setListener(listener: SubCategoryAdapterListener) {
        this.listener = listener
    }

    interface SubCategoryAdapterListener {
        fun onSelectSubCategory(key: String)
    }
}

