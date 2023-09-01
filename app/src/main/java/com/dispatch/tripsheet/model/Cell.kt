package com.dispatch.tripsheet.model

data class Cell(
    var TRIPSHEETNO: Int,
    var WONUMBER: String,
    var DELNO: String,
    var CUSTOMER: String,
    var DRIVER: String,
    var INVOICE: String,
    var WEIGHT: Double,
    var DELIVERED: Int,
    var PALLETS: Int
)

data class Cell2(
    var Exception: Boolean = false,
    var DESCRIPTION: String,
    var QTY_DELV: Int,
    var EReason: Int,
    var EQTY: Int
)

data class exData(
    var uniqueId: String? =null,
    var EReason: Int? =null,
    var eqty: Int? =null ,
    var Pname: String? =null,
    var DELNO: String? =null
)

data class delData(
    var uniqueId: String? =null,
    var DELIVERED: Int = 5,
    var checked: Int? =null,
    var lat: Double? =null,
    var long: Double? =null,
    var delno: String? =null
)

data class otwData(
    var uniqueId: String? =null,
    var DELIVERED: Int = 5,
    var mailOTW: Int = 1,
    var lat: Double? =null,
    var long: Double? =null,
    var delno: String? =null
)

data class notDelData(
    var uniqueId: String,
    var NOT_DELIVERED: Int = 1,
    var delno: String
)

data class palletData(
    var uniqueId: String,
    var NOT_DELIVERED: Int = 1,
    var delno: String
)

data class clearData(
    var uniqueId: String,
    var NOT_DELIVERED: Int = 1,
    var delno: String
)


data class ExceptionDetails(
    val pname: String,
    val eqty: Int,
    val reason: String
    )
