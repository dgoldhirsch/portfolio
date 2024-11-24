package com.cornmuffin.products.data.settings.datasources.cachesettings

import com.cornmuffin.products.data.room.AppDatabase
import com.cornmuffin.products.data.settings.Settings
import javax.inject.Inject

class SettingsCacheDataSource @Inject constructor(
    private val appDatabase: AppDatabase,
) {
    fun settings() = appDatabase.settings()

    fun replaceSettings(newSettings: Settings) {
        appDatabase.replaceSettings(newSettings)
    }
}
