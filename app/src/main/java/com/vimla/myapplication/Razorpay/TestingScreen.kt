package com.vimla.myapplication.Razorpay

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun TestingScreen(
    viewModel: TestingViewModel,
    onPayWithRazorpay: (String) -> Unit,
    onPayWithUpiApp: (String, UpiApp) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Razorpay + UPI Test Payment",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "Enter amount in INR, then either open Razorpay checkout or directly choose an installed UPI app.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    OutlinedTextField(
                        value = viewModel.amount,
                        onValueChange = viewModel::onAmountChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Amount in INR") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    TextButton(
                        onClick = { viewModel.onAmountChange("1") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Quick Test ₹1")
                    }
                }
            }
        }

        item {
            Text(
                text = "Installed UPI Apps",
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (viewModel.installedUpiApps.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "No supported UPI app found on this phone. You can still pay using Razorpay checkout.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(viewModel.installedUpiApps) { app ->
                val isSelected = viewModel.selectedUpiApp?.packageName == app.packageName

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectUpiApp(app) },
                    shape = RoundedCornerShape(16.dp),
                    border = if (isSelected) {
                        BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    } else {
                        null
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = app.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        RadioButton(
                            selected = isSelected,
                            onClick = { viewModel.selectUpiApp(app) }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Button(
                onClick = {
                    val selectedApp = viewModel.selectedUpiApp
                    if (viewModel.amount.isNotBlank() && viewModel.amount != "0" && selectedApp != null) {
                        onPayWithUpiApp(viewModel.amount, selectedApp)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.selectedUpiApp != null
            ) {
                Text("Pay With Selected UPI App")
            }
        }

        item {
            OutlinedButton(
                onClick = {
                    if (viewModel.amount.isNotBlank() && viewModel.amount != "0") {
                        onPayWithRazorpay(viewModel.amount)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pay With Razorpay Checkout")
            }
        }
    }
}