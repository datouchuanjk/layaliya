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
import kotlinx.coroutines.flow.*
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
                PayHelper.pay(activity, result?.googleProductId.toString()) { successful, token ->
                    if (successful) {
                        verify(
                            this,
                            orderNum = result?.orderNum.toString(),
                            purchaseToken = token,
                        )
                    } else {
                        when (token) {
                            PayHelper.PAY_CONNECT_FAILED -> {
                                application.toast(R.string.wallet_pay_connect_failed)
                            }

                            PayHelper.PAY_EMPTY_PRODUCT_FAILED -> {
                                application.toast(R.string.wallet_pay_no_product_failed)
                            }

                            PayHelper.PAY_QUERY_FAILED -> {
                                application.toast(R.string.wallet_pay_query_failed)
                            }

                            PayHelper.PAY_LAUNCH_FAILED -> {
                                application.toast(R.string.wallet_pay_launch_failed)
                            }

                            PayHelper.PAY_CANCEL_FAILED -> {
                                application.toast(R.string.wallet_pay_cancel)
                            }
                        }
                    }

                }
            }
        }
    }

    private fun verify(payHelper: PayHelper, orderNum: String, purchaseToken: String) {
        viewModelScope.launch {
            apiRequest {
                api.verify(VerifyRequest(orderNum = orderNum, purchaseToken = purchaseToken))
                    .checkAndGet()!!.googleProductId!!
            }.apiResponse {
                payHelper.consumePurchase(purchaseToken) {
                    if (it) {
                        application.toast(R.string.wallet_buy_successful)
                    } else {
                        application.toast(R.string.wallet_buy_successful)
                    }
                }
            }
        }
    }
}