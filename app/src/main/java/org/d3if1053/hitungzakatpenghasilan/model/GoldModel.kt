package org.d3if1053.hitungzakatpenghasilan.model

import com.squareup.moshi.Json

data class GoldModel(
    @field:Json(name = "statusCode") val statusCode: Double = 0.0,
    @field:Json(name = "status") val status: String = "",
    @field:Json(name = "data") val data: Current,

    )

data class Current(
    @field:Json(name = "current") val current: PriceDetail
)

data class PriceDetail(
    @field:Json(name = "midPrice") val midPrice: Double = 0.0,
    @field:Json(name = "sell") val sell: Double = 0.0,
    @field:Json(name = "buy") val buy: Double = 0.0,
    @field:Json(name = "installment") val installment: Double = 0.0,
    @field:Json(name = "updated_at") val updatedAt: String = ""
)
