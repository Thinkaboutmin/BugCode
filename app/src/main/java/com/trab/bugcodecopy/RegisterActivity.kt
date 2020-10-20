package com.trab.bugcodecopy

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.trab.bugcodecopy.lib.UserDataBase
import kotlinx.android.synthetic.main.register_form.*
import com.trab.bugcodecopy.lib.errorSnackBar
import com.trab.bugcodecopy.lib.successSnackBar
import com.trab.bugcodecopy.models.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.register_toolbar.*
import kotlin.concurrent.thread

class RegisterActivity : AppCompatActivity() {
    var database: UserDataBase? = null
    var popUp: PopupWindow? = null

    private val clickHandle = View.OnClickListener {
        when(it.id) {
            R.id.btnRegister -> this.register()
            R.id.toolbarRegister -> finish()
        }
    }

    private val menuItemHandle = MenuItem.OnMenuItemClickListener {
        this.infoPopUp()

        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_register)

        database = UserDataBase.getDatabase(this)

        btnRegister.setOnClickListener(clickHandle)
        toolbarRegister.setOnClickListener(clickHandle)
        toolbarRegister.menu.getItem(0).setOnMenuItemClickListener(menuItemHandle)
    }

    private fun register() {
        this.clearFormErrors()
        val errors = this.validateForms()
        if (errors.size > 1) {
            val snack = this.errorSnackBar(
                scrollRegister,
                R.string.error_snack_obligatory
            )

            snack.show()

            return
        } else if(errors.isNotEmpty()) {
            errors[0].show()

            return
        }

        if (database != null) {
            val dao = database!!.userDAO()
            var user: User? = null
            thread {
                dao.register(
                    User(
                        username = textInUsername.text.toString(),
                        email = textInEmail.text.toString(),
                        password = textInPassword.text.toString()
                    )
                )

                user = dao.get(textInUsername.text.toString(), textInPassword.text.toString())
            }.join() // Bypass safety lock on main
            if (user == null) {
                val snack = this.errorSnackBar(scrollRegister, R.string.error_to_register_user)

                snack.show()
                return
            }

            val snack = this.successSnackBar(scrollRegister, R.string.success_to_register_user)
            snack.show()

            return
        }

        val snack = this.errorSnackBar(scrollRegister, R.string.error_database_not_initialized)
        snack.show()
    }

    private fun validateForms() : MutableList<Snackbar> {
        val errors = mutableListOf<Snackbar>()

        if (textInUsername.text.isNullOrBlank()) {
            textHintUsername.error = getString(R.string.error_obligatory)

            errors.add(this.errorSnackBar(scrollRegister, R.string.error_obligatory))

        }

        if (textInEmail.text.isNullOrBlank()) {
            textHintEmail.error = getString(R.string.error_obligatory)

            errors.add(this.errorSnackBar(scrollRegister, R.string.error_obligatory))
        }

        var errorOnPassword = false
        if (textInPassword.text.isNullOrBlank()) {
            errorOnPassword = true
            textHintPassword.error = getString(R.string.error_obligatory)

            errors.add(this.errorSnackBar(scrollRegister, R.string.error_obligatory))
        }

        if (textInPassword2.text.isNullOrBlank()) {
            errorOnPassword = true
            textHintPassword2.error = getString(R.string.error_obligatory)

            errors.add(this.errorSnackBar(scrollRegister, R.string.error_obligatory))
        }

        if (!errorOnPassword) {
            if (textInPassword.text.toString() != textInPassword2.text.toString()) {
                textHintPassword2.error = getString(R.string.error_password_confirmation)

                errors.add(this.errorSnackBar(scrollRegister, R.string.error_password_confirmation))
            }
        }

        return errors
    }

    private fun clearFormErrors() {
        textHintUsername.error = null
        textHintEmail.error = null
        textHintPassword.error = null
        textInPassword2.error = null
    }

    private fun infoPopUp() {
        val popUp = PopupWindow(View.inflate(applicationContext, R.layout.register_pop_up, null))
        popUp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        popUp.width = LinearLayout.LayoutParams.WRAP_CONTENT
        popUp.elevation = 10.0f
        popUp.showAtLocation(scrollRegister, Gravity.CENTER, 0, 0)

    }
}