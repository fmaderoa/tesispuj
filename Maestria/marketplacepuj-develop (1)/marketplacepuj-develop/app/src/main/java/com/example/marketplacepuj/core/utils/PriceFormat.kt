package com.example.marketplacepuj.core.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object PriceFormat {

    private var valueFormat: DecimalFormat

    init {
        val patternValueFormat = "$ #,###,###,###.####"
        val otherSymbols = DecimalFormatSymbols()
        otherSymbols.setDecimalSeparator(',')
        otherSymbols.setGroupingSeparator('.')
        valueFormat = DecimalFormat(patternValueFormat, otherSymbols)
    }

    fun apply(
        priceValue: Any?,
    ): String {
        return valueFormat.format(priceValue)
    }

    fun getBigDecimalValue(value: String): BigDecimal {
        val stringValue: String = if (value.contains("$")) {
            value.replace("$ ", "").replace(".", "")
        } else {
            val aDouble = value.toDouble()
            if (aDouble % 2 == 0.0) {
                aDouble.toInt().toString()
            } else {
                aDouble.toString()
            }
        }
        val bigDecimal = BigDecimal(stringValue)
        return bigDecimal.setScale(4, RoundingMode.HALF_UP)
    }
}