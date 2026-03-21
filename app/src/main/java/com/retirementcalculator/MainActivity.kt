package com.retirementcalculator

import android.content.Context
import android.os.Bundle
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.retirementcalculator.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this)
        binding.adView.loadAd(AdRequest.Builder().build())

        val savedLang = LanguageManager.get(this)
        updateLangBadge(savedLang)
        applyLocalizedLabels(savedLang)

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
                updateLangBadge(lang)
                applyLocalizedLabels(lang)
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

    private fun updateLangBadge(lang: String) {
        binding.btnLanguage.text = LanguageManager.supported.find { it.code == lang }?.badge ?: "KO"
    }

    /** 선택한 언어에 맞는 문자열 리소스로 Android UI 레이블을 업데이트 */
    private fun applyLocalizedLabels(lang: String) {
        val ctx = localizedContext(lang)
        binding.tvAppName.text = ctx.getString(R.string.app_name)
        binding.bottomNav.menu.findItem(R.id.nav_compound)?.title =
            ctx.getString(R.string.nav_compound)
        binding.bottomNav.menu.findItem(R.id.nav_withdrawal)?.title =
            ctx.getString(R.string.nav_withdrawal)
    }

    /** 지정한 언어 코드로 지역화된 Context 생성 */
    private fun localizedContext(lang: String): Context {
        val locale = Locale(lang)
        val config = resources.configuration
        config.setLocale(locale)
        return createConfigurationContext(config)
    }
}
