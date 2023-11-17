package com.dispatch.tripsheet.Main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.dispatch.tripsheet.Exception.DelException
import com.dispatch.tripsheet.R
import com.dispatch.tripsheet.Update
import com.dispatch.tripsheet.model.Cell
import com.dispatch.tripsheet.utils.AddressChange
import kotlinx.android.synthetic.main.table_list_item.view.*


class TableViewAdapter(

    var tripsheetlist: ArrayList<Cell>,
    private val context: Context,
    var tvConfirmdelv: TextView,
    var tvExcondelv: TextView,
    val tvTotalleft: TextView
) : RecyclerView.Adapter<TableViewAdapter.RowViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.table_list_item, parent, false)
        var c = 0
        var x = 0
        var y = 0
        tripsheetlist.forEach {

            when(it.DELIVERED){
                2 ->  y += 1
                1 ->  x += 1
                0 ->  c += 1}
        }
        tvConfirmdelv.text = "Total Confirmations: " +x
        tvExcondelv.text = "Total Exceptions: " + y
        tvTotalleft.text ="Delivery Notes Uncomplete: " +c

        return RowViewHolder(itemView) }

    private  fun updateData(data: ArrayList<Cell>) {
        tripsheetlist = data

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return tripsheetlist.size
        // +1 to add header row... This might need to change as to size of drier tripsheet size
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {

        var doubleClick: Boolean? = false


        val modal = tripsheetlist[position]

        holder.itemView.apply {
            setContentBg(txtWOrder)
            setContentBg(txtDElNote)
            setContentBg(txtCompany)
            setContentBg(txtWeight)
            setContentBg(txtInvoice)

            modal.WEIGHT = Math.round(modal.WEIGHT * 10.0) / 10.0

            txtWOrder.text = modal.WONUMBER.toString()
            txtDElNote.text = modal.DELNO.toString()
            txtCompany.text = modal.CUSTOMER //company
            txtWeight.text = modal.WEIGHT.toString()
            txtInvoice.text = modal.INVOICE.toString()

        }



        holder.apply {


            txtCompany.setOnClickListener {
                if (doubleClick!!) {
                    confirmmap(modal)
                }
                doubleClick = true
                Handler().postDelayed({ doubleClick = false }, 2000)
            }

            txtbtnOtW.setOnClickListener {
                confirmontheway(modal)
            }

            txtbtnCom.setOnClickListener {
                confirmdel(modal)

                println("Location Button confirm")
            }

            txtbtnExc.setOnClickListener {
                confirmexcl(modal)

                println("Location Button exc")
            }

            txtbtnPallet.setOnClickListener {
                confirmpallet(modal)
            }

            txtbtnClear.setOnClickListener {
                confirmclear(modal)
            }
        }
        //To change the textbox based on delivery status

        holder.txttvdone.apply {
            setBackgroundResource(
                when (modal.DELIVERED) {
                    0 -> android.R.color.transparent
                    1 -> R.color.green
                    2 -> R.color.orange
                    5 -> R.color.yellow
                    else -> android.R.color.transparent
                } )
            text = when (modal.DELIVERED) {
                0 -> ""
                1 -> "✓"
                2 -> "x"
                5 -> "⛟"
                else -> ""
            }
        }


    }

    private fun confirmpallet(modal: Cell) {

        val builder = AlertDialog.Builder(context)

        builder.setTitle(R.string.dialogTitlePallet)
        builder.setMessage("Did you just drop some pallets off at, ${modal.CUSTOMER} or did you collect pallets? (${modal.PALLETS})")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Collected") { _, _ ->
            // Begin Collect
            showPalletDialog("How many pallets did you Collect?", "dropPallet", modal.DELNO)
        }

        builder.setNegativeButton("Dropped off") { _, _ ->
            // Begin Dropped off
            showPalletDialog("How many pallets did you drop off?", "collectPallet", modal.DELNO)
        }

        builder.setNeutralButton("Cancel") { _, _ ->
            // Do nothing
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    private fun confirmclear(modal: Cell) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.dialogClear)
        builder.setMessage("Confirm if you want to clear your input?")

        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes"){dialogInterface, which ->
            modal.DELIVERED = 0
            Log.e("Clicked", "Clear")

            notifyDataSetChanged()


            val bundle = Bundle()
            val intent = Intent(context, Update::class.java)
            intent.putExtra("Status","CLEAR")
            intent.putExtra("DELNO",modal.DELNO)
            startActivity(context, intent, bundle)

        }


        builder.setNeutralButton("Cancel"){dialogInterface , which ->

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun confirmmap(modal: Cell) {

        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.dialogTitleMap)
        builder.setMessage("Would you like to see the fastest route to " + modal.CUSTOMER + "?")

        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes"){dialogInterface, which ->

            Log.e("Clicked", "Opened maps")

            notifyDataSetChanged()

            val navigationIntentUri = Uri.parse("google.navigation:q="+ modal.CUSTOMER + "")
            val mapIntent = Intent(Intent.ACTION_VIEW, navigationIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context.startActivity(mapIntent)

        }

        builder.setNeutralButton("Cancel"){dialogInterface , which ->

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    private fun confirmontheway(modal: Cell) {

        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.dialogTitleOnTheWay)
        builder.setMessage("Are you currently on route to " + modal.CUSTOMER + "?")

        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes"){dialogInterface, which ->
            modal.DELIVERED = 5
            Log.e("Clicked", "On the way")

            notifyDataSetChanged()


            val bundle = Bundle()
            val intent = Intent(context, Update::class.java)
            intent.putExtra("Status","OTW")
            intent.putExtra("DELNO",modal.DELNO)
            intent.putExtra("Customer",modal.CUSTOMER)
            startActivity(context, intent, bundle)
        }
        builder.setNegativeButton("Change address"){dialogInterface, which ->
            Log.e("Clicked", "Change address")
            val bundle = Bundle()
            val intent = Intent(context, AddressChange::class.java)
            intent.putExtra("Customer",modal.CUSTOMER)
            startActivity(context, intent, bundle)
            startActivity(context, intent, bundle)

        }

        builder.setNeutralButton("Cancel"){dialogInterface , which ->

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    private fun confirmexcl(modal: Cell) {

        println("Location TableViewAdapter Confirm Excl")

        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.dialogTitleExc)
        builder.setMessage("Was there exception on delivery note " + modal.DELNO + "?")

        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes"){dialogInterface, which ->
            modal.DELIVERED = 2
            Log.e("Clicked", "Exception on delivery")
            // modal.state = DataState.Failure
            notifyDataSetChanged()

            //Toast.makeText(context,"Exception on delivery",Toast.LENGTH_LONG).show()
            var c = 0
            var x = 0
            var y = 0
            tripsheetlist.forEach {
                when(it.DELIVERED){
                    2 ->  y += 1
                    1 ->  x += 1
                    0 -> c += 1}
            }
            tvConfirmdelv.text = "Total confirmations: " +x
            tvExcondelv.text = "Total Exceptions: " + y
            tvTotalleft.text ="Delivery Notes uncomplete: " +c


            //move this to DelException and don't use it here.
//            val bundle2 = Bundle()
//            val intent2 = Intent(context, Update::class.java)
//            intent2.putExtra("Status","Exc")
//            intent2.putExtra("DELNO",modal.DELNO)
//            startActivity(context, intent2, bundle2)


            val bundle = Bundle()
            val intent = Intent(context, DelException::class.java)

            intent.putExtra("Driver",modal.DRIVER)
            intent.putExtra("DELNO",modal.DELNO)
            startActivity(context, intent, bundle)
        }

        builder.setNeutralButton("Cancel"){dialogInterface , which ->
            //    Toast.makeText(context,"Data capture canceled",Toast.LENGTH_LONG).show()
        }
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

    }


    private fun confirmdel(modal: Cell) {
        lateinit var dialog:AlertDialog

        // Initialize an array of colors
        var checked = 0
        val items = arrayOf("CHECKED", "UNCHECKED")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm delivery on Delno-" + modal.DELNO + ". Has the delivery been checked?")

        builder.setSingleChoiceItems(items,-1) { _, which ->
            checked = which + 1
        }

        builder.setPositiveButton("Yes") { dialogInterface, which ->
            if (checked > 0) {

                modal.DELIVERED = 1
                Log.e("Clicked", "Successful delivery")

                notifyDataSetChanged()
                var c = 0
                var x = 0
                var y = 0
                tripsheetlist.forEach {

                    when (it.DELIVERED) {
                        2 -> y += 1
                        1 -> x += 1
                        0 -> c += 1
                    }
                }
                tvConfirmdelv.text = "Total confirmations: " + x
                tvExcondelv.text = "Total Exceptions: " + y
                tvTotalleft.text = "Delivery Notes uncomplete: " + c


                val bundle = Bundle()
                val intent = Intent(context, Update::class.java)
                intent.putExtra("Checked", checked)
                intent.putExtra("Status", "Del")
                intent.putExtra("DELNO", modal.DELNO)
                startActivity(context, intent, bundle)

                // dialog.dismiss()

                // else{PositiveButton.setEnabled(false)}
            }
        }
        builder.setNeutralButton("Cancel") { dialogInterface, which ->
            //dialog.dismiss()
        }

        dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()

    }


    class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtCompany:TextView = itemView.findViewById<TextView>(R.id.txtCompany)
        val txtInvoice:TextView = itemView.findViewById<TextView>(R.id.txtInvoice)
        val txttvdone:TextView = itemView.findViewById<TextView>(R.id.txttvdone)
        val txtbtnCom:Button = itemView.findViewById<Button>(R.id.btnComplete)
        val txtbtnExc:Button = itemView.findViewById<Button>(R.id.btnException)
        val txtbtnOtW:Button = itemView.findViewById<Button>(R.id.btnOnTheWay)
        val txtbtnPallet:Button = itemView.findViewById<Button>(R.id.btnPallet)
        val txtbtnClear:Button = itemView.findViewById<Button>(R.id.btnClear)
    }

    private fun setHeaderBg(view: View) {
        view.setBackgroundResource(R.drawable.table_header_cell_bg)
    }

    private fun setContentBg(view: View) {
        view.setBackgroundResource(R.drawable.table_content_cell_bg)
    }

    fun showPalletDialog(message: String, status: String, delno: String) {
        val inputEditTextField = EditText(context)
        inputEditTextField.inputType = InputType.TYPE_CLASS_NUMBER

        val alertDialog = AlertDialog.Builder(context)
            .setTitle(R.string.dialogPallet)
            .setMessage(message)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setView(inputEditTextField)
            .setPositiveButton("Yes") { _, _ ->
                val totPallet = inputEditTextField.text.toString()
                val pallets: Int? = totPallet.toIntOrNull()

                if (pallets != null && pallets > 0) {
                    // Conversion was successful and pallets value is greater than 0
                    // Perform the desired logic
                    // ...

                    val bundle = Bundle()
                    val intent = Intent(context, Update::class.java)
                    intent.putExtra("Pallet", pallets)
                    intent.putExtra("Status", status)
                    intent.putExtra("DELNO", delno)
                    startActivity(context, intent, bundle)
                } else {
                    Toast.makeText(context, "Please input a valid numerical value greater than 0.", Toast.LENGTH_LONG).show()
                }
            }
            .setNeutralButton("Cancel") { _, _ ->
                // Do nothing
            }
            .setCancelable(false)
            .show()

        inputEditTextField.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(inputEditTextField, InputMethodManager.SHOW_IMPLICIT)
    }

}