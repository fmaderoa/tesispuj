package com.example.marketplacepuj.feature.cart.domain

import com.example.marketplacepuj.core.components.usecase.AbstractUseCase
import com.example.marketplacepuj.core.kart.ShoppingCart
import com.example.marketplacepuj.core.kart.entities.ShoppingCartItem
import com.example.marketplacepuj.core.utils.DateFormat
import com.example.marketplacepuj.feature.cart.data.ICreateOrder
import com.example.marketplacepuj.feature.cart.data.entity.DetallePedidoItem
import com.example.marketplacepuj.feature.cart.data.entity.DireccionEntrega
import com.example.marketplacepuj.feature.cart.data.entity.OrderEntity

class CreateOrder(private val repository: ICreateOrder) :
    AbstractUseCase<List<ShoppingCartItem>, Result<Boolean>>() {

    override suspend fun internalExecute(): Result<Boolean> {
        return repository.createOrder(toOrderEntity(request!!)).also {
            if (it.isSuccess && it.getOrNull() == true) {
                ShoppingCart.clear()
            }
        }
    }

    private fun toOrderEntity(request: List<ShoppingCartItem>): OrderEntity {
        val orderDetail = request.map {
            DetallePedidoItem(
                monto = it.price.toDouble(),
                talla = it.classification,
                descuento = 0.0,
                precioTotalProducto = it.totalPrice().toDouble(),
                cantidad = it.quantity.toInt(),
                idProducto = it.productId
            )
        }
        return OrderEntity(
            detallePedido = orderDetail,
            direccionEntrega = DireccionEntrega(
                complemento = null,
                codigoPostal = null,
                numero = null,
                ciudad = null,
                calle = null
            ),
            valorNeto = ShoppingCart.totalPrice().toDouble(),
            impuestos = 0.0,
            precioTotal = ShoppingCart.totalPrice().toDouble(),
            fechaOrden = DateFormat.getCurrentDateFormatted(),
        )
    }
}