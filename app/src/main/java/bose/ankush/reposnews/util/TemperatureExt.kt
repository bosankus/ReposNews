package bose.ankush.reposnews.util

import kotlin.math.roundToInt


fun Double.toCelsius(): String = (this - KELVIN_CONSTANT).roundToInt().toString()