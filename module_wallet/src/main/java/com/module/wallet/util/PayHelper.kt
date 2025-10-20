package com.module.wallet.util

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams

class PayHelper(private val context: Context) : PurchasesUpdatedListener {
    private lateinit var billingClient: BillingClient

    init {
        Log.e("1234", "这PayHelper")
        initializeBilling()
    }

    private var isConnection = false
    private fun initializeBilling(count: Int = 1) {
        Log.e("1234", "这是第${count}次")
        billingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases()
            .build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    isConnection = true
                    Log.e("1234", "这是第${count}次 成功了")
                } else {
                    isConnection = false
                    Log.e("1234", "这是第${count}次 失败了 ${billingResult.debugMessage}")
                    initializeBilling(count + 1)
                }
            }

            override fun onBillingServiceDisconnected() {
                isConnection = false
                Log.e("1234", "这是第${count}次 失败了")
                initializeBilling(count + 1)
            }
        }
        )
    }

    private var _result: ((String?) -> Unit)? = null
    suspend fun pay(activity: Activity, productId: String, onResult: (String?) -> Unit) {
        if (!isConnection) {
            Toast.makeText(activity, "onBillingServiceDisconnected", Toast.LENGTH_SHORT)
                .show()
            return
        }
        Log.e("1234", "开始支付产品id= $productId")
        _result = onResult
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            ).build()
        Log.e("1234", "构建产品支付参数完成")
        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.e("1234", "很明显 产品查询成功了")
                if (productDetailsList.isEmpty()) {
                    Toast.makeText(activity, "product is null", Toast.LENGTH_SHORT)
                        .show()
                    return@queryProductDetailsAsync
                }
                val billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(
                        listOf(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetailsList[0])
                                .build()
                        )
                    )
                    .build()
                billingClient.launchBillingFlow(activity, billingFlowParams)
            } else {
                Log.e(
                    "1234",
                    "很明显 产品查询失败了 ${billingResult.debugMessage} ${billingResult.responseCode}"
                )
                Toast.makeText(
                    activity,
                    "很明显，我炸了 debugMessage=${billingResult.debugMessage} responseCode=${billingResult.responseCode}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: List<Purchase?>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                if (purchase!!.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                    val params = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()

                    billingClient.acknowledgePurchase(params) { billingResult ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            _result?.invoke(purchase.purchaseToken)
                        } else {
                            _result?.invoke(
                                null
                            )
                        }
                    }
                }
            }
        }
    }
}