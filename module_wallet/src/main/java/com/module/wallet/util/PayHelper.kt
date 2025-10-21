package com.module.wallet.util

import android.app.Activity
import android.util.Log
import com.android.billingclient.api.*
import kotlinx.coroutines.*
import kotlin.coroutines.*

class PayHelper private constructor(
    private val context: Activity,
    private val productId: String,
    private val onResult: PayHelper.(Boolean, String) -> Unit
) :
    PurchasesUpdatedListener {

    companion object {
        const val PAY_CONNECT_FAILED = "PAY_CONNECT_FAILED"
        const val PAY_EMPTY_PRODUCT_FAILED = "PAY_EMPTY_PRODUCT_FAILED"
        const val PAY_QUERY_FAILED = "PAY_QUERY_FAILED"
        const val PAY_LAUNCH_FAILED = "PAY_LAUNCH_FAILED"
        const val PAY_CANCEL_FAILED = "PAY_CANCEL_FAILED"
        suspend fun pay(
            context: Activity,
            productId: String,
            onResult: PayHelper.(Boolean, String) -> Unit
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
        var isConnection = false
        var connectCount = 0
        while (!isConnection && connectCount < 5) {
            isConnection = startConnection()
            connectCount++
        }
        if (!isConnection) {
            onResult(false, PAY_CONNECT_FAILED)
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
        if (productDetailsList == null) {
            onResult(false, PAY_QUERY_FAILED)
            return
        }
        if (productDetailsList.isEmpty()) {
            onResult(false, PAY_EMPTY_PRODUCT_FAILED)
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
            onResult(false, PAY_LAUNCH_FAILED)
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
                            onResult.invoke(this,true, purchase.purchaseToken)
                        } else {
                            onResult(false, PAY_CANCEL_FAILED)
                        }
                    }
                }
            }
        } else {
            onResult(false, PAY_CANCEL_FAILED)
        }
    }

      suspend fun consumePurchase(token: String) :Boolean{
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(token)
            .build()

      return suspendCancellableCoroutine { b->
          billingClient.consumeAsync(consumeParams) { billingResult, _ ->
              Log.e("PayHelper", "billingResult ${billingResult.responseCode} ${billingResult.debugMessage}？")
              b.resume(billingResult.responseCode == BillingClient.BillingResponseCode.OK)
          }
      }
    }
}