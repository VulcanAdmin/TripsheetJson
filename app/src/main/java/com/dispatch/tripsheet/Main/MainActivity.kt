package com.dispatch.tripsheet.Main


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dispatch.tripsheet.MainViewModel
import com.dispatch.tripsheet.R
import com.dispatch.tripsheet.databinding.ActivityMainBinding
import com.dispatch.tripsheet.model.Cell
import com.dispatch.tripsheet.repository.Repository
import com.dispatch.tripsheet.utils.ConnFailed
import com.example.retrofittest.MainViewModelFactory
import com.example.retrofittest.utils.Constants.Companion.uniqueId
import com.example.retrofittest.utils.Constants.Companion.versionDisplay
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    var tripsheetlist: ArrayList<Cell> = ArrayList()
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        recyclerViewTripsheetlist.layoutManager = LinearLayoutManager(this)

        var adapter = ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)

        conn(adapter) // tripsheet list is filled


    btnRefresh.setOnClickListener {
        finish()
        startActivity(intent)
        }

        var doubleClick: Boolean? = false
        tvHeader.setOnClickListener {
            if (doubleClick!!) {
                Toast.makeText(applicationContext, versionDisplay, Toast.LENGTH_SHORT).show()
            }
            doubleClick = true
            Handler().postDelayed({ doubleClick = false }, 2000)
        }
     }



    private fun conn(adapter: ArrayAdapter<String>) {

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getTripsheet(uniqueId)


        //Log.d("API", "Connect to intranet")


        viewModel.myResponse.observe(this, Observer { response ->
            if(response.isSuccessful){

//                Log.d("Main", response.body().toString())
//                Log.d("Main", response.code().toString())
//                Log.d("Main", response.headers().toString())

                val items = response.body()

                if (items != null) {
                    for (i in 0 until items.count()) {
                        // ID
                        val TRIPSHEETNO = items[i].TRIPSHEETNO ?: "N/A"

                        // JC number
                        val WONUMBER = items[i].WONUMBER ?: "N/A"

                        // Delivery note number for delivery
                        val DELNO = items[i].DELNO ?: "N/A"

                        // Who is the delivery for
                        val CUSTOMER = items[i].CUSTOMER ?: "N/A"

                        // Invoice number linked to delivery
                        val INVOICE = items[i].INVOICE ?: "N/A"

                        // Who is assigned to the delivery
                        val DRIVER = items[i].DRIVER ?: "N/A"

                        // Weight of delivery
                        val WEIGHT = items[i].WEIGHT ?: "N/A"

                        // Status of delivery
                        val DELIVERED = items[i].DELIVERED ?: "N/A"

                        // Number of pallets at customer
                        val PALLETS = items[i].PALLETS ?: 1

                        val model =
                            Cell(
                                TRIPSHEETNO as Int,
                                WONUMBER as String,
                                DELNO as String,
                                CUSTOMER as String,
                                DRIVER as String,
                                INVOICE as String,
                                WEIGHT as Double,
                                DELIVERED as Int,
                                PALLETS as Int
                            )

                        tripsheetlist.add(model)
                    }
                }

                // Pass the data on
                populatespinner(tripsheetlist, adapter)
            }else {
                val intent = Intent(baseContext, ConnFailed::class.java)
                startActivity(intent)
                Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
                Log.e("RETROFIT_ERROR", response.code().toString())
            }
        })


    }


    private fun populatespinner(tripsheetlist: ArrayList<Cell>, adapter: ArrayAdapter<String>) {

        tripsheetlist.distinctBy { it.DRIVER }.forEach { adapter.add(it.DRIVER.toString()) }

        spnDriver.adapter = adapter

        Spinner(tripsheetlist)
    }


    private fun datafilter(tripsheetlist: ArrayList<Cell>, driver: String): ArrayList<Cell> {

    var tripsheet = Tripselect(tripsheetlist, driver)

    return ArrayList(tripsheetlist.filter { !(it.DRIVER != driver && it.TRIPSHEETNO!!.equals(tripsheet)) })
    }

    private fun Spinner(tripsheetlist: ArrayList<Cell>) {

        var drivers =  tripsheetlist.distinctBy { it.DRIVER }
        var driver : String      //this will be the default first picked driver

        spnDriver.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val emptylist: ArrayList<Cell> = ArrayList(tripsheetlist.filter {  it.DRIVER.equals("") })

                runOnUiThread { recyclerViewTripsheetlist.adapter = TableViewAdapter(
                    emptylist,
                    context = this@MainActivity,
                    tvConfirmdelv,
                    tvExcondelv,
                    tvTotalleft
                ) }
                driver  = drivers[p2].DRIVER.toString() //+1 for the select driver entry
                datafilter(tripsheetlist, driver)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

    }

    private fun Tripselect(tripsheetlist: ArrayList<Cell>, driver: String): Int {
        var tripsheet: Int = 0

        var driverList = ArrayList(tripsheetlist.filter { !(it.DRIVER != driver ) })

        var i: Int = 0
        var a: Int = 0
        var b: Int = 0
        var c: Int = 0
        var d: Int = 0
        var e: Int = 0


        var filteredList =  driverList.distinctBy { it.TRIPSHEETNO }

        btntripsheet1.isVisible = false
        btntripsheet2.isVisible = false
        btntripsheet3.isVisible = false
        btntripsheet4.isVisible = false
        btntripsheet5.isVisible = false


        while (i < filteredList.size){

            when(i){
               0 -> { a = filteredList[i].TRIPSHEETNO!!
                   btntripsheet1.text = a.toString()
                   btntripsheet1.isVisible = true}
               1 -> { b = filteredList[i].TRIPSHEETNO!!
                   btntripsheet2.text = b.toString()
                   btntripsheet2.isVisible= true}
               2 -> { c = filteredList[i].TRIPSHEETNO!!
                   btntripsheet3.text = c.toString()
                   btntripsheet3.isVisible= true}
               3 -> { d = filteredList[i].TRIPSHEETNO!!
                   btntripsheet4.text = d.toString()
                   btntripsheet4.isVisible= true}
               4 -> { e = filteredList[i].TRIPSHEETNO!!
                   btntripsheet5.text = e.toString()
                   btntripsheet5.isVisible= true}
            }
            i++
        }

        btntripsheet1.setOnClickListener { tripsheet = a
            tvHeader.text = "Vulcan Steel Trip Sheet: " + tripsheet

            var driverListFill =  ArrayList(driverList.filter { it.TRIPSHEETNO!! == tripsheet })

            totalInvoices(driverListFill)
            weightSum(driverListFill)
            totalDelNotes(driverListFill)

            runOnUiThread {
                recyclerViewTripsheetlist.adapter = TableViewAdapter(
                    driverListFill,
                    context = this@MainActivity,
                    tvConfirmdelv,
                    tvExcondelv,
                    tvTotalleft
                )
            }
        }

        btntripsheet2.setOnClickListener { tripsheet = b
            tvHeader.setText("Vulcan Steel Trip Sheet: " + tripsheet)
            var driverListFill =  ArrayList(driverList.filter { it.TRIPSHEETNO!! == tripsheet })

            totalInvoices(driverListFill)
            weightSum(driverListFill)
            totalDelNotes(driverListFill)

            runOnUiThread {
                recyclerViewTripsheetlist.adapter = TableViewAdapter(
                    driverListFill,
                    context = this@MainActivity,
                    tvConfirmdelv,
                    tvExcondelv,
                    tvTotalleft
                )
            }
        }

        btntripsheet3.setOnClickListener { tripsheet = c
            tvHeader.setText("Vulcan Steel Trip Sheet: " + tripsheet)
            var driverListFill =  ArrayList(driverList.filter { it.TRIPSHEETNO!! == tripsheet })

            totalInvoices(driverListFill)
            weightSum(driverListFill)
            totalDelNotes(driverListFill)

            runOnUiThread {
                recyclerViewTripsheetlist.adapter = TableViewAdapter(
                    driverListFill,
                    context = this@MainActivity,
                    tvConfirmdelv,
                    tvExcondelv,
                    tvTotalleft
                )
            }
        }

        btntripsheet4.setOnClickListener { tripsheet = d
            tvHeader.setText("Vulcan Steel Trip Sheet: " + tripsheet)
            var driverListFill =  ArrayList(driverList.filter { it.TRIPSHEETNO!! == tripsheet })

            totalInvoices(driverListFill)
            weightSum(driverListFill)
            totalDelNotes(driverListFill)

            runOnUiThread {
                recyclerViewTripsheetlist.adapter = TableViewAdapter(
                    driverListFill,
                    context = this@MainActivity,
                    tvConfirmdelv,
                    tvExcondelv,
                    tvTotalleft
                )
            }
        }

        btntripsheet5.setOnClickListener { tripsheet = e
            tvHeader.text = "Vulcan Steel Trip Sheet: " + tripsheet
            var driverListFill =  ArrayList(driverList.filter { it.TRIPSHEETNO!! == tripsheet })

            totalInvoices(driverListFill)
            weightSum(driverListFill)
            totalDelNotes(driverListFill)

            runOnUiThread {
                recyclerViewTripsheetlist.adapter = TableViewAdapter(
                    driverListFill,
                    context = this@MainActivity,
                    tvConfirmdelv,
                    tvExcondelv,
                    tvTotalleft
                )
            }
        }

    return tripsheet
    }


    private fun totalDelNotes(driverListfil: ArrayList<Cell> ) {
       var totnotes :Int = driverListfil.size
        val tvTotalDeliver = findViewById<View>(R.id.tvTotaldelv) as TextView
        tvTotalDeliver.text = "Total Delivery Notes: " +totnotes

        var x =0
        val tvTotalleft = findViewById<View>(R.id.tvTotalleft) as TextView
        driverListfil.forEach { if(it.DELIVERED == 0){x =+1} }

        tvTotalleft.text = "Delivery Notes uncomplete: " +x

    }

    private fun totalInvoices( driverListfil: ArrayList<Cell> ) {
        var totInvc :Int = 0

        val tvTotalInv = findViewById<View>(R.id.tvTotalInv) as TextView

        for (i in 0 until driverListfil.size) {
            if (driverListfil[i].INVOICE!!.isNotEmpty()){ totInvc += 1 }

         }
        tvTotalInv.text = "Total Invoices: " + totInvc

    }

    private fun weightSum(driverListFill: ArrayList<Cell>) {
        var sum: Double = 0.0


        val tvTotalWeight = findViewById<View>(R.id.tvTotalweight) as TextView

        for (i in 0 until driverListFill.size) {
            sum += driverListFill[i].WEIGHT!!
        }

        var totalsum  = String.format("%.0f", sum)
        tvTotalWeight.text = "Total Weight: " + totalsum + "kg"
    }

}


