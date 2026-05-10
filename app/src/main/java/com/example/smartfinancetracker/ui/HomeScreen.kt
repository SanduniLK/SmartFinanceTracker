@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.smartfinancetracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.smartfinancetracker.data.FinanceTransaction
import com.example.smartfinancetracker.viewmodel.FinanceViewModel

@Composable
fun HomeScreen(viewModel: FinanceViewModel) {
    val transactions by viewModel.allTransactions.collectAsState(initial = emptyList())
    val balance = viewModel.calculateBalance(transactions)
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Smart Finance") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            BalanceCard(balance)
            if (transactions.isEmpty()) EmptyState() else TransactionList(transactions)
        }

        if (showDialog) {
            AddTransactionDialog(
                onDismiss = { showDialog = false },
                onAdd = { t, a, ty, c -> viewModel.addTransaction(t, a, ty, c) }
            )
        }
    }
}

@Composable
fun BalanceCard(balance: Double) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(24.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Total Balance", style = MaterialTheme.typography.titleMedium)
            Text("Rs. ${String.format("%.2f", balance)}", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun TransactionList(transactions: List<FinanceTransaction>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(transactions) { item ->
            TransactionItem(item)
        }
    }
}

@Composable
fun TransactionItem(transaction: FinanceTransaction) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(transaction.title, style = MaterialTheme.typography.titleMedium)
                Text(transaction.category, style = MaterialTheme.typography.bodySmall)
            }
            val color = if (transaction.type == "INCOME") Color(0xFF4CAF50) else Color(0xFFF44336)
            val prefix = if (transaction.type == "INCOME") "+" else "-"
            Text("$prefix Rs. ${transaction.amount}", color = color, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EmptyState() {
    Column(modifier = Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        Icon(Icons.Default.AccountBalanceWallet, null, Modifier.size(80.dp), Color.LightGray)
        Text("No transactions yet!", color = Color.Gray)
    }
}

@Composable
fun AddTransactionDialog(onDismiss: () -> Unit, onAdd: (String, Double, String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Transaction") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Reason") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text("Expense")
                    Switch(
                        checked = isIncome,
                        onCheckedChange = { isChecked -> isIncome = isChecked } // මෙතන 'onCheckedChange' විය යුතුයි
                    )
                    Text("Income")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val amt = amount.toDoubleOrNull() ?: 0.0
                if (title.isNotEmpty() && amt > 0) {
                    onAdd(title, amt, if (isIncome) "INCOME" else "SPENT", "General")
                    onDismiss()
                }
            }) { Text("Save") }
        }
    )
}