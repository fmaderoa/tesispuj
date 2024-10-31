package com.example.marketplacepuj.feature.products.list.view.list.subcategories.exception

class TypeNotSupportedException private constructor(message: String) : RuntimeException(message) {
    companion object {

        fun create(message: String): TypeNotSupportedException {
            return TypeNotSupportedException(message)
        }
    }
}