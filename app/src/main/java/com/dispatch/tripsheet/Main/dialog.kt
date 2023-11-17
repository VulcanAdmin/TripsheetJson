package com.dispatch.tripsheet.Main

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import com.dispatch.tripsheet.R

class dialog  // constructor of dialog class
// with parameter activity
internal constructor(  // 2 objects activity and dialog
    private val activity: Activity
) {
    private var dialog: AlertDialog? = null
    @SuppressLint("InflateParams")
    fun startLoadingdialog() {

        // adding ALERT Dialog builder object and passing activity as parameter
        val builder = AlertDialog.Builder(activity)

        // layoutinflater object and use activity to get layout inflater
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_alert, null))
        builder.setCancelable(true)
        dialog = builder.create()
        dialog?.show()
    }

    // dismiss method
    fun dismissdialog() {
        dialog!!.dismiss()
    }
}