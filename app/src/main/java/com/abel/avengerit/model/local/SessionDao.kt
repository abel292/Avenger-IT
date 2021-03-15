package com.abel.avengerit.model.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Query("SELECT * FROM _SESSIONS")
    fun getAllLive(): Flow<List<SessionEntity>>

    @Query("SELECT * FROM _SESSIONS")
    suspend fun getAll(): List<SessionEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg entity: SessionEntity)

    @Delete
    fun delete(entity: SessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: List<SessionEntity>)

    @Update
    fun updateTodo(vararg entity: SessionEntity)

    @Query("DELETE FROM _SESSIONS")
    fun clearTable()
}