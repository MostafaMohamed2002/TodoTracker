package com.mostafadevo.todo.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.mostafadevo.todo.data.TodoDAO
import com.mostafadevo.todo.data.model.Priority
import com.mostafadevo.todo.data.model.Todo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoRepository(private val todoDAO: TodoDAO) {
    private val TAG = "TodoRepository"
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    val getAllTodo: LiveData<List<Todo>> = todoDAO.getAllTodos()
    private val fireStoreDB = FirebaseFirestore.getInstance()
        .collection("users")
        .document(currentUser.toString())
        .collection("todos")

    init {
        //handle changes in firestore
/*
        fireStoreDB.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            value?.let {
                for (doc in it.documentChanges) {
                    val priority = when (doc.document["priority"]) {
                        "HIGH" -> Priority.HIGH
                        "MEDIUM" -> Priority.MEDIUM
                        else -> Priority.LOW
                    }
                    val todo = Todo(
                        doc.document["id"].toString(),
                        doc.document["title"].toString(),
                        priority,
                        doc.document["description"].toString()
                    )
                    when (doc.type) {
                        DocumentChange.Type.ADDED -> {
                            Log.d(TAG, "ADDED")
                            CoroutineScope(Dispatchers.IO).launch {
                                todoDAO.insertTodo(todo)
                            }
                        }

                        DocumentChange.Type.MODIFIED -> {
                            Log.d(TAG, "MODIFIED")
                            CoroutineScope(Dispatchers.IO).launch {
                                todoDAO.updateTodo(todo)
                            }
                        }

                        DocumentChange.Type.REMOVED -> {
                            Log.d(TAG, "REMOVED")
                            CoroutineScope(Dispatchers.IO).launch {
                                todoDAO.deleteTodoItem(todo)
                            }
                        }
                    }
                }
            }
        }
*/
    }

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

    fun deleteAllLocalTodos() {
        todoDAO.deleteAllTodos()
    }
}