package com.example.ugd_3_kelompok

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd_3_kelompok.api.PelangganApi
import com.example.ugd_3_kelompok.models.Pelanggan
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_pelanggan.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class AddEditPelangganActivity : AppCompatActivity() {
    companion object{
        private val GENDER_LIST = arrayOf("Laki - Laki", "Perempuan")
    }

    private var etNama: EditText? = null
    private var etUmur: EditText? = null
    private var etNotelp: EditText? = null
    private var edGender: AutoCompleteTextView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_pelanggan)

        queue = Volley.newRequestQueue(this)
        etNama = findViewById(R.id.et_nama)
        etUmur = findViewById(R.id.et_umur)
        etNotelp = findViewById(R.id.et_notelp)
        edGender = findViewById(R.id.ed_gender)
        layoutLoading = findViewById(R.id.layout_loading)

        setExposedDropDownMenu()

        val btnCencel = findViewById<Button>(R.id.btn_cencel)
        btnCencel.setOnClickListener{ finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getLongExtra("id", -1)
        if(id == -1L) {
            tvTitle.setText("Tambah Pelanggan")
            btnSave.setOnClickListener { createPelanggan() }
        }else{
            tvTitle.setText("Edit Pelanggan")
            getPelangganById(id)
            btnSave.setOnClickListener { updataPelanggan(id) }
        }
    }

    fun setExposedDropDownMenu() {
        val adapterGender: ArrayAdapter<String> = ArrayAdapter<String>(this,
        R.layout.item_list, GENDER_LIST)
        edGender!!.setAdapter(adapterGender)
    }

    private fun getPelangganById(id: Long) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PelangganApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val pelanggan = gson.fromJson(response, Pelanggan::class.java)

                etNama!!.setText(pelanggan.nama)
                etUmur!!.setText(pelanggan.umur)
                etNotelp!!.setText(pelanggan.notelp)
                edGender!!.setText(pelanggan.gender)
                setExposedDropDownMenu()

                Toast.makeText(this@AddEditPelangganActivity, "Data berhasil diambil!", Toast.LENGTH_SHORT).show()
                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditPelangganActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@AddEditPelangganActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override  fun getHeaders(): Map<String, String>{
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }
            }
            queue!!.add(stringRequest)
    }
    private fun createPelanggan(){
        setLoading(true)

        val pelanggan = Pelanggan(
            etNama!!.text.toString(),
            etUmur!!.text.toString(),
            etNotelp!!.text.toString(),
            edGender!!.text.toString()
        )

        val stringRequest: StringRequest =
            object  : StringRequest(Method.POST, PelangganApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                var pelanggan = gson.fromJson(response, Pelanggan::class.java)

                if(pelanggan != null)
                    Toast.makeText(this@AddEditPelangganActivity, "Data Berhasil Ditambah", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            },Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditPelangganActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@AddEditPelangganActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(pelanggan)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }
    private fun setLoading(isLoading: Boolean){
        if(isLoading){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.INVISIBLE
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }
    private fun updataPelanggan(id: Long){
        setLoading(true)

        val pelanggan = Pelanggan(
            etNama!!.text.toString(),
            etUmur!!.text.toString(),
            etNotelp!!.text.toString(),
            edGender!!.text.toString()
        )

        val stringRequest: StringRequest =
            object  : StringRequest(Method.PUT, PelangganApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()
                var pelanggan = gson.fromJson(response, Pelanggan::class.java)

                if(pelanggan != null)
                    Toast.makeText(this@AddEditPelangganActivity, "Data Berhasil Diupdate", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            },Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditPelangganActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@AddEditPelangganActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(pelanggan)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }
}