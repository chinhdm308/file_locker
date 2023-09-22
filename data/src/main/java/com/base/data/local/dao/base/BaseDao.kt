package com.base.data.local.dao.base

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface BaseDao<T> {

    /**
     * Insert a entity into the table.
     * @param entity
     * @return The row ID of the newly inserted entity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(entity: T): Long

    /**
     * Delete an entity
     * @return A number of entity deleted. This should always be `1`
     */
    @Delete
    fun delete(entity: T): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: List<T>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(t: T)

}