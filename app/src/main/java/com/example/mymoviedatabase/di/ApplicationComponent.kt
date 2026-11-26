package com.example.mymoviedatabase.di

import android.app.Application
import com.example.mymoviedatabase.di.annotations.ApplicationScope
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class])
interface ApplicationComponent {

    fun getFragmentSubcomponent(): FragmentSubcomponent

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}