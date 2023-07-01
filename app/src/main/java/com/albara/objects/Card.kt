package com.albara.objects

import org.json.JSONObject

class Card (jsonObject : JSONObject) {
    val length: Int = jsonObject.getJSONObject("number").optInt("length")
    val luhn:String = if(jsonObject.getJSONObject("number").has("luhn"))
        if(jsonObject.getJSONObject("number").getBoolean("luhn"))
        "YES" else "NO" else "None"
    val scheme: String = jsonObject.optString("scheme","None")
    val type: String = jsonObject.optString("type","None")
    val brand: String = jsonObject.optString("brand","None")
    val prepaid: String = if(jsonObject.has("prepaid"))
        if(jsonObject.getBoolean("prepaid"))
            "YES" else "NO" else "None"
}