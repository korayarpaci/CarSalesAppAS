package com.example.arabailan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    // Firebase Authentication ve arayüz bileşenleri için değişken tanımları
    private lateinit var auth: FirebaseAuth // Firebase Authentication nesnesi
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

   // auth bileşenim
        auth = FirebaseAuth.getInstance()


        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)
        loginButton = findViewById(R.id.loginButton)

        // Kayıt olma işlemi
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Firebase ile yeni kullanıcı oluşturma
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(this, "Kayıt Başarılı!", Toast.LENGTH_SHORT).show()

                        passwordEditText.text.clear()
                        emailEditText.text.clear()

                    } else {
                        Toast.makeText(this, "Hata: ${task.exception?.message}", Toast.LENGTH_SHORT).show()

                        passwordEditText.text.clear()
                        emailEditText.text.clear()
                    }
                }

        }

        // Giriş yapma işlemi
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Firebase ile kullanıcı giriş yapma
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(this, "Giriş Başarılı!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish() // LoginActivity'yi kapatma
                    } else {
                        // Hata mesajı gösterilir
                        Toast.makeText(this, "Hata: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
