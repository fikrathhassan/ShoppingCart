package com.fikrathhassan.shoppingcart.domain.datasource

import com.fikrathhassan.shoppingcart.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface CartDataSource {

    fun getItemList(
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<List<Item>>

    suspend fun addItem(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        item: Item
    ): Flow<Int>

    suspend fun increaseItemQuantity(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        itemId: String
    ): Flow<*>

    suspend fun reduceItemQuantity(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        itemId: String
    ): Flow<*>

}