package com.mostafadevo.todo.data.repo

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mostafadevo.todo.data.TodoDAO
import com.mostafadevo.todo.data.model.Todo

class TodoRepository(private val todoDAO: TodoDAO) {
    private val TAG = "TodoRepository"
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    val getAllTodo: LiveData<List<Todo>> = todoDAO.getAllTodos()
    private val fireStoreDB = FirebaseFirestore.getInstance()
        .collection("users")
        .document(currentUser.toString())
        .collection("todos")


    suspend fun insertTodo(todo: Todo) {
        todoDAO.insertTodo(todo)
        fireStoreDB.document(todo.id).set(todo)
    }

    suspend fun deleteAllTodos() {
        todoDAO.deleteAllTodos()
        fireStoreDB.get().addOnSuccessListener { querySnapshot ->
            querySnapshot.documents.forEach {
                fireStoreDB.document(it.id).delete()
            }
        }
    }

    suspend fun updateTodo(todo: Todo) {
        todoDAO.updateTodo(todo)
        fireStoreDB.document(todo.id).set(todo)
    }

    fun getTodoSortedBy(sortType: String): LiveData<List<Todo>> {
        return when (sortType) {
            "newest" -> todoDAO.sortByNewest()
            "oldest" -> todoDAO.sortByOldest()
            "high priority" -> todoDAO.sortByHighPriority()
            "low priority" -> todoDAO.sortByLowPriority()
            "title A to Z" -> todoDAO.sortByTitleAZ()
            "title Z to A" -> todoDAO.sortByTitleZA()
            else -> todoDAO.sortByOldest()
        }
    }

    suspend fun deleteTodoItem(itemToDelete: Todo) {
        todoDAO.deleteTodoItem(itemToDelete)
        fireStoreDB.document(itemToDelete.id).delete()
    }

    suspend fun search(query: String): LiveData<List<Todo>> {
        return todoDAO.search(query)
    }
}