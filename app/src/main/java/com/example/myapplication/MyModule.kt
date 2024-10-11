package com.example.myapplication // Doğru paket ismini kullandığınızdan emin olun

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyModule {
    @Provides
    @Singleton
    fun provideMyDependency(): MainActivity.MyDependency {
        return MainActivity.MyDependency()
    }
}
