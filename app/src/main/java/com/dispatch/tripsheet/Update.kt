package com.dispatch.tripsheet

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dispatch.tripsheet.repository.Repository
import com.dispatch.tripsheet.utils.MyLocation
import com.example.retrofittest.MainViewModelFactory
import com.example.retrofittest.utils.Constants.Companion.uniqueId

class Update : AppCompatActivity(){

    private lateinit var viewModel: MainViewModel
    var Cupcake: Int = 10
    var lat: Double = 0.0
    var long: Double = 0.0
//    private val permissionId = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Cupcake= 0
        val bundle = intent.extras
        var DELNO: String? = null
        DELNO = bundle!!.getString("DELNO").toString()

        val locationResult = object : MyLocation.LocationResult() {

             override fun gotLocation(location: Location?) {

                 lat = location!!.latitude
                 long = location.longitude

                 updatesql(lat, long, DELNO)
                 print("Cupcake is "+ Cupcake)
                 //verify(DELNO)
            //Try the very and if that does not work, go with on response as alternative -       if(response.isSuccessful){
                 //remove get api10
            }
        }


        val myLocation = MyLocation()
        myLocation.getLocation(this, locationResult)

        finish()

    }

    private fun verify(DELNO: String) {

    if(Cupcake==0){
        println("running again")
        println("running again")
        println("running again")
        println("running again")
        println("running again")
        println("running again")
        Log.i("API", "Running updatesql again")
        updatesql(lat, long, DELNO)}

    }

    private fun updatesql(lat: Double, long: Double, DELNO: String) {
        val bundle = intent.extras

//        var Checked: Int? = null
//        Checked = bundle!!.getInt("Checked")
//        var DELNO: String? = null
//        DELNO = bundle!!.getString("DELNO").toString()
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

                viewModel.myResponse2.observe(this, Observer { response ->
                    if(response.isSuccessful){
                        Cupcake = 1
                        Log.d("Main", response.body().toString())
                        Log.d("Main", response.code().toString())
                        Log.d("Main", response.message().toString())
                    }else {
                        verify(DELNO)
//                        Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
//                        Log.e("RETROFIT_ERROR", response.code().toString())
//                        System.err.println("Got an exception! ")
//                        val intent = Intent(baseContext, ConnFailed::class.java)
//                        startActivity(intent)

                    }
                })
            }
            "Exc" -> {
                val repository = Repository()
                val viewModelFactory = MainViewModelFactory(repository)
                viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
                viewModel.pushDeliveredUnchecked(uniqueId,2,2, lat, long, DELNO)

                viewModel.myResponse2.observe(this, Observer { response ->
                    if(response.isSuccessful){
                        Cupcake = 1
                        Log.d("Main", response.body().toString())
                        Log.d("Main", response.code().toString())
                        Log.d("Main", response.message().toString())
                    }else {
                        verify(DELNO)
//                        Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
//                        Log.e("RETROFIT_ERROR", response.code().toString())
//                        System.err.println("Got an exception! ")
//                        val intent = Intent(baseContext, ConnFailed::class.java)
//                        startActivity(intent)

                    }
                })
            }
            "OTW" -> {
                val repository = Repository()
                val viewModelFactory = MainViewModelFactory(repository)
                viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
                viewModel.pushOnTheWay(uniqueId,5,1, lat, long, DELNO)

                viewModel.myResponse2.observe(this, Observer { response ->
                    if(response.isSuccessful){
                        Cupcake = 1
                        Log.d("Main", response.body().toString())
                        Log.d("Main", response.code().toString())
                        Log.d("Main", response.message().toString())
                    }else {
                        verify(DELNO)
//                        Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
//                        Log.e("RETROFIT_ERROR", response.code().toString())
//                        System.err.println("Got an exception! ")
//                        val intent = Intent(baseContext, ConnFailed::class.java)
//                        startActivity(intent)

                    }
                })
            }
            "CLEAR" -> {
                val repository = Repository()
                val viewModelFactory = MainViewModelFactory(repository)
                viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
                viewModel.pushClear(uniqueId,0, DELNO)

                viewModel.myResponse2.observe(this, Observer { response ->
                    if(response.isSuccessful){
                        Cupcake = 1
                        Log.d("Main", response.body().toString())
                        Log.d("Main", response.code().toString())
                        Log.d("Main", response.message().toString())
                    }else {
                        verify(DELNO)
//                        Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
//                        Log.e("RETROFIT_ERROR", response.code().toString())
//                        System.err.println("Got an exception! ")
//                        val intent = Intent(baseContext, ConnFailed::class.java)
//                        startActivity(intent)

                    }
                })
            }
            "dropPallet" -> {
                val repository = Repository()
                val viewModelFactory = MainViewModelFactory(repository)
                viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
                viewModel.pushDroppedPallets(uniqueId,Pallet, DELNO)
                Toast.makeText(applicationContext, "Here is how many pallets where passed ${Pallet}", Toast.LENGTH_SHORT).show()

                viewModel.myResponse2.observe(this, Observer { response ->
                    if(response.isSuccessful){
                        Cupcake = 1
                        Log.d("Main", response.body().toString())
                        Log.d("Main", response.code().toString())
                        Log.d("Main", response.message().toString())
                    }else {
                        verify(DELNO)
//                        Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
//                        Log.e("RETROFIT_ERROR", response.code().toString())
//                        System.err.println("Got an exception! ")
//                        val intent = Intent(baseContext, ConnFailed::class.java)
//                        startActivity(intent)

                    }
                })
            }

            "collectPallet" -> {
                val repository = Repository()
                val viewModelFactory = MainViewModelFactory(repository)
                viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
                viewModel.pushCollectPallets(uniqueId, Pallet, DELNO)
                Toast.makeText(applicationContext, "Here is how many pallets where passed ${Pallet}", Toast.LENGTH_SHORT).show()

                viewModel.myResponse2.observe(this, Observer { response ->
                    if(response.isSuccessful){
                        Cupcake = 1
                        Log.d("Main", response.body().toString())
                        Log.d("Main", response.code().toString())
                        Log.d("Main", response.message().toString())
                    }else {
                        verify(DELNO)
//                        Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
//                        Log.e("RETROFIT_ERROR", response.code().toString())
//                        System.err.println("Got an exception! ")
//                        val intent = Intent(baseContext, ConnFailed::class.java)
//                        startActivity(intent)

                    }
                })
            }

        }

    }


}




