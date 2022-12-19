package com.example.ugd_3_kelompok

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd_3_kelompok.api.MemberHotelApi
import com.example.ugd_3_kelompok.api.PemesananKamarApi
import com.example.ugd_3_kelompok.models.MemberHotelModel
import com.example.ugd_3_kelompok.models.PemesananKamarModel
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class PemesananKamarActivity : AppCompatActivity() {
    private var srKamar: SwipeRefreshLayout? =null
    private var adapter: PemesananKamarAdapter? = null
    private var svKamar: SearchView? =null
    private var layoutLoading: LinearLayout? =null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_hotel)

        queue =  Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srKamar = findViewById(R.id.sr_kamar)
        svKamar = findViewById(R.id.sv_kamar)

        srKamar?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener { allMemberKamar() })
        svKamar?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                adapter!!.filter.filter(s)

                return false
            }
        })

        val fabAdd = findViewById<Button>(R.id.btnAdd)
        fabAdd.setOnClickListener{
            val i = Intent(this@PemesananKamarActivity, EditPemesananKamar::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvProduk = findViewById<RecyclerView>(R.id.rv_hotel)
        adapter = PemesananKamarAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allMemberKamar()
    }

    private fun allMemberKamar(){
        srKamar!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, MemberHotelApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonData = jsonObject.getJSONArray("data")
                val pemesananKamarModel : Array<PemesananKamarModel> = gson.fromJson(jsonData.toString(),Array<PemesananKamarModel>::class.java)

                adapter!!.setPemesananKamarList(pemesananKamarModel)
                adapter!!.filter.filter(svKamar!!.query)
                srKamar!!.isRefreshing = false

                if(!pemesananKamarModel.isEmpty())
                    Toast.makeText(this@PemesananKamarActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT ).show()
                else
                    Toast.makeText(this@PemesananKamarActivity, "Data Kosong!", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
                srKamar!!.isRefreshing = false
                try{
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@PemesananKamarActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@PemesananKamarActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    fun deletePemesanaKamar(id: Int){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, PemesananKamarApi.DELETE_URL + id, Response.Listener{ response ->
                setLoading(false)

                val gson = Gson()
                var Kamar = gson.fromJson(response, PemesananKamarModel::class.java)
                if(Kamar != null)
                    Toast.makeText(this@PemesananKamarActivity, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                allMemberKamar()
            }, Response.ErrorListener{ error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@PemesananKamarActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@PemesananKamarActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = java.util.HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers

            }

        }
        queue!!.add(stringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(resultCode, resultCode, data)
        if(requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allMemberKamar()
    }

    private fun setLoading(isLoading: Boolean){
        if(isLoading){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }
}