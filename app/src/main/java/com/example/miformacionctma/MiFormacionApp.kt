package com.example.miformacionctma

import android.app.Application
import com.example.miformacionctma.di.AppContainer

class MiFormacionApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}