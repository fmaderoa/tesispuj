package com.example.marketplacepuj.core.di

import com.example.marketplacepuj.core.components.dialog.ChangePasswordDialog
import com.example.marketplacepuj.core.components.dialog.EditProfileDialog
import com.example.marketplacepuj.core.components.dialog.LoadingDialog
import com.example.marketplacepuj.feature.cart.data.CreateOrderRepository
import com.example.marketplacepuj.feature.cart.data.ICreateOrder
import com.example.marketplacepuj.feature.cart.domain.CreateOrder
import com.example.marketplacepuj.feature.cart.viewmodel.ShoppingCartViewModel
import com.example.marketplacepuj.feature.login.data.FirebaseDataSource
import com.example.marketplacepuj.feature.login.data.ILoginRepository
import com.example.marketplacepuj.feature.login.data.LoginRepository
import com.example.marketplacepuj.feature.login.domain.CheckLoginUseCase
import com.example.marketplacepuj.feature.login.domain.LoginUseCase
import com.example.marketplacepuj.feature.login.viewmodel.LoginViewModel
import com.example.marketplacepuj.feature.orders.data.IRetrieveOrders
import com.example.marketplacepuj.feature.orders.data.RetrieveOrders
import com.example.marketplacepuj.feature.orders.domain.RetrieveOrdersUseCase
import com.example.marketplacepuj.feature.orders.viewmodels.OrdersViewModel
import com.example.marketplacepuj.feature.products.list.data.IProductsRepository
import com.example.marketplacepuj.feature.products.list.data.ProductsRepository
import com.example.marketplacepuj.feature.products.list.domain.GetCategoriesUseCase
import com.example.marketplacepuj.feature.products.list.domain.GetProductsUseCase
import com.example.marketplacepuj.feature.products.list.viewmodel.ProductsViewModel
import com.example.marketplacepuj.feature.profile.data.IUserProfileRepository
import com.example.marketplacepuj.feature.profile.data.UserProfileRepository
import com.example.marketplacepuj.feature.profile.domain.ChangePasswordUseCase
import com.example.marketplacepuj.feature.profile.domain.CloseSessionUseCase
import com.example.marketplacepuj.feature.profile.domain.GetUserProfileUseCase
import com.example.marketplacepuj.feature.profile.domain.UpdateProfileUseCase
import com.example.marketplacepuj.feature.profile.viewmodel.UserProfileViewModel
import com.example.marketplacepuj.feature.register.data.ISignUpRepository
import com.example.marketplacepuj.feature.register.data.SignUpRepository
import com.example.marketplacepuj.feature.register.domain.SignUpUseCase
import com.example.marketplacepuj.feature.register.viewmodel.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    single { FirebaseDataSource() }
    single<ILoginRepository> { LoginRepository(get()) }
    single { LoginUseCase(get()) }
    single { CheckLoginUseCase(get()) }
    viewModel { LoginViewModel(get(), get()) }
}

val signUpModule = module {
    single { FirebaseDataSource() }
    single<ISignUpRepository> { SignUpRepository(get()) }
    single { SignUpUseCase(get()) }
    viewModel { SignUpViewModel(get()) }
}

val productsModule = module {
    single { FirebaseDataSource() }
    single<IProductsRepository> { ProductsRepository(get()) }
    single<IRetrieveOrders> { RetrieveOrders(get()) }

    single { GetProductsUseCase(get()) }
    single { GetCategoriesUseCase(get()) }
    single { GetUserProfileUseCase(get()) }
    single { CloseSessionUseCase(get()) }
    single { ChangePasswordUseCase(get()) }
    single { UpdateProfileUseCase(get()) }
    single { RetrieveOrdersUseCase(get()) }

    viewModel { ProductsViewModel(get(), get()) }
    viewModel { UserProfileViewModel(get(), get(), get(), get()) }
    viewModel { OrdersViewModel(get()) }

    single<ICreateOrder> { CreateOrderRepository(get()) }
    single<IUserProfileRepository> { UserProfileRepository(get()) }
    single { CreateOrder(get()) }
    viewModel { ShoppingCartViewModel(get()) }
}

val appModule = module {
    factory { (message: String) -> LoadingDialog(message) }
    factory { (listener: ChangePasswordDialog.OnConfirmDialogListener) ->
        ChangePasswordDialog(
            listener
        )
    }
    factory { (listener: EditProfileDialog.OnConfirmDialogListener) ->
        EditProfileDialog(
            listener
        )
    }
}