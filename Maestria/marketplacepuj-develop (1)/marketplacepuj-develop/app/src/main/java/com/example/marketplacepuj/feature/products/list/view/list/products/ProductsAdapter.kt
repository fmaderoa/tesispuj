package com.example.marketplacepuj.feature.products.list.view.list.products

import android.content.Context
import android.view.ViewGroup
import android.widget.Filter
import androidx.annotation.StringDef
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplacepuj.feature.products.list.view.list.products.entities.Product
import com.example.marketplacepuj.feature.products.list.view.list.products.factory.ProductsTypeFactory
import com.example.marketplacepuj.feature.products.list.view.list.products.factory.ProductsTypeFactoryForList
import com.example.marketplacepuj.feature.products.list.view.list.products.viewholder.AbstractViewHolder
import com.example.marketplacepuj.feature.products.list.view.list.products.viewmodels.AbstractViewModel
import com.example.marketplacepuj.feature.products.list.view.list.products.viewmodels.ProductViewModel
import kotlin.properties.Delegates

class ProductsAdapter :
    RecyclerView.Adapter<AbstractViewHolder<AbstractViewModel>>(),
    AbstractViewHolder.OnItemClickedListener {

    companion object {
        @StringDef(CATEGORY_FILTER, SUBCATEGORY_FILTER, SEARCH_FILTER)
        @Retention(AnnotationRetention.SOURCE)
        annotation class ProductFilter

        const val CATEGORY_FILTER = "CATEGORY_FILTER"
        const val SUBCATEGORY_FILTER = "SUBCATEGORY_FILTER"
        const val SEARCH_FILTER = "SEARCH_FILTER"
    }

    private var categoryFilter = ""
    private var subCategoryFilter = ""
    private var searchFilter = ""

    private var listener: ProductsAdapterListener? = null
    private lateinit var context: Context
    private val typeFactory: ProductsTypeFactory = ProductsTypeFactoryForList()

    var items: List<AbstractViewModel> by Delegates.observable(ArrayList()) { _, _, _ ->
        itemsFiltered = items.toMutableList()
    }

    private var itemsFiltered: MutableList<AbstractViewModel> by Delegates.observable(mutableListOf()) { _, _, _ ->
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: AbstractViewHolder<AbstractViewModel>, position: Int) {
        val item = itemsFiltered[position]
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

    override fun getItemViewType(position: Int): Int = itemsFiltered[position].type(typeFactory)

    override fun getItemCount() = itemsFiltered.count()

    override fun onClickProduct(product: Product) {
        listener?.onClickProduct(product)
    }

    fun setListener(listener: ProductsAdapterListener) {
        this.listener = listener
    }

    fun subCategoryFilter(): Filter {
        searchFilter = ""
        return object : Filter() {
            override fun performFiltering(value: CharSequence): FilterResults {
                var filteredList: MutableList<AbstractViewModel> = ArrayList()
                if (value.isEmpty()) {
                    filteredList.addAll(items)
                } else {
                    subCategoryFilter = value.toString().trim()
                    val filterPattern = value.toString().lowercase().trim()
                    filteredList = items.filter {
                        it is ProductViewModel &&
                                it.product.subCategory.lowercase().contains(filterPattern) &&
                                it.product.category.contains(categoryFilter)
                    } as MutableList<AbstractViewModel>
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                itemsFiltered = results.values as MutableList<AbstractViewModel>
            }
        }
    }

    private fun resetFilters() {
        categoryFilter = ""
        subCategoryFilter = ""
        searchFilter = ""
    }

    fun categoryFilter(): Filter {
        resetFilters()
        return object : Filter() {
            override fun performFiltering(value: CharSequence): FilterResults {
                var filteredList: MutableList<AbstractViewModel> = ArrayList()
                if (value.isEmpty()) {
                    filteredList.addAll(items)
                } else {
                    categoryFilter = value.toString().trim()
                    val filterPattern = value.toString().lowercase().trim()
                    filteredList = items.filter {
                        it is ProductViewModel && it.product.category.lowercase()
                            .contains(filterPattern)
                    } as MutableList<AbstractViewModel>
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                itemsFiltered = results.values as MutableList<AbstractViewModel>
            }
        }
    }

    fun searchFilter(): Filter {
        searchFilter = ""
        return object : Filter() {
            override fun performFiltering(value: CharSequence): FilterResults {
                var filteredList: MutableList<AbstractViewModel> = ArrayList()
                if (value.isEmpty() && categoryFilter.isEmpty() && subCategoryFilter.isEmpty()) {
                    filteredList.addAll(items)
                } else {
                    searchFilter = value.toString().trim()
                    val filterPattern = value.toString().lowercase().trim()
                    filteredList = items.filter {
                        it is ProductViewModel &&
                                (it.product.name.lowercase().trim().contains(filterPattern)
                                        || it.product.description.lowercase().trim()
                                    .contains(filterPattern))

                    } as MutableList<AbstractViewModel>

                    if (categoryFilter.isNotEmpty()) {
                        filteredList = filteredList.filter {
                            it is ProductViewModel && it.product.category.contains(categoryFilter)
                        } as MutableList<AbstractViewModel>
                    }

                    if (subCategoryFilter.isNotEmpty()) {
                        filteredList = filteredList.filter {
                            it is ProductViewModel && it.product.subCategory.contains(
                                subCategoryFilter
                            )
                        } as MutableList<AbstractViewModel>
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                itemsFiltered = results.values as MutableList<AbstractViewModel>
            }
        }
    }

    interface ProductsAdapterListener {
        fun onClickProduct(product: Product)
    }
}

