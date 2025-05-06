package com.fikrathhassan.shoppingcart.data.local

import android.content.Context
import androidx.annotation.WorkerThread
import com.fikrathhassan.shoppingcart.data.local.database.ItemDao
import com.fikrathhassan.shoppingcart.data.mapper.toCartItemEntity
import com.fikrathhassan.shoppingcart.data.mapper.toItem
import com.fikrathhassan.shoppingcart.domain.datasource.CartDataSource
import com.fikrathhassan.shoppingcart.domain.model.Item
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val itemDao: ItemDao,
    private val ioDispatcher: CoroutineDispatcher
): CartDataSource {

    @WorkerThread
    override fun getItemList(
        onStart: () -> Unit,
        onComplete: () -> Unit
    ) = itemDao.getCartItemList().map { list ->
        list.map {
            it.toItem()
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override suspend fun addItem(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        item: Item
    ) = flow {
        var quantity = 1
        itemDao.getItemById(item.id)?.let {
            it.quantity += 1
            itemDao.updateItem(it)
            quantity = it.quantity
        } ?: itemDao.insertItem(item.copy(quantity = quantity).toCartItemEntity())
        emit(quantity)
    }.flowOn(ioDispatcher)

    @WorkerThread
    override suspend fun increaseItemQuantity(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        itemId: String
    ) = flow {
        itemDao.getItemById(itemId)?.let {
            it.quantity += 1
            itemDao.updateItem(it)
        }
        emit(true)
    }.flowOn(ioDispatcher)

    @WorkerThread
    override suspend fun reduceItemQuantity(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        itemId: String
    ) = flow {
        itemDao.getItemById(itemId)?.let {
            if (it.quantity <= 1) {
                itemDao.deleteItem(it)
            } else {
                it.quantity -= 1
                itemDao.updateItem(it)
            }
        }
        emit(true)
    }.flowOn(ioDispatcher)

}