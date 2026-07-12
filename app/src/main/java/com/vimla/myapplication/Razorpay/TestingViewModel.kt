package com.vimla.myapplication.Razorpay

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class UpiApp(
    val name: String,
    val packageName: String
)


class TestingViewModel : ViewModel() {

    var amount by mutableStateOf("100")
        private set

    var selectedUpiApp by mutableStateOf<UpiApp?>(null)
        private set

    var installedUpiApps by mutableStateOf<List<UpiApp>>(emptyList())
        private set

    fun onAmountChange(newValue: String) {
        if (newValue.all { it.isDigit() } && newValue.length <= 6) {
            amount = newValue
        }
    }

    fun updateInstalledUpiApps(apps: List<UpiApp>) {
        installedUpiApps = apps
        if (selectedUpiApp == null && apps.isNotEmpty()) {
            selectedUpiApp = apps.first()
        }
    }

    fun selectUpiApp(app: UpiApp) {
        selectedUpiApp = app
    }
}