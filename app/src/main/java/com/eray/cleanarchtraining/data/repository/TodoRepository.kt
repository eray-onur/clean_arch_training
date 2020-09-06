package com.eray.cleanarchtraining.data.repository

import androidx.lifecycle.LiveData
import androidx.room.Update
import com.eray.cleanarchtraining.data.TodoDao
import com.eray.cleanarchtraining.data.models.TodoData

class TodoRepository(private val todoDao: TodoDao) {

    val getAllData: LiveData<List<TodoData>> = todoDao.getAllData()
    val sortByHighPriority: LiveData<List<TodoData>> = todoDao.sortByHighPriority()
    val sortByLowPriority: LiveData<List<TodoData>> = todoDao.sortByLowPriority()

    suspend fun insertData(todoData: TodoData) {
        todoDao.insertData(todoData)
    }

    suspend fun updateData(todoData: TodoData) {
        todoDao.updateData(todoData)
    }

    suspend fun removeData(todoData: TodoData) {
        todoDao.removeData(todoData)
    }

    suspend fun removeAll() {
        todoDao.removeAll()
    }

    fun searchDatabase(searchQuery: String): LiveData<List<TodoData>> {
        return todoDao.searchDatabase(searchQuery)
    }

}