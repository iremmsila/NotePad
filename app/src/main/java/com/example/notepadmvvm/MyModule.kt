package com.example.notepadmvvm

import androidx.test.espresso.core.internal.deps.dagger.Module
import androidx.test.espresso.core.internal.deps.dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton

@dagger.Module
@InstallIn(SingletonComponent::class)
object MyModule {
    @dagger.Provides
    @Singleton
    fun provideMyDependency(): MainActivity.MyDependency {
        return MainActivity.MyDependency()
    }
}
