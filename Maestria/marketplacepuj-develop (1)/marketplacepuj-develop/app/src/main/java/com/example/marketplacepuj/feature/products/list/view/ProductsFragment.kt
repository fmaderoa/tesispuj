package com.example.marketplacepuj.feature.products.list.view

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.marketplacepuj.MainActivity
import com.example.marketplacepuj.R
import com.example.marketplacepuj.core.components.dialog.LoadingDialog
import com.example.marketplacepuj.core.components.fragment.BaseFragment
import com.example.marketplacepuj.databinding.FragmentProductsBinding
import com.example.marketplacepuj.feature.products.list.view.list.categories.CategoriesAdapter
import com.example.marketplacepuj.feature.products.list.view.list.categories.viewmodels.AbstractViewModel
import com.example.marketplacepuj.feature.products.list.view.list.products.ProductsAdapter
import com.example.marketplacepuj.feature.products.list.view.list.products.entities.Product
import com.example.marketplacepuj.feature.products.list.view.list.subcategories.SubCategoryAdapter
import com.example.marketplacepuj.feature.products.list.view.list.subcategories.viewmodels.SubCategoryViewModel
import com.example.marketplacepuj.feature.products.list.viewmodel.ProductsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import com.example.marketplacepuj.feature.products.list.view.list.products.viewmodels.AbstractViewModel as ProductsAbstractViewModel

class ProductsFragment : BaseFragment(), CategoriesAdapter.CategoriesAdapterListener,
    SubCategoryAdapter.SubCategoryAdapterListener, ProductsAdapter.ProductsAdapterListener {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductsViewModel by viewModel<ProductsViewModel>()
    private val dialog: LoadingDialog by inject { parametersOf(getString(R.string.text_loading_products)) }

    private val searchCountDown = object : CountDownTimer(1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {
            applyFilter(binding.edtSearch.text.toString(), ProductsAdapter.SEARCH_FILTER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setToolbarColor(0xffffffff.toInt(), 0xff4D4D4D.toInt())
        observeProductsState()
    }

    private fun observeProductsState() {
        viewModel.productsState.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach { state ->
                when (state) {
                    is ProductsViewModel.ProductsState.Error -> handleErrorState()
                    is ProductsViewModel.ProductsState.SuccessProducts -> handleSuccessProductsState(
                        state.products
                    )
                }
            }.launchIn(lifecycleScope)

        viewModel.categoriesState.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach { state ->
                when (state) {
                    ProductsViewModel.CategoriesState.Init -> handleInitState()
                    ProductsViewModel.CategoriesState.Loading -> handleLoadingState()
                    is ProductsViewModel.CategoriesState.SuccessCategories -> handleSuccessCategoriesState(
                        state.categories
                    )

                    is ProductsViewModel.CategoriesState.Error -> handleErrorState()
                }
            }.launchIn(lifecycleScope)
    }

    private fun handleSuccessProductsState(products: List<ProductsAbstractViewModel>) {
        enableSearch(true)
        dismissCurrentlyShownDialog()
        binding.rvProducts.adapter = ProductsAdapter().apply {
            items = products
            setListener(this@ProductsFragment)
        }
    }

    private fun enableSearch(enable: Boolean) {
        binding.edtSearch.isEnabled = enable
    }

    private fun handleErrorState() {
        enableSearch(false)
        dismissCurrentlyShownDialog()
        showSnackbar("Error loading products")
    }

    private fun handleSuccessCategoriesState(categories: List<AbstractViewModel>) {
        binding.rvCategories.adapter = CategoriesAdapter().apply {
            items = categories.map { it.isSelected = false; it }
            setListener(this@ProductsFragment)
        }
        viewModel.loadProducts()
    }

    private fun handleLoadingState() {
        showDialog(dialog, "products_dialog")
    }

    private fun handleInitState() {
        viewModel.loadCategories()
        binding.edtSearch.addTextChangedListener {
            searchCountDown.cancel()
            searchCountDown.start()
        }
    }

    override fun onSelectCategory(name: String, categories: List<SubCategoryViewModel>) {
        binding.tvSubCategory.text = name
        binding.rvSubCategories.adapter = SubCategoryAdapter().apply {
            items = categories
            setListener(this@ProductsFragment)
        }
        applyFilter(name, ProductsAdapter.CATEGORY_FILTER)
    }

    private fun clearSearch() {
        binding.edtSearch.setText("")
    }

    private fun applyFilter(
        key: String,
        @ProductsAdapter.Companion.ProductFilter filterType: String,
    ) {
        val filter = when (filterType) {
            ProductsAdapter.CATEGORY_FILTER -> {
                clearSearch()
                (binding.rvProducts.adapter as ProductsAdapter).categoryFilter()
            }

            ProductsAdapter.SUBCATEGORY_FILTER -> {
                clearSearch()
                (binding.rvProducts.adapter as ProductsAdapter).subCategoryFilter()
            }

            ProductsAdapter.SEARCH_FILTER -> {
                (binding.rvProducts.adapter as ProductsAdapter).searchFilter()
            }

            else -> null
        }
        filter?.filter(key)
    }

    override fun onSelectSubCategory(key: String) {
        applyFilter(key, ProductsAdapter.SUBCATEGORY_FILTER)
    }

    override fun onClickProduct(product: Product) {
        findNavController().navigate(
            ProductsFragmentDirections.actionProductsFragmentToProductDetailActivity(product)
        )
    }
}