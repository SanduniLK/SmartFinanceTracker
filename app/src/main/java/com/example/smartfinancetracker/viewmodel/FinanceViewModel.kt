package com.example.smartfinancetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfinancetracker.data.FinanceTransaction
import com.example.smartfinancetracker.data.TransactionDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FinanceViewModel(private val dao: TransactionDao) : ViewModel() {

    val allTransactions: Flow<List<FinanceTransaction>> = dao.getAllTransactions()

    fun addTransaction(title: String, amount: Double, type: String, category: String) {
        viewModelScope.launch {
            val newTransaction = FinanceTransaction(
                title = title, amount = amount, type = type,
                category = category, date = System.currentTimeMillis()
            )
            dao.insertTransaction(newTransaction)
        }
    }

    fun calculateBalance(transactions: List<FinanceTransaction>): Double {
        var total = 0.0
        transactions.forEach {
            if (it.type == "INCOME") total += it.amount
            else total -= it.amount
        }
        return total
    }
}