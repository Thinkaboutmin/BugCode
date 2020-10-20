package com.trab.bugcodecopy.lib
import android.graphics.Color
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.trab.bugcodecopy.R

fun AppCompatActivity.errorSnackBar(view: View, string: String): Snackbar {
    val snack = Snackbar.make(view, string, Snackbar.LENGTH_LONG)

    snack.setBackgroundTint(getColor(R.color.design_default_color_error))
    snack.setTextColor(Color.WHITE)

    return snack
}

fun AppCompatActivity.errorSnackBar(view: View, stringId: Int): Snackbar {
    return this.errorSnackBar(view, getString(stringId))
}

fun AppCompatActivity.successSnackBar(view: View, string: String): Snackbar {
    val snack = Snackbar.make(view, string, Snackbar.LENGTH_LONG)

    snack.setBackgroundTint(Color.GREEN)
    snack.setTextColor(Color.BLACK)

    return snack
}

fun AppCompatActivity.successSnackBar(view: View, stringId: Int): Snackbar {
    return this.successSnackBar(view, getString(stringId))
}