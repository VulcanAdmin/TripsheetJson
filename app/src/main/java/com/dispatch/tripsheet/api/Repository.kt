package com.dispatch.tripsheet.api

import com.dispatch.tripsheet.model.Cell
import com.dispatch.tripsheet.model.Cell2
import com.dispatch.tripsheet.model.clearData
import com.dispatch.tripsheet.model.delData
import com.dispatch.tripsheet.model.exData
import com.dispatch.tripsheet.model.notDelData
import com.dispatch.tripsheet.model.otwData
import com.dispatch.tripsheet.model.palletData
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

    suspend fun pushDelivered(uniqueId: String, DELIVERED: Int = 1, checked: Int,lat: Double, long: Double, delno: String): Response<delData> {
        return RetrofitInstance.api.pushDelivered(uniqueId, DELIVERED, checked, lat, long, delno)
    }

    suspend fun pushExcept(uniqueId: String, DELIVERED: Int = 2, checked: Int = 1,lat: Double, long: Double, delno: String): Response<delData> {
        return RetrofitInstance.api.pushExcept(uniqueId, DELIVERED, checked, lat, long, delno)
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