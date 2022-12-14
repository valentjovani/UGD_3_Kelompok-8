package com.example.ugd_3_kelompok

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ugd_3_kelompok.room.Constant
import com.example.ugd_3_kelompok.room.MemberHotel
import com.example.ugd_3_kelompok.room.MemberHotelDB
import kotlinx.android.synthetic.main.activity_edit_member_hotel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd_3_kelompok.api.MemberHotelApi
import com.example.ugd_3_kelompok.databinding.ActivityEditMemberHotelBinding
import com.example.ugd_3_kelompok.models.MemberHotelModel
import com.google.gson.Gson
import org.json.JSONObject
import java.io.FileNotFoundException
import java.nio.charset.StandardCharsets

class EditMemberHotel : AppCompatActivity() {
    private var etId: EditText? = null
    private var etFasilitas: EditText? = null
    private var etMembership: EditText? = null
    private var edTanggal: EditText? = null
    private var edDurasi: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null
    private val MEMBER_HOTEL_1 = "notif_member_hotel_1"
    private val notificationId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_member_hotel)

        queue = Volley.newRequestQueue(this)
        etId = findViewById(R.id.et_id)
        etFasilitas = findViewById(R.id.et_fasilitas)
        etMembership = findViewById(R.id.et_membership)
        edTanggal = findViewById(R.id.et_tanggal)
        edDurasi = findViewById(R.id.et_durasi)
        layoutLoading = findViewById(R.id.layout_loading)

        val btnCancel = findViewById<Button>(R.id.btnCencel)
        btnCancel.setOnClickListener { finish() }
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val tvTitle = findViewById<TextView>(R.id.txtEditMemberHotel)
        val id = intent.getIntExtra("id", -1)
        if (id == -1) {
            btnUpdate.setOnClickListener {

                tvTitle.setText("Tambah Member Hotel")

                createMahasiswa()
            }


        } else {
            tvTitle.setText("Edit Member Hotel")
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

                    etFasilitas!!.setText(member.getString("Fasilitas"))
                    etMembership!!.setText(member.getString("membership"))
                    edTanggal!!.setText(member.getString("tanggal"))
                    edDurasi!!.setText(member.getString("durasi"))

                    Toast.makeText(this@EditMemberHotel, "Data berhasil diambil", Toast.LENGTH_SHORT)
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
                            this@EditMemberHotel,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@EditMemberHotel, e.message, Toast.LENGTH_SHORT).show()
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

        if (etFasilitas!!.text.toString().isEmpty()) {
            Toast.makeText(
                this@EditMemberHotel,
                "Fasilitas tidak boleh kosong!",
                Toast.LENGTH_SHORT
            ).show()
        } else if (etMembership!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditMemberHotel, "Membership tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else if (edTanggal!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditMemberHotel, "Tanggal tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else if (edDurasi!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditMemberHotel, "Durasi tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else {

            val member = MemberHotelModel(
                0,
                etFasilitas!!.text.toString(),
                etMembership!!.text.toString(),
                edTanggal!!.text.toString(),
                edDurasi!!.text.toString()
            )
            val stringRequest: StringRequest =
                object :
                    StringRequest(Method.POST, MemberHotelApi.ADD_URL, Response.Listener { response ->
                        val gson = Gson()
                        val mahasiswa = gson.fromJson(response, MemberHotel::class.java)

                        if (mahasiswa != null)
                            Toast.makeText(
                                this@EditMemberHotel,
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
                                this@EditMemberHotel,
                                errors.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@EditMemberHotel, e.message, Toast.LENGTH_SHORT).show()
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
                        val requestBody = gson.toJson(member)
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

        val member = MemberHotelModel(
            id,
            etFasilitas!!.text.toString(),
            etMembership!!.text.toString(),
            edTanggal!!.text.toString(),
            edDurasi!!.text.toString()
        )

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.PUT,
                MemberHotelApi.UPDATE_URL + id,
                Response.Listener { response ->
                    val gson = Gson()

                    val mahasiswa = gson.fromJson(response, MemberHotelModel::class.java)

                    if (mahasiswa != null)
                        Toast.makeText(
                            this@EditMemberHotel,
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
                            this@EditMemberHotel,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@EditMemberHotel, e.message, Toast.LENGTH_SHORT).show()
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
