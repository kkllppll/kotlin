package com.example.lw_4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lw_4.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ініціалізація вкладок
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        // Перший фрагмент за замовчуванням
        replaceFragment(Task1Fragment())

        // Обробка кліків по вкладках
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> replaceFragment(Task1Fragment())
                    1 -> replaceFragment(Task2Fragment())
                    2 -> replaceFragment(Task3Fragment())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    // Функція для заміни фрагментів
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
