package com.example.navigationdrawercommunityobjects.view

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import com.example.navigationdrawercommunityobjects.databinding.DialogCareersBinding
import com.example.navigationdrawercommunityobjects.databinding.DialogNetworkBinding
import com.example.navigationdrawercommunityobjects.databinding.DialogNetworkConnectedBinding
import com.example.navigationdrawercommunityobjects.databinding.LoginDialogBinding
import com.example.navigationdrawercommunityobjects.model.LoginActivity


fun Activity.showNetworkDisconnectedDialog() {
    val binding = DialogNetworkBinding.inflate(layoutInflater)
    val builder = AlertDialog.Builder(this)
    builder.setView(binding.root)
    val alertDialog = builder.create()
    if (alertDialog.window != null) {
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
    }
    alertDialog.show()

    //dissmiss the dialog after 5 seconds
    val handler = Handler()
    handler.postDelayed({ alertDialog.dismiss() }, 5000)
}

fun Activity.showNetworkDialog() {
    val binding = DialogNetworkConnectedBinding.inflate(layoutInflater)
    val builder = AlertDialog.Builder(this)
    builder.setView(binding.root)
    val alertDialog = builder.create()
    if (alertDialog.window != null) {
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
    }
    alertDialog.show()

    val handler = Handler()
    handler.postDelayed({ alertDialog.dismiss() }, 5000)
}

fun Activity.showLoginDialog() {
    val binding = LoginDialogBinding.inflate(layoutInflater)
    val btnLogin = binding.issueDone
    val btnNotNow = binding.notNow
    val builder = AlertDialog.Builder(this)
    builder.setView(binding.root)
    val alertDialog = builder.create()
    btnLogin.setOnClickListener {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        alertDialog.dismiss()
    }
    btnNotNow.setOnClickListener { alertDialog.dismiss() }
    if (alertDialog.window != null) {
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
    }
    alertDialog.show()
}
fun Activity.showCareersDialog() {
    Handler(Looper.getMainLooper()).postDelayed({
        val binding = DialogCareersBinding.inflate(layoutInflater)
        val btnClose = binding.notNow
        val builder = AlertDialog.Builder(this)
        builder.setView(binding.root)
        val alertDialog = builder.create()
        btnClose.setOnClickListener { alertDialog.dismiss() }
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()
    }, 4000)

}