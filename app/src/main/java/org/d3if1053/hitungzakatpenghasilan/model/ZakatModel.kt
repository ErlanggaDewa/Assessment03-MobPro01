package org.d3if1053.hitungzakatpenghasilan.model

data class ZakatModel(
    val hargaEmas: Long = 0,
    val gajiBulanan: Long = 0,
    val bonusGaji: Long = 0,
    var totalZakat: Long = 0,
    var outputZakat: String = "",

    )
