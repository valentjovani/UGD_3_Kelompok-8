package com.example.ugd_3_kelompok

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
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
import com.example.ugd_3_kelompok.adapters.PelangganAdapter
import com.example.ugd_3_kelompok.api.PelangganApi
import com.example.ugd_3_kelompok.models.Pelanggan
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class RetrofitPelanggan: AppCompatActivity() {
    private var srPelanggan: SwipeRefreshLayout? = null
    private var adapter: PelangganAdapter? = null
    private var svPelanggan: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object{
        const val LUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.retrofit_pelanggan)

        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srPelanggan = findViewById(R.id.sr_pelanggan)
        svPelanggan = findViewById(R.id.sv_pelanggan)

        srPelanggan?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { allPelanggan() })
        svPelanggan?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })

        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener{
            val i = Intent(this@RetrofitPelanggan, AddEditPelangganActivity::class.java)
            startActivityForResult(i, LUNCH_ADD_ACTIVITY)
        }

        val rvProduck = findViewById<RecyclerView>(R.id.rv_pelanggan)
        adapter = PelangganAdapter(ArrayList(), this)
        rvProduck.layoutManager = LinearLayoutManager(this)
        rvProduck.adapter = adapter
        allPelanggan()
    }

    private fun allPelanggan(){
        srPelanggan!!.isRefreshing = true
        val StringRequest : StringRequest = object :
            StringRequest(Method.GET, PelangganApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                var pelanggan : Array<Pelanggan> = gson.fromJson(response, Array<Pelanggan>::class.java)

                adapter!!.setMahasiswaList(pelanggan)
                adapter!!.filter.filter(svPelanggan!!.query)
                srPelanggan!!.isRefreshing = false

                if(!pelanggan.isEmpty())
                    Toast.makeText(this@RetrofitPelanggan, "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@RetrofitPelanggan, "Data Kosong", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
                srPelanggan!!.isRefreshing = false
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@RetrofitPelanggan,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@RetrofitPelanggan, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(StringRequest)
    }

    fun deletePelanggan(id: Long){
        setLoading(true)
        val StringRequest : StringRequest = object :
            StringRequest(Method.DELETE, PelangganApi.DELETE_URL + id,  Response.Listener { response ->
                setLoading(false)

                val gson = Gson()
                var pelanggan = gson.fromJson(response, Pelanggan::class.java)
                if(pelanggan != null)
                    Toast.makeText(this@RetrofitPelanggan, "Data Berhasil Dihapus!", Toast.LENGTH_SHORT).show()
                allPelanggan()
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@RetrofitPelanggan,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: java.lang.Exception){
                    Toast.makeText(this@RetrofitPelanggan, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(StringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == LUNCH_ADD_ACTIVITY && resultCode == AppCompatActivity.RESULT_OK) allPelanggan()
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
            layoutLoading!!.visibility = View.GONE
        }
    }
}