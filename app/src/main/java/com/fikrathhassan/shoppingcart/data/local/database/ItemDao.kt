package com.fikrathhassan.shoppingcart.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.fikrathhassan.shoppingcart.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM cart_items ORDER BY quantity DESC")
    fun getCartItemList(): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_items WHERE itemID = :itemId")
    fun getItemById(itemId: String): CartItemEntity?

    @Upsert
    suspend fun insertItem(item: CartItemEntity)

    @Update
    suspend fun updateItem(item: CartItemEntity)

    @Delete
    suspend fun deleteItem(item: CartItemEntity)

}