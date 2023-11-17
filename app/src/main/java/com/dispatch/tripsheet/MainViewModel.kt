package com.dispatch.tripsheet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dispatch.tripsheet.model.*
import com.dispatch.tripsheet.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {

    var myResponse: MutableLiveData<Response<List<Cell>>> = MutableLiveData()
    var myResponse2: MutableLiveData<Response<List<Cell2>>> = MutableLiveData()
    var myPost: MutableLiveData<Response<exData>> = MutableLiveData()
    var myPost2: MutableLiveData<Response<delData>> = MutableLiveData()
    var myPost3: MutableLiveData<Response<otwData>> = MutableLiveData()
    var myPost4: MutableLiveData<Response<notDelData>> = MutableLiveData()
    var myPost5: MutableLiveData<Response<palletData>> = MutableLiveData()
    var myPost6: MutableLiveData<Response<clearData>> = MutableLiveData()



    fun getTripsheet(uniqueId: String){
        viewModelScope.launch {
            val response = repository.getTripsheet(uniqueId)
            myResponse.value = response
        }
    }

    fun getException(uniqueId: String, delno: String) {
        viewModelScope.launch {
            val response = repository.getException(uniqueId, delno)
            myResponse2.value = response
        }
    }




    fun pushException(uniqueId: String, EReason: Int, EQTY: Int, DESCRIPTION: String, delno: String) {
        viewModelScope.launch {


            val response = repository.pushException(uniqueId, EReason, EQTY, DESCRIPTION, delno)
            myPost.value = response


        }
    }

    fun pushDelivered(uniqueId: String, DELIVERED: Int = 1, checked: Int,lat: Double, long: Double, delno: String) {
        viewModelScope.launch {


            val response = repository.pushDelivered(uniqueId, DELIVERED, checked, lat, long, delno)
            myPost2.value = response


        }
    }


    fun pushExcept(uniqueId: String, DELIVERED: Int = 2, checked: Int = 1,lat: Double, long: Double, delno: String) {
        viewModelScope.launch {


            val response = repository.pushExcept(uniqueId, DELIVERED, checked, lat, long, delno)
            myPost2.value = response


        }
    }
    fun pushOnTheWay(uniqueId: String, DELIVERED: Int = 5, mailOTW: Int = 1,lat: Double, long: Double, delno: String) {
        viewModelScope.launch {


            val response = repository.pushOnTheWay(uniqueId, DELIVERED, mailOTW, lat, long, delno)
            myPost3.value = response


        }
    }

    fun pushNotDel(uniqueId: String, NOT_DELIVERED: Int = 1, delno: String, selectedReason: Int) {
        viewModelScope.launch {


            val response = repository.pushNotDel(uniqueId, NOT_DELIVERED, delno, selectedReason)
            myPost4.value = response


        }
    }

    fun pushDroppedPallets(uniqueId: String, pallet: Int, delno: String) {
        viewModelScope.launch {


            val response = repository.pushDroppedPallets(uniqueId, pallet, delno)
            myPost5.value = response


        }
    }

    fun pushClear(uniqueId: String, DELIVERED: Int = 0, delno: String) {
        viewModelScope.launch {


            val response = repository.pushClear(uniqueId, DELIVERED, delno)
            myPost6.value = response


        }
    }

    fun pushCollectPallets(uniqueId: String, pallet: Int, delno: String) {
        viewModelScope.launch {


            val response = repository.pushCollectPallets(uniqueId, pallet, delno)
            myPost5.value = response


        }
    }

}