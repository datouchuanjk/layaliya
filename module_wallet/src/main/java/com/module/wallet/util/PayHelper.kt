package com.module.wallet.util

import android.app.Activity
import android.util.Log
import com.android.billingclient.api.*
import com.helper.develop.util.toast
import com.module.wallet.R
import kotlinx.coroutines.*
import kotlin.coroutines.*

class PayHelper private constructor(
    private val context: Activity,
    private val productId: String,
    private val onResult: (String) -> Unit
) : PurchasesUpdatedListener {

    companion object {
        suspend fun pay(
            context: Activity,
            productId: String,
            onResult: (String) -> Unit
        ) {
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
                        if (b.isCompleted) {
                            return
                        }
                        b.resume(true)
                    } else {
                        if (b.isCompleted) {
                            return
                        }
                        b.resume(false)
                    }
                }

                override fun onBillingServiceDisconnected() {
                    if (b.isCompleted) {
                        return
                    }
                    b.resume(false)
                }
            })
        }
    }

    private var isClose: Boolean = false

    private suspend fun pay() {
        var isConnection = false
        var connectCount = 0
        while (!isConnection && connectCount < 5) {
            isConnection = startConnection()
            connectCount++
        }
        if (!isConnection) {
            context.toast(R.string.wallet_pay_connect_failed)
            close()
            return
        }
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            ).build()
        val productDetailsList = suspendCancellableCoroutine { b ->
            billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    b.resume(productDetailsList)
                } else {
                    b.resume(null)
                }
            }
        }
        if (productDetailsList.isNullOrEmpty()) {
            context.toast(R.string.wallet_pay_query_failed)
            close()
            return
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
        val result = billingClient.launchBillingFlow(context, billingFlowParams)
        if (result.responseCode != BillingClient.BillingResponseCode.OK) {
            context.toast(R.string.wallet_pay_launch_failed)
            close()
            return
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: List<Purchase?>?
    ) {
        if (isClose) {
            return
        }
        isClose = true
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                if (purchase!!.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                    val params = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()

                    billingClient.acknowledgePurchase(params) { billingResult ->
                        close()
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            onResult.invoke(purchase.purchaseToken)
                        } else {
                            context.toast(R.string.wallet_pay_cancel)
                        }
                    }
                }
            }
        } else {
            context.toast(R.string.wallet_pay_cancel)
            if (billingClient.isReady) {
                billingClient.endConnection()
            }
        }
    }

    private fun close() {
        isClose = true
        if (billingClient.isReady) {
            billingClient.endConnection()
        }
    }
}