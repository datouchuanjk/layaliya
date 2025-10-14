package com.module.bag.api.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class BagResponse(
    val tabs: List<Tab> = emptyList(),
    val list: Map<String, List<Item>>
) {

    @Keep
    data class Tab(
        val index: String?,
        val name: String?,
    )

    @Keep
    data class Item(
        @SerializedName("expire_time")
        val expireTime: Int?,
        val id: Int,
        val name: String?,
        val pic: String?,
        val price: String?,
        val isSelected: Boolean = false
    )
}