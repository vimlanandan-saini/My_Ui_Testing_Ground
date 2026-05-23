package com.vimla.myapplication.RazorpayTesting.ui.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.razorpay.Checkout
import com.vimla.myapplication.RazorpayTesting.data.config.RazorpayConfig
import com.vimla.myapplication.RazorpayTesting.data.module.PaymentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject

class PaymentViewModel : ViewModel() {
    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState = _paymentState.asStateFlow()

    fun startPayment(activity: Activity,amount : Int , description : String="test payment"){
        try {
            val options = JSONObject().apply {
                put("name", "Razorpay Demo App")
                put("description", description)
                put("currency", "INR")
                put("amount",(amount*100).toLong())
                put("theme.color", "#3399cc")
                put("method",JSONObject().apply {
                    put("upi",true)
                    put("qr",true)
                })
                put("upi", JSONObject().apply {
                    put("flow","intent")
                })
                put("readonly", JSONObject().apply {
                    put("contact",false)
                    put("email",false)
                    put("method",false)

                })


            }

            val checkout = Checkout()
            checkout.setKeyID(RazorpayConfig.KEY_ID)
            checkout.open(activity, options)
        }
        catch (e : Exception){
            _paymentState.value = PaymentState.Error(e.message.toString() )
        }
    }

    fun handlePaymentSuccess(paymentId: String) {
        _paymentState.value = PaymentState.Success(paymentId)
    }

    fun handlePaymentError(code : Int,message: String) {
        _paymentState.value = PaymentState.Error(message)
    }
}