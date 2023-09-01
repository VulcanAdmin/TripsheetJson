package com.dispatch.tripsheet

import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dispatch.tripsheet.repository.Repository
import com.dispatch.tripsheet.utils.MyLocation
import com.example.retrofittest.MainViewModelFactory
import com.example.retrofittest.utils.Constants.Companion.uniqueId

class Update : AppCompatActivity(){

    private lateinit var viewModel: MainViewModel

    var lat: Double = 0.0
    var long: Double = 0.0
    private val permissionId = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val locationResult = object : MyLocation.LocationResult() {

             override fun gotLocation(location: Location?) {

                 lat = location!!.latitude
                 long = location.longitude

                 updatesql(lat, long)
            }
        }


        val myLocation = MyLocation()
        myLocation.getLocation(this, locationResult)

        finish()

    }

    private fun updatesql(lat: Double, long: Double) {
        val bundle = intent.extras

        var Checked: Int? = null
        Checked = bundle!!.getInt("Checked")
        var DELNO: String? = null
        DELNO = bundle!!.getString("DELNO").toString()
        var Status: String? = null
        Status = bundle!!.getString("Status")
        var Pallet: Int? = null
        Pallet = bundle!!.getInt("Pallet")


        when(Status){
            "Del" -> {
                val repository = Repository()
                val viewModelFactory = MainViewModelFactory(repository)
                viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
                viewModel.pushDeliveredChecked(uniqueId,1,1, lat, long, DELNO)
            }
            "Exc" -> {
                val repository = Repository()
                val viewModelFactory = MainViewModelFactory(repository)
                viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
                viewModel.pushDeliveredUnchecked(uniqueId,2,2, lat, long, DELNO)
            }
            "OTW" -> {
                val repository = Repository()
                val viewModelFactory = MainViewModelFactory(repository)
                viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
                viewModel.pushOnTheWay(uniqueId,5,1, lat, long, DELNO)
            }
            "CLEAR" -> {
                val repository = Repository()
                val viewModelFactory = MainViewModelFactory(repository)
                viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
                viewModel.pushClear(uniqueId,0, DELNO)
            }
            "dropPallet" -> {
                val repository = Repository()
                val viewModelFactory = MainViewModelFactory(repository)
                viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
                viewModel.pushDroppedPallets(uniqueId,Pallet, DELNO)
                Toast.makeText(applicationContext, "Here is how many pallets where passed ${Pallet}", Toast.LENGTH_SHORT).show()
            }

            "collectPallet" -> {
                val repository = Repository()
                val viewModelFactory = MainViewModelFactory(repository)
                viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
                viewModel.pushCollectPallets(uniqueId, Pallet, DELNO)
                Toast.makeText(applicationContext, "Here is how many pallets where passed ${Pallet}", Toast.LENGTH_SHORT).show()

            }

        }

    }


}



