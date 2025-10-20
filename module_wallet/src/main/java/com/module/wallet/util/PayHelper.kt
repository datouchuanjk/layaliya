package com.module.wallet.util

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.*
import com.helper.develop.util.*
import com.module.wallet.*
import kotlinx.coroutines.*
import kotlin.coroutines.*

class PayHelper private constructor(
    private val context: Activity,
    private val productId: String,
    private val onResult: (String?) -> Unit
) :
    PurchasesUpdatedListener {

    companion object {
        suspend fun pay(context: Activity, productId: String, onResult: (String?) -> Unit) {
            PayHelper(context, productId, onResult).pay()
        }
    }


    private var billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enablePrepaidPlans()
                .enableOneTimeProducts()
                .build()
        )
        .build()


    private suspend fun startConnection(): Boolean {
        return suspendCancellableCoroutine { b ->
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        b.resume(true)
                    } else {
                        b.resume(false)
                    }
                }

                override fun onBillingServiceDisconnected() {
                    b.resume(false)
                }
            })
        }
    }


    private suspend fun pay() {
        Log.e("PayHelper", "开始支付  包名${context.packageName} sha1 =${context.SHA1}")

        Log.e("PayHelper", "开始支付  产品id= $productId")
        var isConnection = false
        var connectCount = 0
        while (!isConnection && connectCount < 5) {
            Log.e("PayHelper", "开始循环连接 这是第${connectCount}次")
            isConnection = startConnection()
            connectCount++
        }
        if (!isConnection) {
            Log.e("PayHelper", "5次了 还是没有连接上，支付失败")
            context.toast(R.string.wallet_pay_connect_failed)
            return
        }
        Log.e("PayHelper", "连接成功 开始构建查询参数")
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            ).build()
        Log.e("PayHelper", "开始查询产品")
        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.e("PayHelper", "产品查询成功")
                if (productDetailsList.isEmpty()) {
                    Log.e("PayHelper", "产品查询成功 但是是空的 支付失败")
                    Toast.makeText(context, "product is null", Toast.LENGTH_SHORT)
                        .show()
                    return@queryProductDetailsAsync
                }
                Log.e("PayHelper", "开始构建支付参数")
                val billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(
                        listOf(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetailsList[0])
                                .build()
                        )
                    )
                    .build()
                Log.e("PayHelper", "开始支付，拉起谷歌支付")
                val result = billingClient.launchBillingFlow(context, billingFlowParams)
                if (result.responseCode != BillingClient.BillingResponseCode.OK) {
                    Log.e(
                        "PayHelper",
                        "开始支付，拉起失败 code=${result.responseCode} msg=${result.debugMessage}"
                    )
                    context.toast(R.string.wallet_pay_query_failed)
                } else {
                    Log.e("PayHelper", "拉起成功 这个时候应该出现谷歌支付界面")
                }
            } else {
                Log.e(
                    "PayHelper",
                    " 产品查询失败 code=${billingResult.responseCode} msg=${billingResult.debugMessage}"
                )
                context.toast(R.string.wallet_pay_query_failed)
            }
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: List<Purchase?>?
    ) {
        Log.e("PayHelper", " 支付回来了，")
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                if (purchase!!.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                    val params = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()

                    billingClient.acknowledgePurchase(params) { billingResult ->
                        Log.e(
                            "PayHelper",
                            " 支付回来了，拿到token ${purchase.purchaseToken} code=${billingResult.responseCode}"
                        )
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            onResult.invoke(purchase.purchaseToken)
                        } else {
                            onResult.invoke(
                                null
                            )
                        }
                    }
                }
            }
        } else {
            Log.e("PayHelper", " 支付失败 或者是取消了把")
            context.toast(R.string.wallet_pay_buy_failed)
        }
    }
}