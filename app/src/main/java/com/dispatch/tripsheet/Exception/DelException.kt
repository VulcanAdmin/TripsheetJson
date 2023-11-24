package com.dispatch.tripsheet.Exception

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dispatch.tripsheet.Main.MainActivity
import com.dispatch.tripsheet.MainViewModel
import com.dispatch.tripsheet.R
import com.dispatch.tripsheet.Update
import com.dispatch.tripsheet.model.Cell2
import com.dispatch.tripsheet.model.ExceptionDetails
import com.dispatch.tripsheet.repository.Repository
import com.example.retrofittest.MainViewModelFactory
import com.example.retrofittest.utils.Constants.Companion.uniqueId
import kotlinx.android.synthetic.main.activity_ex_form.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/// Check reason before photo check!

class DelException : AppCompatActivity(){

    private var exceptionList = ArrayList<Cell2>()
    private lateinit var viewModel: MainViewModel
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private val CAMERA_PERMISSION_CODE = 1001
    private var imageUri: Uri? = null
    private var imageFile: File? = null
    val exceptionDetailsList = mutableListOf<ExceptionDetails>()
    private var currentPhotoPath: String? = null

//    companion object {
//        private const val MAX_WIDTH = 1920
//        private const val MAX_HEIGHT = 1080
//        private const val COMPRESSION_QUALITY = 60
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        println("Location DelException  Exception")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ex_form)

        Log.e("Skull", "1")
        imageFile = createImageFile()
        val bundle = intent.extras

        var DELNO: String = "null"
        DELNO = bundle!!.getString("DELNO").toString()
//         var Driver: String? = null
//         Driver = bundle!!.getString("Driver")
//         tvDriver.text = Driver

        tvDriver.text = bundle.getString("Driver")

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        tvDate.text = sdf.format(Date())

        recyclerViewPartlist.layoutManager = LinearLayoutManager(this)
        recyclerViewPartlist.setItemViewCacheSize(30)

        var partsList = ArrayList<Cell2>()

        var adapter = ArrayAdapter<String>(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )

        conn(partsList, DELNO, adapter)

        var btnSend: Button = findViewById(R.id.btnSend)
        var btnBack: Button = findViewById(R.id.btnBack)
        var cBPhoto: CheckBox = findViewById(R.id.cBPhoto)
        var btnNotDel: Button = findViewById(R.id.btnNotDelivered)

        btnBack.setOnClickListener {
        //    finish()

            val i = Intent(this, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
        }




        btnNotDel.setOnClickListener {

            Log.e("Skull", "2")
            val dialogView = layoutInflater.inflate(R.layout.dialog_layout, null)
            val spinnerReason: Spinner = dialogView.findViewById(R.id.spinnerReason)

            val reasonsAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.notDelivered_reasons,
                android.R.layout.simple_spinner_item
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            spinnerReason.adapter = reasonsAdapter

            val builder = AlertDialog.Builder(this)
                .setTitle("Select Reason")
                .setView(dialogView)
                .setPositiveButton("OK") { _, _ ->

                    Log.e("Skull", "3")
                    val selectedReason = resources.getIntArray(R.array.notDelv_reasons_values)[spinnerReason.selectedItemPosition] //not optimal but used for flexibility
                    sendNotDel(DELNO, selectedReason)


                    // this.finish()


                    // dialog.dismiss()
                }
                .setNeutralButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }


            val dialog = builder.create()
            dialog.show()

        }


        takePictureLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (imageUri != null) {
                    val compressedImageFile = compressImage(imageUri!!)
                    val compressedImageUri = FileProvider.getUriForFile(
                        this,
                        "com.dispatch.tripsheet.fileprovider",
                        compressedImageFile
                    )
                    sendEmailWithAttachment(DELNO)
                } else {
                    // Handle the case when imageUri is null
                    Toast.makeText(this, "Image URI is null", Toast.LENGTH_SHORT).show()
                }



                // Proceed with sending the email
                //sendEmailWithAttachment(imageUri)

//                val emailIntent = Intent(Intent.ACTION_SEND)
//                emailIntent.type = "text/plain"
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("hancke@vulcansteel.co.za"))
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Exc -DELNO:$DELNO")
//
//                val emailBody = StringBuilder()
//                exceptionDetailsList.forEach {
//                    emailBody.append("Part: ${it.pname}\n")
//                    emailBody.append("Reason: ${it.reason}\n")
//                    emailBody.append("Quantity: ${it.eqty}\n\n")
//                }
//                emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody.toString())
//
//                if (imageUri != null) {
//                    emailIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
//                    emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                }
//
//                startActivity(Intent.createChooser(emailIntent, "Send email..."))
//
                updateException(exceptionList, DELNO)
            }
        }



        btnSend.setOnClickListener {
            try {

                exceptionList = ArrayList(partsList.filter { (it.Exception) })

                // Clear the existing list before populating it
                exceptionDetailsList.clear()

                exceptionList.forEach { it ->
                    val pname = it.DESCRIPTION
                    val eqty = it.EQTY
                    var reason = "Unknown"

                    when (it.EReason) {
                        1 -> reason = "Parts short"
                        2 -> reason = "Parts incorrect"
                        3 -> reason = "Parts damaged"
                        4 -> reason = "Parts not bent/bent wrong"
                        5 -> reason = "Over supplied"
                        6 -> reason = "Incorrectly supplied"
                        7 -> reason = "Parts not loaded"
                        8 -> reason = "Contact Driver"
                        9 -> reason = "Contact Customer"
                    }

                    // Add exception details to the list
                    exceptionDetailsList.add(ExceptionDetails(pname, eqty, reason))
                }
                println("exceptionDetailsList")
                println(exceptionDetailsList)

                if (exceptionList.any {0 == it.EReason })
                {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Please select a reason for all exception")
                    //    Toast.makeText(context,"Data capture canceled",Toast.LENGTH_LONG).show()
                    builder.setIcon(android.R.drawable.ic_dialog_alert)
                    builder.setPositiveButton("OK"){dialogInterface , which ->
                    }
                    val alertDialog: AlertDialog = builder.create()

                    alertDialog.setCancelable(true)
                    alertDialog.show()

                }else {


                    if (cBPhoto.isChecked) {
                        // Request camera permission if not granted
                        if (ContextCompat.checkSelfPermission(
                                this,
                                Manifest.permission.CAMERA
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf(Manifest.permission.CAMERA),
                                CAMERA_PERMISSION_CODE
                            )
                        } else {
                            // Permission is already granted, proceed with camera usage
                            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            if (takePictureIntent.resolveActivity(packageManager) != null) {
                                val photoFile: File? =
                                    createImageFile() // Create a File object for the image


                                photoFile?.also {
                                    //val photoURI: Uri = FileProvider.getUriForFile(this, "com.yourapp.fileprovider", it)
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

                                    takePictureLauncher.launch(takePictureIntent)
                                }
                            }
                        }

                    } else {
                        updateException(exceptionList, DELNO)
                    }
                }



            } catch (e: Exception) {
                // Log the exception details
                Log.e("Exception", "Exception occurred: ${e.message}")
                e.printStackTrace()
            }

        }

        Log.e("Skull", "6")

    }

    private fun sendNotDel(DELNO: String, selectedReason: Int) {

        Log.e("Skull", "4")
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.pushNotDel(uniqueId,1, DELNO, selectedReason)


//                     val bundle2 = Bundle()
//                     val intent2 = Intent(this, Update::class.java)
//                     intent2.putExtra("Status","Exc")
//                     intent2.putExtra("DELNO",DELNO)
//                     ContextCompat.startActivity(this, intent2, bundle2)

//                     finish()
//                     val i = Intent(this, MainActivity::class.java)
//                     i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                     startActivity(i)

        //works



        viewModel.myPost4.observe(this, Observer { response ->
            if(response.isSuccessful){
                Log.e("Skull", "5")
                gotException(DELNO)
//                             this.finish()
//                             Log.d("Main", response.body().toString())
//                             Log.d("Main", response.code().toString())
//                             Log.d("Main", response.message().toString())
                //run to post exception
                this.finish()

            }else {
                Log.e("Skull", "5")
                Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
                sendNotDel(DELNO, selectedReason)
//                setContentView(R.layout.scary_screen)
//                val tvError = findViewById<View>(R.id.tvError) as TextView
//                tvError.text = response.code().toString()

//                             Log.e("RETROFIT_ERROR", response.code().toString())
//                             System.err.println("Got an exception! ")
//                             val intent = Intent(baseContext, ConnFailed::class.java)
//                             startActivity(intent)
            }
        })




    }


    private fun sendEmailWithAttachment( DELNO: String) {
//        val compressedImageFile = compressImage(imageUri!!)
//
//
//        val compressedImageUri = FileProvider.getUriForFile(
//            this,
//            "com.dispatch.tripsheet.fileprovider",
//            compressedImageFile
//        )

        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("diekie@vulcansteel.co.za"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Exception Report - DELNO: $DELNO")

        val emailBody = StringBuilder()
        exceptionDetailsList.forEach {
            emailBody.append("Part: ${it.pname}\n")
            emailBody.append("Reason: ${it.reason}\n")
            emailBody.append("Quantity: ${it.eqty}\n\n")
        }
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody.toString())

        if (imageUri != null) {
            val mimeType = "image/jpeg"
            val mimeTypeArray = arrayOf<String>(mimeType)
            // Compress the image
            val compressedImageFile = compressImage(imageUri!!)
            val compressedImageUri = FileProvider.getUriForFile(
                this,
                "com.dispatch.tripsheet.fileprovider",
                compressedImageFile
            )


//            // Grant URI permission to the email app
//            grantUriPermission(
//                "com.google.android.gm",  // Replace with the appropriate package name
//                compressedImageUri,
//                Intent.FLAG_GRANT_READ_URI_PERMISSION
//            )
//
//            // Attach the compressed image URI to the email intent
//            emailIntent.putExtra(Intent.EXTRA_STREAM, compressedImageUri)
//            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)


            // Add the uri as a ClipData
            emailIntent.setClipData(
                ClipData(
                    "Picture from D",
                    mimeTypeArray,
                    ClipData.Item(compressedImageUri)
                )
            )

            // EXTRA_STREAM is kept for compatibility with old applications
            emailIntent.putExtra(Intent.EXTRA_STREAM, compressedImageUri)
            emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        }

        startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }

    private fun compressImage(uri: Uri): File {
        val originalBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val maxWidth = originalBitmap.width / 2
        val maxHeight = originalBitmap.height / 2
        val compressedBitmap = Bitmap.createScaledBitmap(originalBitmap, maxWidth, maxHeight, true)

        // Save the compressed image in the same directory
        val compressedFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "compressed_${System.currentTimeMillis()}.jpg")

        val outputStream = FileOutputStream(compressedFile)
        compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        outputStream.close()

        return compressedFile
    }




    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return try {
            File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
                imageUri = FileProvider.getUriForFile(
                    this@DelException,
                    "com.dispatch.tripsheet.fileprovider",
                    this
                )
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with camera usage
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(packageManager) != null) {
                    takePictureLauncher.launch(takePictureIntent)
                }
            } else {
                // Permission denied
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                // Optionally, guide the user to app settings to enable the permission manually
                openAppSettings()
            }
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun updateException(exceptionList: ArrayList<Cell2>, DELNO: String) {


            exceptionList.forEach {
                var Pname = it.DESCRIPTION
                var reason = it.EReason
                var eqty = it.EQTY

                val repository = Repository()
                val viewModelFactory = MainViewModelFactory(repository)
                viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

                viewModel.pushException(uniqueId,reason,eqty, Pname, DELNO)


                //Looking for one successful response currently.
//                viewModel.myPost.observe(this, Observer { response ->
//                    if(response.isSuccessful){
//                        this.finish()
////                        Log.d("Main", response.body().toString())
////                        Log.d("Main", response.code().toString())
////                        Log.d("Main", response.message().toString())
//                    }else {
//                        Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
//                        Log.e("RETROFIT_ERROR", response.code().toString())
//                        System.err.println("Got an exception! ")
//                        val intent = Intent(baseContext, ConnFailed::class.java)
//                        startActivity(intent)
//
//                    }
//                })
            }

        viewModel.myPost.observe(this, Observer { response ->
            if(response.isSuccessful){
                gotException(DELNO)
                finish()
            }else {
                updateException(exceptionList, DELNO)
                Toast.makeText(this, response.code(), Toast.LENGTH_LONG).show()
//                Log.e("RETROFIT_ERROR", response.code().toString())
//                System.err.println("Got an exception! ")
//                val intent = Intent(baseContext, ConnFailed::class.java)
//                startActivity(intent)

            }
        })

//            gotException(DELNO)
//
//
//            finish()
//                     val i = Intent(this, MainActivity::class.java)
//                     i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                     startActivity(i)

    }

    private fun gotException(DELNO: String) {

        val bundle2 = Bundle()
        val intent2 = Intent(baseContext, Update::class.java)
        intent2.putExtra("Status","Exc")
        intent2.putExtra("DELNO",DELNO)
        startActivity(intent2, bundle2)
    }

    private fun conn(partsList: ArrayList<Cell2>, delno: String, adapter: ArrayAdapter<String>)    {



        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getException(uniqueId, delno)




        //Log.d("API", "Connect to intranet")


        viewModel.myResponse2.observe(this, Observer { response ->
            if(response.isSuccessful){

//                Log.d("Main", response.body().toString())
//                Log.d("Main", response.code().toString())
//                Log.d("Main", response.headers().toString())

                val items = response.body()


                //Log.d("Clicked", " This is the list print")


                if (items != null) {
                    for (i in 0 until items.count()) {

                        // Part number
                        val Pname = items[i].DESCRIPTION ?: "N/A"

                        // PQTY
                        val PQTY = items[i].QTY_DELV ?: "N/A"

                        val list =
                            Cell2(
                                false,
                                Pname as String,
                                PQTY as Int,
                                0,
                                0

                            )

                        partsList.add(list)
                    }
                }

                runOnUiThread { recyclerViewPartlist.adapter = ReturnFormAdapter(
                    partsList,
                    context = this@DelException,
                    adapter,
                    hdrcbExc
                ) } }else {

                Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
                conn(partsList, delno, adapter)
//                val intent = Intent(baseContext, ConnFailed::class.java)
//                startActivity(intent)
            }

        })

    }
}


