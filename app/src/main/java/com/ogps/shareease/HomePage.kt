package com.ogps.shareease

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ogps.shareease.fragments.Profile
import com.ogps.shareease.fragments.Publish
import com.ogps.shareease.fragments.Search

class HomePage : AppCompatActivity() {

    private val profile = Profile()
    private val publish = Publish()
    private val search = Search()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        replaceFragment(search)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.btmnav)

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ic_profile -> replaceFragment(profile)
                R.id.ic_search -> replaceFragment(search)
                R.id.ic_publish -> replaceFragment(publish)
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container,fragment)
        transaction.commit()
    }

}