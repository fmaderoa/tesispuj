package com.example.marketplacepuj.feature.cart.data.entity

data class OrderEntity(
	val detallePedido: List<DetallePedidoItem>,
	val direccionEntrega: DireccionEntrega,
	val estado: String = "pendiente",
	val valorNeto: Double? = null,
	val impuestos: Double? = null,
	val precioTotal: Double? = null,
	val fechaOrden: String? = null,
	val orderId: String? = null,
)

data class DetallePedidoItem(
	val monto: Double? = null,
	val talla: String? = null,
	val descuento: Double? = null,
	val precioTotalProducto: Double? = null,
	val cantidad: Int? = null,
	val idProducto: String? = null,
)

data class DireccionEntrega(
	val complemento: String? = null,
	val codigoPostal: String? = null,
	val numero: String? = null,
	val ciudad: String? = null,
	val calle: String? = null,
)

