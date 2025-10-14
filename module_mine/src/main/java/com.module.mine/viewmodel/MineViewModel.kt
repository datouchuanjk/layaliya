package com.module.mine.viewmodel

import com.module.basic.sp.AppGlobal
import com.module.basic.viewmodel.BaseViewModel

internal class MineViewModel : BaseViewModel() {

    val userInfo  get() =  AppGlobal.userResponse

}