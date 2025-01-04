package com.example.arabailan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
// arayüz bileşenleri
class AddPostActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var titleEditText: EditText
    private lateinit var brandEditText: EditText
    private lateinit var modelEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var addButton: Button
    private lateinit var storage: FirebaseStorage
    private lateinit var firestore: FirebaseFirestore
    private var imageUri: Uri? = null

    // Galeri açmak için ActivityResultLauncher
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
            imageView.setImageURI(it) // Seçilen resmi kullanıcıya göster
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)


        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Arayüz bileşenlerini bağlama
        titleEditText = findViewById(R.id.titleEditText)
        brandEditText = findViewById(R.id.brandEditText)
        modelEditText = findViewById(R.id.modelEditText)
        priceEditText = findViewById(R.id.priceEditText)
        addButton = findViewById(R.id.addPostButton)
        imageView = findViewById(R.id.imageView)

        // Resim seçme işlemi
        imageView.setOnClickListener {
            galleryLauncher.launch("image/*") // Sadece resim dosyalarını aç
        }

        // İlan ekleme butonu
        addButton.setOnClickListener {
            uploadPost() // İlan yükleme fonksiyonu çağrılır

        }
    }

    private fun uploadPost() {

        if (imageUri == null) {
            Toast.makeText(this, "Lütfen bir resim seçin!", Toast.LENGTH_SHORT).show()
            return
        }

        // Firebase Storage için resim yolu oluşturma
        val storageRef = storage.reference.child("images/${System.currentTimeMillis()}.jpg")
        // Resmi Firebase Storage'a yükleme
        storageRef.putFile(imageUri!!)
            .addOnSuccessListener { task ->

                storageRef.downloadUrl.addOnSuccessListener { uri ->

                    val post = hashMapOf(
                        "title" to titleEditText.text.toString(),
                        "brand" to brandEditText.text.toString(),
                        "model" to modelEditText.text.toString(),
                        "price" to priceEditText.text.toString(),
                        "imageUrl" to uri.toString(),
                        "userEmail" to FirebaseAuth.getInstance().currentUser?.email,
                        "date" to System.currentTimeMillis() // İlan tarihi
                    )

                    // Post'u Firestore'a kaydetme
                    firestore.collection("Posts").add(post)
                        .addOnSuccessListener {

                            Toast.makeText(this, "İlan başarıyla eklendi!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->

                            Toast.makeText(this, "Hata: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Hata: ${e.message}", Toast.LENGTH_SHORT).show()// Resim yükleme hatası
            }
    }
}
