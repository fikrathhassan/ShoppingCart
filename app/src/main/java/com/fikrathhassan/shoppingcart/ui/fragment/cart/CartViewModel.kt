package com.fikrathhassan.shoppingcart.ui.fragment.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fikrathhassan.shoppingcart.domain.datasource.CartDataSource
import com.fikrathhassan.shoppingcart.domain.model.Item
import com.fikrathhassan.shoppingcart.toRoundedTwoDecimal
import com.fikrathhassan.shoppingcart.ui.base.BaseVM
import com.fikrathhassan.shoppingcart.ui.model.ItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartDataSource: CartDataSource
): BaseVM() {

    private val _subTotal = MutableLiveData("0.00")
    val subTotal: LiveData<String> = _subTotal
    private fun setSubtotal(value: String) {
        _subTotal.value = value
    }

    private val _tax = MutableLiveData("0.00")
    val tax: LiveData<String> = _tax
    private fun setTax(value: String) {
        _tax.value = value
    }

    private val _total = MutableLiveData("0.00")
    val total: LiveData<String> = _total
    private fun setTotal(value: String) {
        _total.value = value
    }

    fun updateValues(data: List<ItemUi>) {
        val subTotal = data.sumOf {
            it.price.originalPrice * it.quantity
        }
        setSubtotal(subTotal.toRoundedTwoDecimal())

        val tax = data.sumOf {
            it.price.originalPrice * it.quantity * (it.taxPercentage/100)
        }
        setTax(tax.toRoundedTwoDecimal())

        val total = subTotal + tax
        setTotal(total.toRoundedTwoDecimal())
    }

    private val _itemList = MutableLiveData<List<Item>>()
    val itemList: LiveData<List<Item>> = _itemList
    private fun setItemList(list: List<Item>) {
        _itemList.value = list
    }

    fun getItems() {
        viewModelScope.launch {
            cartDataSource.getItemList(
                onStart = {
                    setProgress(true)
                },
                onComplete = {
                    setProgress(false)
                }
            ).collect {
                setItemList(it)
            }
        }
    }

    fun increaseItemQuantity(itemId: String) {
        viewModelScope.launch {
            cartDataSource.increaseItemQuantity(
                onStart = {
                },
                onComplete = {
                },
                itemId = itemId
            ).collect {
            }
        }
    }

    fun reduceItemQuantity(itemId: String) {
        viewModelScope.launch {
            cartDataSource.reduceItemQuantity(
                onStart = {
                },
                onComplete = {
                },
                itemId = itemId
            ).collect {
            }
        }
    }

    init {
        getItems()
    }

}