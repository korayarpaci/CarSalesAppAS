package com.example.arabailan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {
  // ara yüz bileşenlerim
    private lateinit var recyclerView: RecyclerView
    private lateinit var postList: ArrayList<Post>
    private lateinit var adapter: PostAdapter
    private lateinit var firestore: FirebaseFirestore
  // xml dosya bağlantısı
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        postList = ArrayList()
        adapter = PostAdapter(postList)
        recyclerView.adapter = adapter
// ekleme butonum
        val addPostButton: FloatingActionButton = findViewById(R.id.addPostButton)
        addPostButton.setOnClickListener {
            val intent = Intent(this, AddPostActivity::class.java)
            startActivity(intent)
        }

//databaseim
        firestore = FirebaseFirestore.getInstance()
        fetchPosts()
    }
// post için değişkenlerimi ekledim
    private fun fetchPosts() {
        firestore.collection("Posts")
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {

                    Toast.makeText(this, "Hata: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    postList.clear() // Liste temizlenir
                    for (document in snapshot.documents) {
                        val post = document.toObject(Post::class.java) // Firebase belgesi Post nesnesine dönüştürülür
                        post?.let {
                            postList.add(it) // İlan listeye eklenir
                            android.util.Log.d("FirestorePost", "Post: ${it.title}, ${it.brand}, ${it.date}")
                        }
                    }
                    adapter.notifyDataSetChanged() // Liste güncellendiğinde adapter bilgilendirilir
                } else {
                    Toast.makeText(this, "Henüz bir ilan eklenmedi.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
