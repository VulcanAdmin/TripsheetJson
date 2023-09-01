package com.dispatch.tripsheet.api

import com.dispatch.tripsheet.model.*
import retrofit2.Response
import retrofit2.http.*

interface API {
    @GET("/?q=1")
    suspend fun getTripsheet(
        @Query("ID") uniqueId: String
    ): Response<List<Cell>>

    @GET("/?q=2")
    suspend fun getException(
        @Query("ID") uniqueId: String,
        @Query("p1") delno: String
    ): Response<List<Cell2>>


    @POST("/?q=3")
    suspend fun pushException(
        @Query("ID") uniqueId: String,
        @Query("p1") EReason: Int,
        @Query("p2") EQTY: Int,
        @Query("p3") DESCRIPTION: String,
        @Query("p4") delno: String
    ): Response<exData>


    @POST("/?q=4")
    suspend fun pushDeliveredChecked(
        @Query("ID") uniqueId: String,
        @Query("p1") DELIVERED: Int = 1,
        @Query("p2") checked: Int = 1,
        @Query("p3") lat: Double,
        @Query("p4") long: Double,
        @Query("p5") delno: String
    ): Response<delData>


    @POST("/?q=4")
    suspend fun pushDeliveredUnchecked(
        @Query("ID") uniqueId: String,
        @Query("p1") DELIVERED: Int = 2,
        @Query("p2") checked: Int = 2,
        @Query("p3") lat: Double,
        @Query("p4") long: Double,
        @Query("p5") delno: String
    ): Response<delData>


    @POST("/?q=5")
    suspend fun pushOnTheWay(
        @Query("ID") uniqueId: String,
        @Query("p1") DELIVERED: Int = 5,
        @Query("p2") mailOTW: Int = 1,
        @Query("p3") lat: Double,
        @Query("p4") long: Double,
        @Query("p5") delno: String
    ): Response<otwData>


    @POST("/?q=6")
    suspend fun pushNotDel(
        @Query("ID") uniqueId: String,
        @Query("p1") NOT_DELIVERED: Int = 1,
        @Query("p2") delno: String,
        @Query("p3") selectedReason: Int
    ): Response<notDelData>


    @POST("/?q=7")
    suspend fun pushDroppedPallets(
        @Query("ID") uniqueId: String,
        @Query("p1") pallet: Int,
        @Query("p2") delno: String
    ): Response<palletData>


    @POST("/?q=8")
    suspend fun pushClear(
        @Query("ID") uniqueId: String,
        @Query("p1") DELIVERED: Int = 0,
        @Query("p2") delno: String
    ): Response<clearData>

    @POST("/?q=9")
    suspend fun pushCollectPallets(
        @Query("ID") uniqueId: String,
        @Query("p1") pallet: Int,
        @Query("p2") delno: String
    ): Response<palletData>
}