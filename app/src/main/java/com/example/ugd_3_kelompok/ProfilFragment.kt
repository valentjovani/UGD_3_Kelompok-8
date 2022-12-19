package com.example.ugd_3_kelompok

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd_3_kelompok.api.UserApi
import com.example.ugd_3_kelompok.databinding.FragmentProfilBinding
import com.example.ugd_3_kelompok.room.UserDB
import org.json.JSONObject
import java.nio.charset.StandardCharsets


class ProfilFragment : Fragment() {

    private var _binding  : FragmentProfilBinding?= null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        queue = Volley.newRequestQueue(activity)
        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()
        var id = sharedPreferences.getInt("id", 0)
        showUser(id)

        binding.btnUpdate.setOnClickListener {
            transitionFragment(EditProfileFragment())
        }
        binding.btnLokasi.setOnClickListener {
            transitionFragment(LocationFragment())
        }
        binding.btnCamera.setOnClickListener {
            val moveCamera = Intent(this@ProfilFragment.requireContext(), CameraActivity::class.java)
            startActivity(moveCamera)
        }
    }

    private fun setProfil() {
        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()

        val db by lazy { UserDB(activity as HomeActivity) }
        val userDao = db.userDao()

        val user = userDao.getUser(sharedPreferences.getInt("id", 0))
        binding.viewUsername.setText(user.username)
        binding.viewEmail.setText(user.email)
        binding.viewNomorTelepon.setText(user.nomorTelepon)


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.fragment_profil, fragment)
            .addToBackStack(null).commit()
        transition.hide(ProfilFragment())
    }

    private fun showUser(id: Int) {

        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, UserApi.GET_BY_ID_URL + id, Response.Listener { response ->

                var userObject = JSONObject(response.toString())
                val userData = userObject.getJSONObject("data")

                binding.viewUsername.setText(userData.getString("username"))
                binding.viewEmail.setText(userData.getString("email"))
                binding.viewNomorTelepon.setText(userData.getString("nomorTelepon"))

                Toast.makeText(activity, "Data User berhasil diambil!", Toast.LENGTH_SHORT).show()

            }, Response.ErrorListener { error ->

                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        activity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }



}