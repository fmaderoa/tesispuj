package com.example.marketplacepuj.feature.products.list.view.list.categories.exception

class TypeNotSupportedException private constructor(message: String) : RuntimeException(message) {
    companion object {

        fun create(message: String): TypeNotSupportedException {
            return TypeNotSupportedException(message)
        }
    }
}