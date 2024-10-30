package com.example.myapplication.ui.theme // Doğru paket ismini kullandığınızdan emin olun

import com.example.myapplication.db.MainActivity
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
