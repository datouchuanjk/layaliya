package com.module.gift.api.data.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
internal data class GiftResponse(
    val list: Map<String, List<Item>> = emptyMap(),
    val tabs: List<Tab> = emptyList()
) {
    @Keep
    data class Tab(
        val index: String?,
        val name: String?
    )

    @Keep
    data class Item(
        val disable: Int?,
        val id: Int,
        val name: String?,
        val price: String?,
        val isSelected: Boolean,
        @SerializedName("floating_screen_id")
        val floatingScreenId: Int,
    )
}