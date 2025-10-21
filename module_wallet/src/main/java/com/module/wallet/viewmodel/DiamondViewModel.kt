package com.module.wallet.viewmodel

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.helper.develop.paging.LoadResult
import com.helper.develop.paging.buildPaging
import com.helper.develop.util.toast
import com.module.basic.api.ApiException
import com.module.basic.api.service.BasicApiService
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
    private val basicApi: BasicApiService,
    private val application: Application,
) : BaseViewModel() {

    val userInfo get() = AppGlobal.userResponse

    private var _payHelper: PayHelper? = null

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
        Log.e("PayHelper", "我点击了购买 开始创建订单")
        val id = pagingData.find { it.isSelected }?.id ?: return
        viewModelScope.launch {
            apiRequest {
                api.buy(BuyRequest(id.toString())).checkAndGet()!!.apply {
                    Log.e("PayHelper", "订单号是多少  ${orderNum.toString()}")
                } //创建订单接口
            }.apiResponse { result ->
                Log.e(
                    "PayHelper",
                    "跳去支付之前 先确定我这次购买的订单号是多少 ${result.orderNum}"
                )
                _payHelper = PayHelper.pay(
                    activity,
                    result.googleProductId.toString()
                ) { successful, token ->
                    Log.e(
                        "PayHelper",
                        "支付完成 我回来了 这里我先输出 我购买接口 返回给我的订单号 ${result.orderNum}"
                    )
                    if (successful) {
                        Log.e("PayHelper", "支付成功 开始调用验证接口")
                        verify(
                            orderNum = result.orderNum.toString(),
                            purchaseToken = token,
                        )
                    } else {
                        _payHelper?.end()
                        _payHelper = null
                        Log.e(
                            "PayHelper",
                            "支付失败 这个时候应该有toast 假如我是成功的 那么orderNum=${result.orderNum} token= 没有token 只有错误码"
                        )
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

    private fun verify(orderNum: String, purchaseToken: String) {
        viewModelScope.launch {
            Log.e(
                "PayHelper",
                "开始调用验证接口 orderNum =$orderNum token=$purchaseToken"
            )
            apiRequest {
                api.verify(VerifyRequest(orderNum = orderNum, purchaseToken = purchaseToken))
                    .checkAndGet()!!.googleProductId!!
                Log.e(
                    "PayHelper",
                    "验证接口调用成功  开始调用用户信息接口刷新砖石 现在的数量为${userInfo?.diamond}"
                )
                AppGlobal.userResponse(basicApi.user().checkAndGet())
                Log.e("PayHelper", "用户信息接口调用成功  砖石数量为${userInfo?.diamond}")
            }.apiResponse(catch = { a, b ->
                Log.e(
                    "PayHelper",
                    "验证接口或者是用户信息接口报错 msg =  ${a.message}"
                )
                b()
            }) {
                Log.e(
                    "PayHelper",
                    "出现toast 购买成功"
                )
                application.toast(R.string.wallet_buy_successful)
                Log.e(
                    "PayHelper",
                    "异步调用消费接口，可能报错 但是不影响 token=${purchaseToken} "
                )
                _payHelper?.consumePurchase(purchaseToken)
                _payHelper?.end()
                _payHelper = null
                Log.e(
                    "PayHelper",
                    "异步调用消费接口，断开了链接 不会出现多次请求 "
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _payHelper?.end()
        _payHelper =null
    }
}