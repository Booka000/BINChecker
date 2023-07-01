package com.albara.objects

import org.json.JSONObject

class Bank (jsonObject: JSONObject) {
    val name: String = jsonObject.optString("name","None")
    val url: String = jsonObject.optString("url","None")
    val phone: String = jsonObject.optString("phone","None")
    val city: String = jsonObject.optString("city","None")
}