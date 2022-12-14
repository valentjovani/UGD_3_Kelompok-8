package com.example.ugd_3_kelompok

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd_3_kelompok.api.MemberHotelApi
import com.example.ugd_3_kelompok.models.MemberHotelModel
import com.example.ugd_3_kelompok.room.Constant
import com.example.ugd_3_kelompok.room.MemberHotel
import com.example.ugd_3_kelompok.room.MemberHotelDB
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_member_hotel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class MemberHotelActivity : AppCompatActivity() {
    private var srHotel: SwipeRefreshLayout? =null
    private var adapter: MemberHotelAdapter? = null
    private var svHotel: SearchView? =null
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
        srHotel = findViewById(R.id.sr_hotel)
        svHotel = findViewById(R.id.sv_hotel)

        srHotel?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener { allMemberHotel() })
        svHotel?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
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
            val i = Intent(this@MemberHotelActivity, EditMemberHotel::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvProduk = findViewById<RecyclerView>(R.id.rv_hotel)
        adapter = MemberHotelAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allMemberHotel()
    }

    private fun allMemberHotel(){
        srHotel!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, MemberHotelApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonData = jsonObject.getJSONArray("data")
                val memberHotelModel : Array<MemberHotelModel> = gson.fromJson(jsonData.toString(),Array<MemberHotelModel>::class.java)

                adapter!!.setMemberHotelList(memberHotelModel)
                adapter!!.filter.filter(svHotel!!.query)
                srHotel!!.isRefreshing = false

                if(!memberHotelModel.isEmpty())
                    Toast.makeText(this@MemberHotelActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT ).show()
                else
                    Toast.makeText(this@MemberHotelActivity, "Data Kosong!", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
                srHotel!!.isRefreshing = false
                try{
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@MemberHotelActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@MemberHotelActivity, e.message, Toast.LENGTH_SHORT).show()
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

    fun deleteMemberHotel(id: Int){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, MemberHotelApi.DELETE_URL + id, Response.Listener{ response ->
                setLoading(false)

                val gson = Gson()
                var Hotel = gson.fromJson(response, MemberHotelModel::class.java)
                if(Hotel != null)
                    Toast.makeText(this@MemberHotelActivity, "Data Berhasil Dihapus",Toast.LENGTH_SHORT).show()
                allMemberHotel()
            }, Response.ErrorListener{ error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@MemberHotelActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@MemberHotelActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            // Menambahkan header pada request
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
        if(requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allMemberHotel()
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