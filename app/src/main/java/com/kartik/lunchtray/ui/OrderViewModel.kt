package com.kartik.lunchtray.ui

import android.icu.text.NumberFormat
import androidx.lifecycle.ViewModel
import com.kartik.lunchtray.model.MenuItem
import com.kartik.lunchtray.model.MenuItem.EntreeItem
import com.kartik.lunchtray.model.MenuItem.SideDishItem
import com.kartik.lunchtray.model.MenuItem.AccompanimentItem
import com.kartik.lunchtray.model.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OrderViewModel : ViewModel(){
    private val taxRate = 0.08

    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState:StateFlow<OrderUiState> = _uiState.asStateFlow()

    fun updateEntree(entree: EntreeItem){
        val previousEntree = _uiState.value.entree
        updateItem(entree,previousEntree)
    }

    fun updateSideDish(sideDish: SideDishItem){
        val previousSideDish = _uiState.value.sideDish
        updateItem(sideDish,previousSideDish)
    }

    fun updateAccompaniment(sideDish: AccompanimentItem){
        val previousAccompaniment = _uiState.value.sideDish
        updateItem(sideDish,previousAccompaniment)
    }

    fun resetOrder(){
        _uiState.value = OrderUiState()
    }

    private fun updateItem(newItem: MenuItem, previousItem: MenuItem?) {
        _uiState.update { currentState ->

            val previousItemPrice = previousItem?.price ?: 0.0

            val itemTotalPrice = currentState.itemTotalPrice - previousItemPrice + newItem.price

            val tax = itemTotalPrice * taxRate
            currentState.copy(
                itemTotalPrice = itemTotalPrice,
                orderTax = tax,
                orderTotalPrice = itemTotalPrice + tax,
                entree = if (newItem is EntreeItem) newItem else currentState.entree,
                sideDish = if (newItem is MenuItem.SideDishItem) newItem else currentState.sideDish,
                accompaniment =
                    if (newItem is MenuItem.AccompanimentItem) newItem else currentState.accompaniment
            )
        }
    }
}

fun Double.formatPrice():String{
    return NumberFormat.getCurrencyInstance().format(this)
}