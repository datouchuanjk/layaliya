package com.module.wallet.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.viewModelScope
import com.helper.develop.paging.LoadResult
import com.helper.develop.paging.buildPaging
import com.helper.develop.util.toast
import com.module.wallet.R
import com.module.basic.sp.*
import com.module.basic.viewmodel.*
import com.module.wallet.api.data.request.BuyRequest
import com.module.wallet.api.data.request.VerifyRequest
import com.module.wallet.api.data.response.DiamondListResponse
import com.module.wallet.api.service.WalletApiService
import com.module.wallet.util.PayHelper
import kotlinx.coroutines.launch

class DiamondViewModel(
    private val api: WalletApiService,
    private val application: Application,
) : BaseViewModel() {

    val userInfo get() = AppGlobal.userResponse


    fun selected(response: DiamondListResponse) {
        pagingData.handle {
            forEachIndexed { index, item ->
                if (item.id == response.id) {
                    set(index, item.copy(isSelected = true))
                } else {
                    set(index, item.copy(isSelected = false))
                }
            }
        }
    }

    val pagingData = buildPaging(viewModelScope) {
        LoadResult(
            null,
            api.getDiamondList().checkAndGet()
        )
    }.pagingData

    fun buy(activity: Activity) {
        val id = pagingData.find { it.isSelected }?.id ?: return
        viewModelScope.launch {
            apiRequest {
                api.buy(BuyRequest(id.toString())).checkAndGet()
            }.apiResponse { result ->
                PayHelper.pay(activity, result?.googleProductId.toString()) { token ->
                    verify(
                        orderNum = result?.orderNum.toString(),
                        purchaseToken = token.toString(),
                    )
                }
            }
        }
    }

    private fun verify(orderNum: String, purchaseToken: String) {
        viewModelScope.launch {
            apiRequest {
                api.verify(VerifyRequest(orderNum = orderNum, purchaseToken = purchaseToken))
                    .checkAndGet()!!.googleProductId!!
            }.apiResponse {
                application.toast(R.string.wallet_buy_successful)
            }
        }
    }
}