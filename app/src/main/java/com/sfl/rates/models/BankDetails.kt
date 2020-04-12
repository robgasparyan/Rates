package com.sfl.rates.models


import com.google.gson.annotations.SerializedName

data class BankDetails(
    @SerializedName("date")
    val date: Double? = 0.0, // 1386592265.2065647
    @SerializedName("list")
    val list: Map<String, BankBranchDetail> = mapOf()
) {
    data class BankBranchDetail(
        @SerializedName("address")
        val address: Address? = Address(),
        @SerializedName("contacts")
        val contacts: String? = "", // (374 10) 20 79 77
        @SerializedName("head")
        val head: Int? = 0, // 0
        @SerializedName("location")
        val location: Location? = Location(),
        @SerializedName("title")
        val title: Title? = Title(),
        @SerializedName("workhours")
        val workhours: List<Workhour> = listOf()
    ) {
        data class Address(
            @SerializedName("am")
            val am: String? = "", // Կոմիտասի պող., 38/2-62
            @SerializedName("en")
            val en: String? = "", // Կոմիտասի պող., 38/2-62
            @SerializedName("ru")
            val ru: String? = "" // Կոմիտասի պող., 38/2-62
        )

        data class Location(
            @SerializedName("lat")
            val lat: Double? = 0.0, // 40.20696748074688
            @SerializedName("lng")
            val lng: Double? = 0.0 // 44.51557159423828
        )

        data class Title(
            @SerializedName("am")
            val am: String? = "", // Արաբկիր
            @SerializedName("en")
            val en: String? = "", // Արաբկիր
            @SerializedName("ru")
            val ru: String? = "" // Արաբկիր
        )

        data class Workhour(
            @SerializedName("days")
            val days: String? = "", // 1-5
            @SerializedName("hours")
            val hours: String? = "" // 09:05-17:00
        )
    }
}