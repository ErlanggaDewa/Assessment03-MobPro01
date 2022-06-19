package org.d3if1053.hitungzakatpenghasilan.model

import com.squareup.moshi.Json

data class UserModel(
    @field:Json(name = "avatar_url") val avatar_url: String = "",
    @field:Json(name = "html_url") val html_url: String = "",
    @field:Json(name = "name") val name: String = "",
    @field:Json(name = "company") val company: String = "",
    @field:Json(name = "location") val location: String = "",
    @field:Json(name = "bio") val bio: String = "",
    @field:Json(name = "blog") val blog: String = ""
)
