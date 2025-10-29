package com.module.gift.api.data

data class GiftInfo(
    val userInfo: List<UserInfo>,
    val roomId: String?,
) {
    data class UserInfo(
        val uid: String,
        val avatar: String?,
        val nickname: String,
    )
}
