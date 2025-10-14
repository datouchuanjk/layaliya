package com.module.wallet.viewmodel

import com.module.basic.sp.*
import com.module.basic.viewmodel.*

class DiamondViewModel(
) : BaseViewModel() {

    val userInfo get() = AppGlobal.userResponse
}