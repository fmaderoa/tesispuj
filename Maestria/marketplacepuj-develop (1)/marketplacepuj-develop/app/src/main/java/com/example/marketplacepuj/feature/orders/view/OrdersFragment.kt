package com.example.marketplacepuj.feature.orders.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.marketplacepuj.MainActivity
import com.example.marketplacepuj.R
import com.example.marketplacepuj.core.components.dialog.LoadingDialog
import com.example.marketplacepuj.core.components.fragment.BaseFragment
import com.example.marketplacepuj.feature.cart.data.entity.OrderEntity
import com.example.marketplacepuj.feature.orders.view.layout.OrderHistoryScreen
import com.example.marketplacepuj.feature.orders.viewmodels.OrdersViewModel
import com.example.marketplacepuj.feature.products.detail.view.theme.MarketPlacePUJTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class OrdersFragment : BaseFragment() {

    private val viewModel: OrdersViewModel by viewModel<OrdersViewModel>()
    private val loadingDialog: LoadingDialog by inject { parametersOf(getString(R.string.text_loading_orders)) }

    private val data = mutableStateOf(emptyList<OrderEntity>())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MarketPlacePUJTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        OrderHistoryScreen(data) {}
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setToolbarColor(0xffffffff.toInt(), 0xff4D4D4D.toInt())
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach {
                when (it) {
                    OrdersViewModel.OrdersState.Init -> handleInitState()
                    OrdersViewModel.OrdersState.Loading -> handleLoadingState()
                    is OrdersViewModel.OrdersState.Success -> handleSuccessState(it.data)
                    is OrdersViewModel.OrdersState.Error -> handleErrorState()
                }

            }.launchIn(lifecycleScope)
    }

    private fun handleInitState() {
        viewModel.getOrders()
    }

    private fun handleSuccessState(data: List<OrderEntity>) {
        dismissCurrentlyShownDialog()
        this.data.value = data
    }

    private fun handleLoadingState() {
        showDialog(loadingDialog, "orders_dialog")
    }

    private fun handleErrorState() {
        dismissCurrentlyShownDialog()
    }
}