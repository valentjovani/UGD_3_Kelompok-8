package com.example.ugd_3_kelompok

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd_3_kelompok.api.MemberHotelApi
import com.example.ugd_3_kelompok.api.PemesananKamarApi
import com.example.ugd_3_kelompok.models.MemberHotelModel
import com.example.ugd_3_kelompok.models.PemesananKamarModel
import com.example.ugd_3_kelompok.room.MemberHotel
import com.google.gson.Gson
import org.json.JSONObject
import java.io.FileNotFoundException
import java.nio.charset.StandardCharsets

class EditPemesananKamar : AppCompatActivity() {
    private var etId: EditText? = null
    private var etJenisKamar: EditText? = null
    private var edTanggal: EditText? = null
    private var edDurasi: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pemesanan_kamar)

        queue = Volley.newRequestQueue(this)
        etId = findViewById(R.id.pk_id)
        etJenisKamar = findViewById(R.id.pk_jenisKamar)
        edTanggal = findViewById(R.id.pk_tanggal)
        edDurasi = findViewById(R.id.pk_durasi)
        layoutLoading = findViewById(R.id.layout_loading)

        val btnCancel = findViewById<Button>(R.id.btnCencel)
        btnCancel.setOnClickListener { finish() }
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val tvTitle = findViewById<TextView>(R.id.txtEditPemesananKamar)
        val id = intent.getIntExtra("id", -1)
        if (id == -1) {
            btnUpdate.setOnClickListener {

                tvTitle.setText("Tambah Pemesanan Kamar")

                createMahasiswa()
            }


        } else {
            tvTitle.setText("Edit Pemesanan Kamar")
            getMahasiswaById(id)

            btnUpdate.setOnClickListener { updateMahasiswa(id) }
        }

    }


    private fun getMahasiswaById(id: Int) {
        setLoading(true)
        val stringRequest: StringRequest =
            object : StringRequest(
                Method.GET,
                MemberHotelApi.GET_BY_ID_URL + id,
                Response.Listener { response ->

                    var memberJo = JSONObject(response.toString())
                    val member = memberJo.getJSONObject("data")

                    etJenisKamar!!.setText(member.getString("jenisKamar"))
                    edTanggal!!.setText(member.getString("tanggal"))
                    edDurasi!!.setText(member.getString("durasi"))

                    Toast.makeText(this@EditPemesananKamar, "Data berhasil diambil", Toast.LENGTH_SHORT)
                        .show()
                    setLoading(false)
                },
                Response.ErrorListener { error ->
                    setLoading(false)
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@EditPemesananKamar,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@EditPemesananKamar, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }
            }
        queue!!.add(stringRequest)
    }

    private fun createMahasiswa() {
        setLoading(true)

        if (etJenisKamar!!.text.toString().isEmpty()) {
            Toast.makeText(
                this@EditPemesananKamar,
                "Jenis Kamar tidak boleh kosong!",
                Toast.LENGTH_SHORT
            ).show()
        } else if (edTanggal!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditPemesananKamar, "Tanggal tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else if (edDurasi!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditPemesananKamar, "Durasi tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else {

            val kamar = PemesananKamarModel(
                0,
                etJenisKamar!!.text.toString(),
                edTanggal!!.text.toString(),
                edDurasi!!.text.toString()
            )
            val stringRequest: StringRequest =
                object :
                    StringRequest(Method.POST, PemesananKamarApi.ADD_URL, Response.Listener { response ->
                        val gson = Gson()
                        val mahasiswa = gson.fromJson(response, MemberHotel::class.java)

                        if (mahasiswa != null)
                            Toast.makeText(
                                this@EditPemesananKamar,
                                "Data Berhasil Ditambahkan",
                                Toast.LENGTH_SHORT
                            ).show()

                        val returnIntent = Intent()
                        setResult(RESULT_OK, returnIntent)
                        finish()

                        setLoading(false)
                    }, Response.ErrorListener { error ->
                        setLoading(false)
                        try {
                            val responseBody =
                                String(error.networkResponse.data, StandardCharsets.UTF_8)
                            val errors = JSONObject(responseBody)
                            Toast.makeText(
                                this@EditPemesananKamar,
                                errors.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@EditPemesananKamar, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Accept"] = "application/json"
                        return headers

                    }

                    @Throws(AuthFailureError::class)
                    override fun getBody(): ByteArray {
                        val gson = Gson()
                        val requestBody = gson.toJson(kamar)
                        return requestBody.toByteArray(StandardCharsets.UTF_8)
                    }

                    override fun getBodyContentType(): String {
                        return "application/json"
                    }
                }
            queue!!.add(stringRequest)
        }
        setLoading(false)
    }


    private fun updateMahasiswa(id: Int) {
        setLoading(true)

        val member = PemesananKamarModel(
            id,
            etJenisKamar!!.text.toString(),
            edTanggal!!.text.toString(),
            edDurasi!!.text.toString()
        )

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.PUT,
                PemesananKamarApi.UPDATE_URL + id,
                Response.Listener { response ->
                    val gson = Gson()

                    val mahasiswa = gson.fromJson(response, PemesananKamarModel::class.java)

                    if (mahasiswa != null)
                        Toast.makeText(
                            this@EditPemesananKamar,
                            "Data Berhasil Diupdate",
                            Toast.LENGTH_SHORT
                        ).show()
                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()

                    setLoading(false)
                },
                Response.ErrorListener { error ->
                    setLoading(false)
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@EditPemesananKamar,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@EditPemesananKamar, e.message, Toast.LENGTH_SHORT).show()
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
                val requestBody = gson.toJson(member)
                return requestBody.toByteArray(StandardCharsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        queue!!.add(stringRequest)
    }



    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Throws(
        FileNotFoundException::class
    )

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }
}
