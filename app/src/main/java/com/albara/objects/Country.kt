package com.albara.objects

import org.json.JSONObject

class Country(jsonObject: JSONObject) {

    val name: String = jsonObject.optString("name", "None")
    val emoji: String = jsonObject.optString("emoji", "None")
    val currency: String = jsonObject.optString("currency", "None")
    val latitude: Int = jsonObject.optInt("latitude")
    val longitude: Int = jsonObject.optInt("longitude")

}