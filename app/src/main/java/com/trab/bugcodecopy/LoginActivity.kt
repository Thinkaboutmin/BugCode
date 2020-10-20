package com.trab.bugcodecopy

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.room.RoomDatabase
import com.trab.bugcodecopy.lib.UserDataBase
import com.trab.bugcodecopy.lib.errorSnackBar
import com.trab.bugcodecopy.models.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.login_form.*
import kotlin.concurrent.thread

class LoginActivity : AppCompatActivity() {
    private var database: UserDataBase? = null

    private val clickHandler = View.OnClickListener {
        when(it.id) {
            R.id.btnLogin -> this.login()
            R.id.btnToRegister -> this.toRegister()
            R.id.btnFacebook -> this.toLink("https://facebook.com")
            R.id.btnInstagram -> this.toLink("https://instagram.com")
            R.id.btnGplus -> this.toLink("https://currents.google.com")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_login)

        database = UserDataBase.getDatabase(this)

        btnLogin.setOnClickListener(this.clickHandler)
        btnToRegister.setOnClickListener(this.clickHandler)
        btnFacebook.setOnClickListener(this.clickHandler)
        btnGplus.setOnClickListener(this.clickHandler)
        btnInstagram.setOnClickListener(this.clickHandler)
    }

    private fun login() {
        if (textInPassword.text.isNullOrEmpty() || textInUsername.text.isNullOrEmpty()) {
            val snack = this.errorSnackBar(scrollLogin, R.string.error_login)

            snack.show()
            return
        }


        if (this.database != null) {
            var user: User? = null
            thread {
                 user =this.database!!.userDAO().get(
                    textInPassword.text.toString(),
                    textInUsername.text.toString()
                )
            }.join() // Bypass safety lock

            if (user == null) {
                val snack = this.errorSnackBar(scrollLogin, R.string.error_login)

                snack.show()
                return
            }
        }
    }

    private fun toRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun toLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}