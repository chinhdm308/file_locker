package com.base.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.base.data.local.dao.base.BaseDao
import com.base.data.local.entities.RepoEntity

@Dao
abstract class RepoDao : BaseDao<RepoEntity> {

    /**
     * Select a repo by the id
     * @param id The repo id
     * @return A [RepoEntity]
     */
    @Query("SELECT * FROM repo_table WHERE id=:id")
    abstract fun get(id: Long): RepoEntity?

    /**
     * Select all repos by the userName
     * @return A list of [RepoEntity] of all the repos in the table for user
     */
    @Query("SELECT * FROM repo_table WHERE user_name=:userName")
    abstract fun getAll(userName: String): List<RepoEntity>

    /**
     * Update is favorite repo by the id
     * @param id The repo id
     * @param isFavorite If repo is favorite or not
     * @return A number of repo updated. This should always be `1`
     */
    @Query("UPDATE repo_table SET is_favorite=:isFavorite WHERE id=:id")
    abstract fun updateIsFavorite(id: Long, isFavorite: Boolean): Int

    @Transaction
    open fun insertAndDeleteInTransaction(newRepo: RepoEntity, oldRepo: RepoEntity) {
        // Anything inside this method runs in a single transaction.
        insertOrReplace(newRepo)
        delete(oldRepo)
    }

}