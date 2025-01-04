package com.example.arabailan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

// PostAdapter: RecyclerView için adaptör
class PostAdapter(private val postList: ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    // ViewHolder: Her bir liste elemanını temsil eder
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val emailTextView: TextView = itemView.findViewById(R.id.emailEditText)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val brandTextView: TextView = itemView.findViewById(R.id.brandTextView)
        val modelTextView: TextView = itemView.findViewById(R.id.modelTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    // Yeni bir liste elemanının görünümünü oluşturur
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    // Liste elemanına verileri bağlar
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.emailTextView.text = post.userEmail
        holder.modelTextView.text = post.model
        holder.titleTextView.text = post.title
        holder.brandTextView.text = post.brand
        holder.priceTextView.text = post.price

        // Picasso ile resmi yükle ve ImageView'a ekle
        Picasso.get().load(post.imageUrl).into(holder.imageView)
    }

    // Liste elemanlarının toplam sayısını döndürür
    override fun getItemCount(): Int {
        return postList.size
    }
}
