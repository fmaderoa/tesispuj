package com.example.marketplacepuj.ui.producto.data.entities

data class Producto(
    val idProducto: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val url_imagen: String,
    val activo: Int,
    val cantidadDisponible: Int,
    val categoria: String,
    val subcategoria: String,
    val tallas: Map<String, Int>? = null
) {
    // Constructor vacío necesario para Firebase
    constructor() : this("", "", "", 0.0, "", 0, 0, "", "", null)
}
