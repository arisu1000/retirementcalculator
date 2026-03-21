package com.retirementcalculator

import android.content.Context

object LanguageManager {
    private const val PREFS = "settings"
    private const val KEY_LANG = "language"

    val supported = listOf(
        Lang("ko", "한국어", "KO"),
        Lang("en", "English", "EN"),
        Lang("ja", "日本語", "JA"),
    )

    fun get(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_LANG, "ko") ?: "ko"

    fun set(context: Context, lang: String) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putString(KEY_LANG, lang).apply()

    data class Lang(val code: String, val label: String, val badge: String)
}
