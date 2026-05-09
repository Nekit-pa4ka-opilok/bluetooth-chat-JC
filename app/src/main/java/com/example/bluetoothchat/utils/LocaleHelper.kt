package com.example.bluetoothchat.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.*

object LocaleHelper {

    fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
        } else {
            config.locale = locale
        }

        return context.createConfigurationContext(config)
    }

    // Для применения при старте приложения
    fun wrapContext(base: Context): Context {
        val prefs = base.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val language = prefs.getString("language", "ru") ?: "ru"

        return setLocale(base, language)
    }
}