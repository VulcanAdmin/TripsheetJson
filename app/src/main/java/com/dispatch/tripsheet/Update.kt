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
    //    var Cupcake: Int = 10
    var lat: Double = 0.0
    var long: Double = 0.0
    //    private val permissionId = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.loading)
        //Just to test the view
        //setContentView(R.layout.scary_screen)
//        Cupcake= 0
//        val bundle = intent.extras
//        var DELNO: String? = null
//        DELNO = bundle!!.getString("DELNO").toString()

        val locationResult = object : MyLocation.LocationResult() {

            override fun gotLocation(location: Location?) {

                lat = location!!.latitude
                long = location.longitude
                updatesql(lat, long)
                //print("Cupcake is "+ Cupcake)
                //verify(DELNO)
                //Try the very and if that does not work, go with on response as alternative -       if(response.isSuccessful){
                //remove get api10
            }
        }


        Log.e("Skull", "Inside Update")
        val myLocation = MyLocation()
        myLocation.getLocation(this, locationResult)

        //finish()
        // this should only be like this to test.  Once I have tested only use verify not SetContent. And move finish back to top

    }

//    private fun verify() {
//
//    if(Cupcake==0){
//        println("running again")
//        println("running again")
//        println("running again")
//        println("running again")
//        println("running again")
//        println("running again")
//        Log.i("API", "Running updatesql again")
//        updatesql(lat, long)}
//
//    }

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
                viewModel.pushDelivered(uniqueId,1,Checked, lat, long, DELNO)
                viewModel. myPost2.observe(this, Observer { response ->
                    if(response.isSuccessful){

//                        Cupcake = 1
//                        Log.e("Skull", "Success")
//                        Log.e("Skull", response.body().toString())
//                        Log.e("Skull", response.code().toString())
//                        Log.e("Skull", response.message().toString())
                        finish()
                    }else {
                        Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
                        updatesql(lat, long)
//                        setContentView(R.layout.scary_screen)
//                        val tvError = findViewById<View>(R.id.tvError) as TextView
//                        tvError.text = response.code().toString()

//                        Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
//                        Log.e("RETROFIT_ERROR", response.code().toString())
//                        System.err.println("Got an exception! ")
//                        val intent = Intent(baseContext, ConnFailed::class.java)
//                        startActivity(intent)

                    }
                })
            }
            "Exc" -> {
                Log.e("Skull", "7")
                val repository = Repository()
                val viewModelFactory = MainViewModelFactory(repository)
                viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
                viewModel.pushExcept(uniqueId,2,1, lat, long, DELNO)
                Log.e("Skull", "8")
                viewModel. myPost2.observe(this, Observer { response ->
                    Log.e("Skull", "8.5")
                    if(response.isSuccessful){
                        Log.e("Skull", "9")
//                        Cupcake = 1
//                        Log.d("Main", response.body().toString())
//                        Log.d("Main", response.code().toString())
//                        Log.d("Main", response.message().toString())
//                        val i = Intent(this, MainActivity::class.java)
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                        startActivity(i)

                        finish()
                    }else {
                        Log.e("Skull", "9")
                        Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
                        updatesql(lat, long)
//                        setContentView(R.layout.scary_screen)
//                        val tvError = findViewById<View>(R.id.tvError) as TextView
//                        tvError.text = response.code().toString()

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

                viewModel. myPost3.observe(this, Observer { response ->
                    if(response.isSuccessful){
//                        Cupcake = 1
//                        Log.d("Main", response.body().toString())
//                        Log.d("Main", response.code().toString())
//                        Log.d("Main", response.message().toString())
                        finish()
                    }else {
                        Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
                        updatesql(lat, long)
//                        setContentView(R.layout.scary_screen)
//                        val tvError = findViewById<View>(R.id.tvError) as TextView
//                        tvError.text = response.code().toString()

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
                viewModel. myPost6.observe(this, Observer { response ->
                    if(response.isSuccessful){
//                        Cupcake = 1
//                        Log.e("Skull", "Happy Skull")
//                        Log.e("Skull", response.body().toString())
//                        Log.e("Skull", response.code().toString())
//                        Log.e("Skull", response.message().toString())
                        finish()
                    }else {
                        Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
                        updatesql(lat, long)
//                        setContentView(R.layout.scary_screen)
//                        val tvError = findViewById<View>(R.id.tvError) as TextView
//                        tvError.text = response.code().toString()

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

                viewModel. myPost5.observe(this, Observer { response ->
                    if(response.isSuccessful){
//                        Cupcake = 1
//                        Log.d("Main", response.body().toString())
//                        Log.d("Main", response.code().toString())
//                        Log.d("Main", response.message().toString())
                        finish()
                    }else {
                        Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
                        updatesql(lat, long)
//                        setContentView(R.layout.scary_screen)
//                        val tvError = findViewById<View>(R.id.tvError) as TextView
//                        tvError.text = response.code().toString()

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

                viewModel. myPost5.observe(this, Observer { response ->
                    if(response.isSuccessful){
//                        Cupcake = 1
//                        Log.d("Main", response.body().toString())
//                        Log.d("Main", response.code().toString())
//                        Log.d("Main", response.message().toString())
                        finish()
                    }else {
                        Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
                        updatesql(lat, long)
//                        setContentView(R.layout.scary_screen)
//                        val tvError = findViewById<View>(R.id.tvError) as TextView
//                        tvError.text = response.code().toString()

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




