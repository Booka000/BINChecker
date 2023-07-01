package com.albara.binchecker

import android.content.Context
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.albara.binchecker.databinding.ActivityMainBinding
import com.albara.objects.Bank
import com.albara.objects.Card
import com.albara.objects.Country
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var historyList : MutableList<String>
    private lateinit var adapter : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyList = loadData()
        adapter = makeAdapter(historyList)
        binding.history.adapter = adapter
        binding.history.onItemClickListener = AdapterView.OnItemClickListener{
                parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position)
            binding.binField.setText(selected.toString())
        }

        binding.button.setOnClickListener {
            binding.dataView.text = "Please wait"
            val bin = binding.binField.text.toString()
            fetchData(bin) {
                binding.dataView.text = it
                historyList.add(bin)
                adapter.notifyDataSetChanged()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        saveHistory()
    }

    private fun fetchData(BIN: String,callback : (result : String) -> Unit) {
        Thread{
            try {
                val url = "https://lookup.binlist.net/$BIN"
                val queue = Volley.newRequestQueue(this)
                val jsonObjectRequest = JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                    { response ->
                        val card = Card(response)
                        val country = Country(response.getJSONObject("country"))
                        val bank = Bank(response.getJSONObject("bank"))

                        val info = "Card No length : ${card.length}\n" +
                                "Luhn : ${card.luhn}\n" +
                                "Scheme / Network : ${card.scheme}\n" +
                                "Type : ${card.type}\n" +
                                "Brand : ${card.brand}\n" +
                                "Prepaid : ${card.prepaid}\n" +
                                "Country : ${country.name} ${country.emoji}\n" +
                                "Currency ${country.currency}\n" +
                                "Latitude/Longitude : ${country.latitude}/${country.longitude}\n" +
                                "Bank : ${bank.name}\n" +
                                "City : ${bank.city}\n" +
                                "Phone : ${bank.phone}\n" +
                                "Website : ${bank.url}"

                        callback(info)
                    },
                    {
                        callback("No card was found")
                    }
                )
                queue.add(jsonObjectRequest)
            } catch (e : Exception){
                e.printStackTrace()
            }
        }.start()
    }

    private fun makeAdapter(list: List<String>): ArrayAdapter<String> =
        ArrayAdapter(this, android.R.layout.simple_list_item_1, list)


    private fun saveHistory() {
        val sharedPreferences = getSharedPreferences("history", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("STRING_KEY",historyList.joinToString(","))
        }.apply()
    }

    private fun loadData() : MutableList<String>{
        val sharedPreferences = getSharedPreferences("history", Context.MODE_PRIVATE)
        val savedList = sharedPreferences.getString("STRING_KEY","")
        if (savedList.isNullOrEmpty()) return mutableListOf()
        val result = savedList.split(",").map { it.trim() }
        return result.toMutableList()
    }

}

