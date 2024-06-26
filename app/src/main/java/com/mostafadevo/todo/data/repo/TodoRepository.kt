package com.mostafadevo.todo.data.repo

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mostafadevo.todo.Utils
import com.mostafadevo.todo.data.database.TodoDAO
import com.mostafadevo.todo.data.model.Todo
import kotlinx.coroutines.tasks.await

class TodoRepository(private val todoDAO: TodoDAO) {
    private val TAG = "TodoRepository"
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    private val fireStoreDB = FirebaseFirestore.getInstance()
        .collection(Utils.FIREBASE_USER_COLLECTION_NAME)
        .document(currentUser.toString())
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val storageReference = firebaseStorage.reference

    suspend fun getAllTodos(): List<Todo> {
        return todoDAO.getAllTodos()
    }

    suspend fun getAllDeletedTodos(): List<Todo> {
        return todoDAO.getAllDeletedTodos()
    }

    suspend fun getUserName(): String {
        return try {
            val document = fireStoreDB.get().await()
            if (document.exists()) {
                document.getString(Utils.FIREBASE_USER_NAME_FIELD) ?: ""
            } else {
                ""
            }
        } catch (e: Exception) {
            Log.d(TAG, "getUserName failed with ", e)
            ""
        }
    }

    suspend fun getUserEmail(): String {
        return try {
            val document = fireStoreDB.get().await()
            if (document.exists()) {
                document.getString(Utils.FIREBASE_USER_EMAIL_FIELD) ?: ""
            } else {
                ""
            }
        } catch (e: Exception) {
            Log.d(TAG, "getUserName failed with ", e)
            ""
        }
    }

    suspend fun getUserImageUrl(): String {
        return try {
            val document = fireStoreDB.get().await()
            if (document.exists()) {
                document.getString(Utils.FIREBASE_USER_IMAGE_URL_FIELD) ?: ""
            } else {
                ""
            }
        } catch (e: Exception) {
            Log.d(TAG, "getUserImageUrl failed with ", e)
            ""
        }
    }

    suspend fun pushTodosToFirebase(todos: List<Todo>, deletedTodos: List<Todo>) {
        try {
            todos.forEach { todo ->
                fireStoreDB.collection(Utils.FIREBASE_TODO_COLLECTION_NAME)
                    .document(todo.id.toString())
                    .set(todo)
                    .await()
            }
            deletedTodos.forEach { todo ->
                fireStoreDB.collection(Utils.FIREBASE_TODO_COLLECTION_NAME)
                    .document(todo.id.toString())
                    .delete()
                    .await()
            }

        } catch (e: Exception) {
            Log.d(TAG, "pushTodosToFirebase failed with ", e)
        }
    }

    suspend fun getTodosFromFirebase(): List<Todo> {
        return try {
            val querySnapshot =
                fireStoreDB.collection(Utils.FIREBASE_TODO_COLLECTION_NAME).get().await()
            querySnapshot.toObjects(Todo::class.java)
        } catch (e: Exception) {
            Log.d(TAG, "getTodosFromFirebase failed with ", e)
            emptyList()
        }
    }


    suspend fun insertTodo(todo: Todo) {
        todoDAO.insertTodo(todo)
    }

    suspend fun deleteAllTodos() {
        todoDAO.deleteAllTodos()
    }

    suspend fun updateLocalTodo(todo: Todo) {
        todoDAO.updateTodo(todo)
    }

    fun getLocalTodoSortedBy(sortType: String): LiveData<List<Todo>> {
        return when (sortType) {
            "high priority" -> todoDAO.sortByHighPriority()
            "low priority" -> todoDAO.sortByLowPriority()
            "title A to Z" -> todoDAO.sortByTitleAZ()
            "title Z to A" -> todoDAO.sortByTitleZA()
            else -> todoDAO.sortByTitleAZ()
        }
    }

    suspend fun deleteLocalTodo(itemToDelete: Todo) {
        todoDAO.deleteTodoItem(itemToDelete.id) // delete from local db
    }

    suspend fun search(query: String): LiveData<List<Todo>> {
        return todoDAO.search(query)
    }

    fun deleteAllLocalTodos() {
        todoDAO.deleteAllTodos()
    }

    fun uploadImageToFirebase(picUri: Uri?) {
        val imageReference =
            storageReference.child("/users/${currentUser}/${picUri?.lastPathSegment}")
        val uploadedImage = imageReference.putFile(picUri!!)
        uploadedImage
            .addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener { link ->
                    updateImageUrl(link.toString())
                }
            }
            .addOnFailureListener {}
    }

    private fun updateImageUrl(imageUrl: String) {
        fireStoreDB.update(
            mapOf(
                Utils.FIREBASE_USER_IMAGE_URL_FIELD to imageUrl
            )
        )
            .addOnSuccessListener {
                Log.d(TAG, "updateImageUrl: success")
            }.addOnFailureListener {
                Log.d(TAG, "updateImageUrl: failed")
            }
    }

    fun updateUserName(newUserName: String) {
        fireStoreDB.update(
            mapOf(
                Utils.FIREBASE_USER_NAME_FIELD to newUserName
            )
        )
            .addOnSuccessListener {
                Log.d(TAG, "updateUserName: success")
            }.addOnFailureListener {
                Log.d(TAG, "updateUserName: failed")
            }
    }

    fun saveUserEmail(currentUserEmail: String?) {
        fireStoreDB.update(
            mapOf(
                Utils.FIREBASE_USER_EMAIL_FIELD to currentUserEmail
            )
        )
            .addOnSuccessListener {
                Log.d(TAG, "saveUserEmail: success")
            }.addOnFailureListener {
                Log.d(TAG, "saveUserEmail: failed")
            }
    }
}