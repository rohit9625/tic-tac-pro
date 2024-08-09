package com.devx.tictacpro

import android.app.Application
import com.devx.tictacpro.di.AppModule
import com.devx.tictacpro.di.AppModuleImpl

class TicTacProApp: Application() {
    companion object {
        lateinit var appModule : AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}