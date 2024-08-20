package com.example.hazard

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        firestore = FirebaseFirestore.getInstance()

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextAge = findViewById<EditText>(R.id.editTextAge)
        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val buttonRead = findViewById<Button>(R.id.buttonRead)
        val buttonUpdate = findViewById<Button>(R.id.buttonUpdate)
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)

        buttonAdd.setOnClickListener {
            val name = editTextName.text.toString()
            val age = editTextAge.text.toString().toInt()

            addData(name, age)
        }

        buttonRead.setOnClickListener {
            readData()
        }

        buttonUpdate.setOnClickListener {
            // Implementasi update dengan mengambil ID dokumen dan nilai baru dari EditText
            fun updateData(documentId: String) {
                val userUpdates = hashMapOf<String, Any>(
                    "age" to 30
                )

                firestore.collection("users").document(documentId)
                    .update(userUpdates)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully updated!")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error updating document", e)
                    }
            }
        }

        buttonDelete.setOnClickListener {
            // Implementasi delete dengan mengambil ID dokumen dari EditText atau cara lain
            fun deleteData(documentId: String) {
                firestore.collection("users").document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error deleting document", e)
                    }
            }
        }
    }

    private fun addData(name: String, age: Int) {
        val user = hashMapOf(
            "name" to name,
            "age" to age
        )

        firestore.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    private fun readData() {
        firestore.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents.", e)
            }
    }
}