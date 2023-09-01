package com.dispatch.tripsheet.utils

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dispatch.tripsheet.R

class AddressChange : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_change)

        val bundle = intent.extras
        var customer: String? = null
        customer = bundle!!.getString("Customer")

        var btnASend: Button = findViewById(R.id.btnASend)
        var btnABack: Button = findViewById(R.id.btnABack)
        var etUnit: EditText = findViewById(R.id.tvEditUnitNumber)
        var etStreet: EditText = findViewById(R.id.tvEditStreet)
        var etComplex: EditText = findViewById(R.id.tvEditComplex)
        var etArea: EditText = findViewById(R.id.tvEditArea)

        btnABack.setOnClickListener { this.finish() }
        btnASend.setOnClickListener {

            var unit = etUnit.text.toString()
            var street = etStreet.text.toString()
            var complex = etComplex.text.toString()
            var area = etArea.text.toString()


            if (unit == "" || street == "")
            {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Please enter enough details for the new address.")
                //    Toast.makeText(context,"Data capture canceled",Toast.LENGTH_LONG).show()
                builder.setIcon(android.R.drawable.ic_dialog_alert)
                builder.setPositiveButton("OK"){dialogInterface , which ->
                }
                val alertDialog: AlertDialog = builder.create()

                alertDialog.setCancelable(true)
                alertDialog.show()

            }else{

                /*ACTION_SEND action to launch an email client installed on your Android device.*/
                val mIntent = Intent(Intent.ACTION_SEND)
                /*To send an email you need to specify mailto: as URI using setData() method
                and data type will be to text/plain using setType() method*/
                mIntent.data = Uri.parse("mailto:")
                mIntent.type = "text/plain"
                // put recipient email in intent
                /* recipient is put as array because you may wanna send email to multiple emails
                   so enter comma(,) separated emails, it will be stored in array*/
                mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("natasha@vulcansteel.co.za", "hancke@vulcansteel.co.za"))
                //put the Subject in the intent
                mIntent.putExtra(Intent.EXTRA_SUBJECT, "Update of customers address")
                //put the message in the intent
                mIntent.putExtra(Intent.EXTRA_TEXT, ("The new address for customer: "+customer + ". " +
                        " \n "+ unit +
                        " \n "+ street +
                        " \n "+ complex +
                        " \n "+ area +
                        " \n " +
                        " \n " +
                        " Please call IT for further assistance "
                        ))

                try {
                    //start email intent
                    startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
                }
                catch (e: Exception){
                    //if any thing goes wrong for example no email client application or any exception
                    //get and show exception message
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }


                this.finish()
            }

        }
    }

}