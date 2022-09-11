package com.example.ugd_3_kelompok

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity: AppCompatActivity() {
    private lateinit var inputEmail: TextInputLayout
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var inputKofimasiPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setTitle("User Login")

        inputEmail = findViewById(R.id.inputLayoutEmail)
        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        inputKofimasiPassword = findViewById(R.id.inputLayoutKonfirmasiPassword)
        mainLayout = findViewById(R.id.mainLayout)
        val btnClear: Button = findViewById(R.id.btnClear)
        val btnRegister: Button = findViewById(R.id.btnRegister)
        val btnLogin: Button = findViewById(R.id.btnLogin)

        btnClear.setOnClickListener {
            inputEmail.getEditText()?.setText("")
            inputUsername.getEditText()?.setText("")
            inputPassword.getEditText()?.setText("")
            inputKofimasiPassword.getEditText()?.setText("")

            Snackbar.make(mainLayout, "Text Cleared Success", Snackbar.LENGTH_LONG).show()
        }

        btnLogin.setOnClickListener {
            val moveLogin = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(moveLogin)
        }

        btnRegister.setOnClickListener(View.OnClickListener {
            var checkRegister = false
            val email: String = inputEmail.getEditText()?.getText().toString()
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()
            val konfimasiPassword: String = inputKofimasiPassword.getEditText()?.getText().toString()

            if(email.isEmpty()){
                inputEmail.setError("Email must be filled with text")
                checkRegister = false
            }

            if(username.isEmpty()){
                inputUsername.setError("Username must be filled with text")
                checkRegister = false
            }

            if(password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
                checkRegister = false
            }

            if(konfimasiPassword.isEmpty()){
                inputKofimasiPassword.setError("password confirmation must be filled with text")
                checkRegister = false
            }

            if(password != konfimasiPassword){
                inputKofimasiPassword.setError("Wrong password")
                checkRegister = false
            }

            if (email.isEmpty() && !username.isEmpty() && !password.isEmpty() && konfimasiPassword.isEmpty() && password == konfimasiPassword) {
                Snackbar.make(mainLayout, "Success Register", Snackbar.LENGTH_LONG).show()
                checkRegister = true
            }
            if(!checkRegister) return@OnClickListener
            val moveHome = Intent(this@RegisterActivity, RegisterActivity::class.java)
            startActivity(moveHome)
        })
    }
}
