package com.base.presentation.utils

import java.math.RoundingMode
import java.text.DecimalFormat


/**
 * Convert memory
 *
 * @param finalMoney
 * @return
 */
fun transMoney(finalMoney: Float): String {
    val formatter = DecimalFormat()
    formatter.maximumFractionDigits = 2
    formatter.groupingSize = 0
    formatter.roundingMode = RoundingMode.FLOOR
    return formatter.format(finalMoney)
}