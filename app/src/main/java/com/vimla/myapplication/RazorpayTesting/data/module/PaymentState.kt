package com.vimla.myapplication.RazorpayTesting.data.module

sealed class PaymentState {
    data object Idle : PaymentState()
    data class Success(val paymentId: String) : PaymentState()
    data class Error(val message: String) : PaymentState()

}