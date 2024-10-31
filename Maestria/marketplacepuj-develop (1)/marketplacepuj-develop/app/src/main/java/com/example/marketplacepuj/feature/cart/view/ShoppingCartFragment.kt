package com.example.marketplacepuj.feature.cart.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.marketplacepuj.MainActivity
import com.example.marketplacepuj.R
import com.example.marketplacepuj.core.components.dialog.LoadingDialog
import com.example.marketplacepuj.core.components.fragment.BaseFragment
import com.example.marketplacepuj.core.kart.ShoppingCart
import com.example.marketplacepuj.core.kart.entities.ShoppingCartItem
import com.example.marketplacepuj.feature.cart.view.layout.CartItemProvider
import com.example.marketplacepuj.feature.cart.view.layout.CartScreen
import com.example.marketplacepuj.feature.cart.viewmodel.ShoppingCartViewModel
import com.example.marketplacepuj.feature.cart.viewmodel.ShoppingCartViewModel.CreateOrderState
import com.example.marketplacepuj.feature.products.detail.view.theme.MarketPlacePUJTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ShoppingCartFragment : BaseFragment() {

    private val dialog: LoadingDialog by inject { parametersOf(getString(R.string.text_creating_order)) }

    private val viewModel by viewModel<ShoppingCartViewModel>()

    private val showSuccessDialog = mutableStateOf(false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MarketPlacePUJTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        CartScreen(products = ShoppingCart.products,
                            onDeleteClick = { productDeleted ->
                                ShoppingCart.removeProduct(productDeleted)
                            }, createOrder = {
                                viewModel.sendOrder(ShoppingCart.products)
                            }, showSuccessDialog, onDismissSuccessDialog = {
                                findNavController().popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setToolbarColor(0xff6a48bd.toInt(), 0xffffffff.toInt())
        observeCartState()
    }

    private fun observeCartState() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach { state ->
                when (state) {
                    CreateOrderState.Idle -> handleIdleState()
                    CreateOrderState.Loading -> handleLoadingState()
                    CreateOrderState.Success -> handleSuccessState()
                    is CreateOrderState.Error -> handleErrorState()
                }
            }.launchIn(lifecycleScope)
    }

    private fun handleSuccessState() {
        dismissCurrentlyShownDialog()
        showSuccessDialog.value = true
    }

    private fun handleErrorState() {
        dismissCurrentlyShownDialog()
    }

    private fun handleLoadingState() {
        showDialog(dialog, "order_dialog")
    }

    private fun handleIdleState() {
    }
}

@Preview
@Composable
fun CartScreenWithDataPreview(
    @PreviewParameter(CartItemProvider::class) products: ShoppingCartItem,
) {
    MarketPlacePUJTheme {
        val lists = listOf(products, products, products)
        CartScreen(
            products = lists,
            onDeleteClick = { },
            showSuccessDialog = remember { mutableStateOf(false) })
    }
}