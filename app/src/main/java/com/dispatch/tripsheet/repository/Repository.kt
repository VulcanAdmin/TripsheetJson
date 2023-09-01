package com.dispatch.tripsheet.repository

import com.dispatch.tripsheet.api.RetrofitInstance
import com.dispatch.tripsheet.model.*
import retrofit2.Response

class Repository {

    suspend fun getTripsheet(uniqueId: String): Response<List<Cell>> {
        return RetrofitInstance.api.getTripsheet(uniqueId)
    }

    suspend fun getException(uniqueId: String, delno: String): Response<List<Cell2>> {
        return RetrofitInstance.api.getException(uniqueId, delno)
    }

    suspend fun pushException(uniqueId: String, EReason: Int, EQTY: Int, DESCRIPTION: String, delno: String): Response<exData> {
        return RetrofitInstance.api.pushException(uniqueId, EReason, EQTY, DESCRIPTION, delno)
    }

    suspend fun pushDeliveredChecked(uniqueId: String, DELIVERED: Int = 1, checked: Int = 1,lat: Double, long: Double, delno: String): Response<delData> {
        return RetrofitInstance.api.pushDeliveredChecked(uniqueId, DELIVERED, checked, lat, long, delno)
    }

    suspend fun pushDeliveredUnchecked(uniqueId: String, DELIVERED: Int = 2, checked: Int = 2,lat: Double, long: Double, delno: String): Response<delData> {
        return RetrofitInstance.api.pushDeliveredUnchecked(uniqueId, DELIVERED, checked, lat, long, delno)
    }

    suspend fun pushOnTheWay(uniqueId: String, DELIVERED: Int = 5, mailOTW: Int = 1,lat: Double, long: Double, delno: String): Response<otwData> {
        return RetrofitInstance.api.pushOnTheWay(uniqueId, DELIVERED, mailOTW, lat, long, delno)
    }

    suspend fun pushNotDel(
        uniqueId: String,
        NOT_DELIVERED: Int = 1,
        delno: String,
        selectedReason: Int
    ): Response<notDelData> {
        return RetrofitInstance.api.pushNotDel(uniqueId, NOT_DELIVERED, delno, selectedReason)
    }

    suspend fun pushDroppedPallets(uniqueId: String, pallet: Int, delno: String): Response<palletData> {
        return RetrofitInstance.api.pushDroppedPallets(uniqueId, pallet, delno)
    }

    suspend fun pushClear(uniqueId: String, DELIVERED: Int = 0, delno: String): Response<clearData> {
        return RetrofitInstance.api.pushClear(uniqueId, DELIVERED, delno)
    }

    suspend fun pushCollectPallets(uniqueId: String, pallet: Int, delno: String): Response<palletData> {
        return RetrofitInstance.api.pushCollectPallets(uniqueId, pallet, delno)
    }

}