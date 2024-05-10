package com.mostafadevo.todo.view.fragments.profile

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class ProfileRepository {
    private val storageReference = FirebaseStorage.getInstance().reference
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    suspend fun uploadImageToFirebase(imageUri: Uri): String {
        val ref =
            storageReference.child(
                "profile_images/${FirebaseAuth.getInstance().currentUser?.uid}/${imageUri.lastPathSegment}"
            )
        ref.putFile(imageUri).await()
        val downloadUrl = ref.downloadUrl.await().toString()
        firestore
            .collection("users")
            .document(auth.currentUser?.uid!!)
            .update(

                "ImageUrl", downloadUrl

            ).await()
        return downloadUrl
    }

    fun getEmail(): String? {
        return auth.currentUser?.email
    }

    fun getName(): String? {
        return auth.currentUser?.displayName
    }

    suspend fun getProfileImageUrl(): String? {
        return firestore
            .collection("users")
            .document(auth.currentUser?.uid!!)
            .get()
            .await()
            .getString("ImageUrl")
    }
}