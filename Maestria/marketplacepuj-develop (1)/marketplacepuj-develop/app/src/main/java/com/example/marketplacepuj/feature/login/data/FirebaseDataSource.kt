package com.example.marketplacepuj.feature.login.data

import com.example.marketplacepuj.core.REALTIME_CATEGORIES_KEY
import com.example.marketplacepuj.core.REALTIME_CATEGORY_NAME_KEY
import com.example.marketplacepuj.core.REALTIME_CATEGORY_STATE_KEY
import com.example.marketplacepuj.core.REALTIME_ORDERS_CATEGORY_KEY
import com.example.marketplacepuj.core.REALTIME_ORDER_DETAIL_KEY
import com.example.marketplacepuj.core.REALTIME_ORDER_ID_KEY
import com.example.marketplacepuj.core.REALTIME_PRODUCTS_KEY
import com.example.marketplacepuj.core.REALTIME_PRODUCT_CATEGORY_KEY
import com.example.marketplacepuj.core.REALTIME_PRODUCT_DESCRIPTION_KEY
import com.example.marketplacepuj.core.REALTIME_PRODUCT_ID_KEY
import com.example.marketplacepuj.core.REALTIME_PRODUCT_IMG_KEY
import com.example.marketplacepuj.core.REALTIME_PRODUCT_NAME_KEY
import com.example.marketplacepuj.core.REALTIME_PRODUCT_PRICE_KEY
import com.example.marketplacepuj.core.REALTIME_PRODUCT_QUANTITY_KEY
import com.example.marketplacepuj.core.REALTIME_PRODUCT_SIZE_VALUES_KEY
import com.example.marketplacepuj.core.REALTIME_PRODUCT_STATE_KEY
import com.example.marketplacepuj.core.REALTIME_PRODUCT_SUBCATEGORY_KEY
import com.example.marketplacepuj.core.REALTIME_SUBCATEGORY_KEY
import com.example.marketplacepuj.core.REALTIME_SUB_CATEGORY_NAME_KEY
import com.example.marketplacepuj.core.REALTIME_SUB_CATEGORY_STATE_KEY
import com.example.marketplacepuj.core.REALTIME_USERS_KEY
import com.example.marketplacepuj.core.REALTIME_USER_ID_KEY
import com.example.marketplacepuj.core.exception.ChangePasswordException
import com.example.marketplacepuj.core.exception.NewOrderException
import com.example.marketplacepuj.core.exception.NoUserFoundException
import com.example.marketplacepuj.core.exception.OrderHistoryException
import com.example.marketplacepuj.core.exception.UpdateProfileException
import com.example.marketplacepuj.feature.cart.data.entity.DetallePedidoItem
import com.example.marketplacepuj.feature.cart.data.entity.DireccionEntrega
import com.example.marketplacepuj.feature.cart.data.entity.OrderEntity
import com.example.marketplacepuj.feature.products.list.view.list.categories.entities.Category
import com.example.marketplacepuj.feature.products.list.view.list.products.entities.Product
import com.example.marketplacepuj.feature.products.list.view.list.subcategories.entities.SubCategory
import com.example.marketplacepuj.feature.profile.domain.entities.ProfileEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.coroutineContext


var pendingFirebaseDeferred: CompletableDeferred<Result<Boolean>>? = null

internal suspend inline fun awaitPendingFirebaseAction(): Result<Boolean> {
    val deferred = CompletableDeferred<Result<Boolean>>(coroutineContext[Job])
    pendingFirebaseDeferred = deferred
    return deferred.await()
}

class FirebaseDataSource {

    private var auth: FirebaseAuth = Firebase.auth
    private val database = FirebaseDatabase.getInstance().reference

    fun isSessionActive(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null
    }

    suspend fun createAccount(username: String, password: String): Result<Boolean> {
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    pendingFirebaseDeferred?.complete(Result.success(true))
                } else {
                    pendingFirebaseDeferred?.complete(Result.failure(task.exception!!))
                }
            }

        return awaitPendingFirebaseAction()
    }

    suspend fun signIn(username: String, password: String): Result<Boolean> {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    pendingFirebaseDeferred?.complete(Result.success(true))
                } else {
                    pendingFirebaseDeferred?.complete(Result.failure(task.exception!!))
                }
            }

        return awaitPendingFirebaseAction()
    }

    suspend fun getCategories(): List<Category>? {
        val categories = database.child(REALTIME_CATEGORIES_KEY)
        return try {
            categories.get().await().children
                .filter { (it.child(REALTIME_CATEGORY_STATE_KEY).value as Long) == 1L }
                .map { dataSnapshot ->
                    val name = dataSnapshot.child(REALTIME_CATEGORY_NAME_KEY).value as String
                    val key = dataSnapshot.key ?: ""
                    val subCategories = dataSnapshot.child(REALTIME_SUBCATEGORY_KEY).children
                        .filter { (it.child(REALTIME_SUB_CATEGORY_STATE_KEY).value as Long == 1L) }
                        .map { subCategory ->
                            val subCategoryName =
                                subCategory.child(REALTIME_SUB_CATEGORY_NAME_KEY).value as String
                            val subCategoryKey = subCategory.key ?: ""
                            SubCategory(subCategoryKey, subCategoryName)
                        }
                    Category(key, name, subCategories)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun getProducts(): List<Product>? {
        val products = database.child(REALTIME_PRODUCTS_KEY)
        return try {
            products.get().await().children
                .filter { (it.child(REALTIME_PRODUCT_STATE_KEY).value as Long) == 1L }
                .map { product ->
                    val name = product.child(REALTIME_PRODUCT_NAME_KEY).value as String
                    val category = product.child(REALTIME_PRODUCT_CATEGORY_KEY).value as String
                    val description =
                        product.child(REALTIME_PRODUCT_DESCRIPTION_KEY).value as String
                    val productId = product.child(REALTIME_PRODUCT_ID_KEY).value as String
                    val price = product.child(REALTIME_PRODUCT_PRICE_KEY).value as Long? ?: 0L
                    val subCategory =
                        product.child(REALTIME_PRODUCT_SUBCATEGORY_KEY).value as String
                    val sizeValues =
                        product.child(REALTIME_PRODUCT_SIZE_VALUES_KEY).value as Map<String, Long>?
                            ?: emptyMap()
                    val img = product.child(REALTIME_PRODUCT_IMG_KEY).value as String
                    val quantity = product.child(REALTIME_PRODUCT_QUANTITY_KEY).value as Long

                    Product(
                        id = productId,
                        name = name,
                        category = category,
                        quantityAvailable = quantity,
                        description = description,
                        price = price.toBigDecimal(),
                        subCategory = subCategory,
                        imageUrl = img,
                        availableSizes = sizeValues
                    )
                }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getCurrentUser() = auth.currentUser

    fun getCurrentProfile() =
        getCurrentUser()?.let { ProfileEntity(it.displayName ?: "", it.email ?: "") }


    private fun generateOrderId(): Int {
        val currentTime = System.currentTimeMillis()
        return currentTime.hashCode() and Int.MAX_VALUE
    }

    suspend fun createOrder(orderEntity: OrderEntity): Result<Boolean> {
        return getCurrentUser()?.let { currentUser ->
            val products = database.child(REALTIME_ORDERS_CATEGORY_KEY)
            val orderKey = products.push().key!!
            try {
                val newOrder = hashMapOf(
                    "idUsuario" to currentUser.uid,
                    "idPedido" to orderKey,
                    "idOrden" to generateOrderId(),
                    "detallePedido" to orderEntity.detallePedido.map { detail ->
                        val map = mapOf(
                            "cantidad" to detail.cantidad,
                            "descuento" to detail.descuento,
                            "idProducto" to detail.idProducto,
                            "monto" to detail.monto,
                            "precioTotalProducto" to detail.precioTotalProducto,
                        )
                        if (detail.talla == null) map else map.plus("talla" to detail.talla)
                    },
                    "direccionEntrega" to mapOf(
                        "calle" to orderEntity.direccionEntrega.calle,
                        "ciudad" to orderEntity.direccionEntrega.ciudad,
                        "codigoPostal" to orderEntity.direccionEntrega.codigoPostal,
                        "complemento" to orderEntity.direccionEntrega.complemento,
                        "numero" to orderEntity.direccionEntrega.numero
                    ),
                    "estado" to orderEntity.estado,
                    "fechaOrden" to orderEntity.fechaOrden,
                    "impuestos" to orderEntity.impuestos,
                    "precioTotal" to orderEntity.precioTotal,
                    "valorNeto" to orderEntity.valorNeto
                )

                products.child(orderKey).setValue(newOrder).await()
                Result.success(true)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(NewOrderException())
            }

        } ?: Result.failure(NoUserFoundException())
    }

    suspend fun closeUserSession(): Result<Boolean> {
        auth.addAuthStateListener { p0 ->
            pendingFirebaseDeferred?.complete(Result.success(p0.currentUser == null))
        }
        auth.signOut()
        return awaitPendingFirebaseAction()
    }

    suspend fun changeUserPassword(password: String): Result<Boolean> {
        getCurrentUser()?.let { firebaseUser ->
            firebaseUser.updatePassword(password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    pendingFirebaseDeferred?.complete(Result.success(true))
                } else {
                    when (task.exception) {
                        is FirebaseAuthRecentLoginRequiredException -> {
                            pendingFirebaseDeferred?.complete(Result.failure(task.exception as FirebaseAuthRecentLoginRequiredException))
                        }

                        else -> {
                            task.exception?.printStackTrace()
                            pendingFirebaseDeferred?.complete(Result.failure(ChangePasswordException()))
                        }
                    }
                }
            }

        }

        return awaitPendingFirebaseAction()
    }

    suspend fun updateProfile(name: String, address: String): Result<Boolean> {
        val user = getCurrentUser() ?: return Result.failure(NoUserFoundException())
        val profileUpdates = userProfileUpdates(name)
        user.updateProfile(profileUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val claims = mapOf("direccion" to address, "nombre" to name)
                val users = database.child(REALTIME_USERS_KEY)
                users.child(user.uid).updateChildren(claims).addOnCompleteListener { update ->
                    if (update.isSuccessful) {
                        pendingFirebaseDeferred?.complete(Result.success(true))
                    } else {
                        update.exception?.printStackTrace()
                        pendingFirebaseDeferred?.complete(Result.failure(UpdateProfileException()))
                    }
                }
            } else {
                task.exception?.printStackTrace()
                pendingFirebaseDeferred?.complete(Result.failure(UpdateProfileException()))
            }
        }

        return awaitPendingFirebaseAction()
    }

    private fun userProfileUpdates(name: String): UserProfileChangeRequest {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
        return profileUpdates.build()
    }

    suspend fun getOrderHistory(): Result<List<OrderEntity>> {
        try {
            val user = getCurrentUser() ?: return Result.failure(NoUserFoundException())
            val orders = database.child(REALTIME_ORDERS_CATEGORY_KEY)
            val history = orders.get()
                .await().children.filter { it.child(REALTIME_USER_ID_KEY).value == user.uid }
                .map {
                    val orderId = it.child(REALTIME_ORDER_ID_KEY).value

                    val orderDetail = it.child(REALTIME_ORDER_DETAIL_KEY).children.map { detail ->
                        val cantidad = detail.child("cantidad").value as Long
                        val descuento = detail.child("descuento").value as Long
                        val idProducto = detail.child("idProducto").value as String
                        val monto = detail.child("monto").value as Long
                        val precioTotalProducto = detail.child("precioTotalProducto").value as Long
                        val talla = detail.child("talla").value as String?
                        DetallePedidoItem(
                            cantidad = cantidad.toInt(),
                            descuento = descuento.toDouble(),
                            idProducto = idProducto,
                            monto = monto.toDouble(),
                            precioTotalProducto = precioTotalProducto.toDouble(),
                            talla = talla
                        )
                    }

                    val address = it.child("direccionEntrega").children.map { orderAddress ->
                        val calle = orderAddress.child("calle").value as String
                        val ciudad = orderAddress.child("ciudad").value as String
                        val codigoPostal = orderAddress.child("codigoPostal").value as String
                        val complemento = orderAddress.child("complemento").value as String
                        val numero = orderAddress.child("numero").value as String
                        DireccionEntrega(
                            calle = calle,
                            ciudad = ciudad,
                            codigoPostal = codigoPostal,
                            complemento = complemento,
                            numero = numero
                        )
                    }

                    val estado = it.child("estado").value as String
                    val fechaOrden = it.child("fechaOrden").value as String
                    val impuestos = it.child("impuestos").value as Long
                    val precioTotal = it.child("precioTotal").value as Long
                    val valorNeto = it.child("valorNeto").value as Long

                    OrderEntity(
                        orderId = orderId.toString(),
                        detallePedido = orderDetail,
                        direccionEntrega = if (address.isEmpty()) DireccionEntrega() else address[0],
                        estado = estado,
                        valorNeto = valorNeto.toDouble(),
                        fechaOrden = fechaOrden,
                        impuestos = impuestos.toDouble(),
                        precioTotal = precioTotal.toDouble(),
                    )

                }

            return Result.success(history)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(OrderHistoryException())
        }
    }
}