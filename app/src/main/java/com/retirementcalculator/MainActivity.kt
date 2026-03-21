package com.retirementcalculator

import android.os.Bundle
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.retirementcalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateLangBadge()

        if (savedInstanceState == null) {
            loadFragment(CalculatorFragment.newInstance("compound_calculator.html"))
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_compound -> {
                    loadFragment(CalculatorFragment.newInstance("compound_calculator.html"))
                    true
                }
                R.id.nav_withdrawal -> {
                    loadFragment(CalculatorFragment.newInstance("withdrawal_calculator.html"))
                    true
                }
                else -> false
            }
        }

        binding.btnLanguage.setOnClickListener { anchor ->
            val popup = PopupMenu(this, anchor)
            LanguageManager.supported.forEachIndexed { i, lang ->
                popup.menu.add(0, i, i, lang.label)
            }
            popup.setOnMenuItemClickListener { item ->
                val lang = LanguageManager.supported[item.itemId].code
                LanguageManager.set(this, lang)
                updateLangBadge()
                activeFragment()?.updateLanguage(lang)
                true
            }
            popup.show()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun activeFragment(): CalculatorFragment? =
        supportFragmentManager.findFragmentById(R.id.fragment_container) as? CalculatorFragment

    private fun updateLangBadge() {
        val current = LanguageManager.get(this)
        binding.btnLanguage.text = LanguageManager.supported.find { it.code == current }?.badge ?: "KO"
    }
}
