package com.mostafadevo.todo

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mostafadevo.todo.data.TodoDataBase
import com.mostafadevo.todo.data.model.Todo
import com.mostafadevo.todo.data.repo.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class FirebaseSyncWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    private val fireStoreDB = FirebaseFirestore.getInstance()
        .collection(Utils.FIREBASE_USER_COLLECTION_NAME)
        .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
    private val todoDAO = TodoDataBase.getDatabase(context).todoDao()
    private val repository: TodoRepository = TodoRepository(todoDAO)
    private val TAG = "FirebaseSyncWorker"
    private var allTodos: List<Todo> = emptyList()
    private var allDeletedTodos: List<Todo> = emptyList()
    private var allTodosFromFirebase: List<Todo> = emptyList()


    companion object {
        const val WORK_NAME = "com.mostafadevo.todo.FirebaseSyncWorker"
        fun schedule(context: Context) {
            val syncRequest = PeriodicWorkRequestBuilder<FirebaseSyncWorker>(1, TimeUnit.HOURS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

            // Schedule the work
            androidx.work.WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                androidx.work.ExistingPeriodicWorkPolicy.KEEP,
                syncRequest
            )
        }

    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                // Fetch local todos and deleted todos
                val allTodos = repository.getAllTodos()
                val allDeletedTodos = repository.getAllDeletedTodos()

                // Push local todos to Firebase
                repository.pushTodosToFirebase(allTodos, allDeletedTodos)

                // Fetch todos from Firebase and insert them into local database
                val allTodosFromFirebase = repository.getTodosFromFirebase()
                allTodosFromFirebase.forEach {
                    repository.insertTodo(it)
                }

                Result.success()
            } catch (e: Exception) {
                Result.retry()
            }
        }
    }
}
