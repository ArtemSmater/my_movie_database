package com.example.mymoviedatabase

import android.app.Application
import com.example.mymoviedatabase.di.DaggerApplicationComponent

class App : Application() {

    val component = DaggerApplicationComponent.factory().create(this)
}