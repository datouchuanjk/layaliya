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
        Log.e("1234","这PayHelper")
        initializeBilling()
    }
    private var isConnection = false
    private  fun initializeBilling(count: Int = 1) {
        Log.e("1234","这是第${count}次")
        billingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases()
            .build()
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        Log.e("1234","这是第${count}次 成功了")
                    }else{
                        Log.e("1234","这是第${count}次 失败了 ${billingResult.responseCode}")
                    }
                }

                override fun onBillingServiceDisconnected() {
                    Log.e("1234","这是第${count}次 失败了")
                    initializeBilling()
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
        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
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
                            _result?.invoke( purchase.purchaseToken)
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