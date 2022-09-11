package com.example.ugd_3_kelompok

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /* getSupportActionBar()?.hide()
         var bottomNavigationView: BottomNavigationView = findViewById(R.id.nav_view)
         val data1 = HomeFragment()
         val data2 = DataFragment()

         setThatFragments(home)

         bottomNavigationView.setOnItemSelectedListener{
             when(it.itemId){
                 R.id.navigation_data1 -> {
                     setThatFragments(home)
                 }
                 R.id.navigation_data2 -> {
                     setThatFragments(data)
                 }
             }
             true
         }

         private fun setThatFragments(fragment : Fragment){
             supportFragmentManager.beginTransaction().apply {
                 replace(R.id.layoutFragment,fragment)
                 commit()
             }
         }*/

    }
}