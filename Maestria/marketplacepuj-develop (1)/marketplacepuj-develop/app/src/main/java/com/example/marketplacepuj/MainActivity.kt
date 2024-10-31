package com.example.marketplacepuj

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.marketplacepuj.core.kart.ShoppingCart
import com.example.marketplacepuj.databinding.ActivityMainBinding
import com.example.marketplacepuj.feature.login.viewmodel.LoginViewModel
import com.example.marketplacepuj.feature.login.viewmodel.LoginViewModel.SessionState
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModel<LoginViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)
        observeCartQuantities()

        binding.toolbar.setNavigationOnClickListener {
            findNavController(this, R.id.navHostFragment).navigateUp()
        }
    }

    private fun observeCartQuantities() {
        ShoppingCart.totalQuantity.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach { quantity ->
                val cartMenuItem: MenuItem =
                    binding.navView.menu.findItem(R.id.shoppingCartFragment)
                val badge: BadgeDrawable = binding.navView.getOrCreateBadge(cartMenuItem.itemId)
                badge.number = quantity
            }.launchIn(lifecycleScope)
    }

    private fun setUpNavigation() {
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(this, R.id.navHostFragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.productsFragment, R.id.shoppingCartFragment, R.id.userProfileFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setOnApplyWindowInsetsListener { v, insets ->
            v.updatePadding(bottom = 0)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        observeSessionState()
    }

    private fun observeSessionState() {
        viewModel.currentSession.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach { state ->
                when (state) {
                    SessionState.Init -> {
                        viewModel.checkCurrentLoginSession()
                    }

                    is SessionState.SessionValidation -> handleSessionValidationState(state)
                }
            }.launchIn(lifecycleScope)
    }

    private fun handleSessionValidationState(session: SessionState.SessionValidation) {
        if (session.isValid) {
            setUpNavigation()
        } else {
            handleInvalidSession()
        }
    }

    private fun handleInvalidSession() {
        goToLogin()
    }

    fun goToLogin() {
        val navController = findNavController(this, R.id.navHostFragment)
        navController.navigate(R.id.loginActivity)
    }

    fun setToolbarColor(colorBg: Int, titleColor: Int) {
        binding.toolbar.setBackgroundColor(colorBg)
        binding.toolbar.setTitleTextColor(titleColor)
    }
}
