package com.example.notepadmvvm.model.repository


import android.content.Context
import android.content.SharedPreferences
import com.example.notepadmvvm.R
import com.example.notepadmvvm.model.OnboardPage


class OnboardingRepository(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)

    fun isOnboardingShown(): Boolean {
        return sharedPreferences.getBoolean("onboarding_shown", true)
    }

    fun setOnboardingShown() {
        sharedPreferences.edit().putBoolean("onboarding_shown", false).apply()
    }

    fun getOnboardPages(): List<OnboardPage> {
        return listOf(
            OnboardPage(
                imageRes = R.mipmap.screen7,
                title = context.getString(R.string.intro1), // context üzerinden çağırılır.
                description = context.getString(R.string.intro1_1).uppercase()
            ),
            OnboardPage(
                imageRes = R.mipmap.screen1,
                title = context.getString(R.string.intro2),
                description = ""
            ),
            OnboardPage(
                imageRes = R.mipmap.screen2,
                title = context.getString(R.string.intro3),
                description = ""
            ),
            OnboardPage(
                imageRes = R.mipmap.screen3,
                title = context.getString(R.string.intro4),
                description = ""
            ),
            OnboardPage(
                imageRes = R.mipmap.screen4,
                title = context.getString(R.string.intro5),
                description = ""
            ),
            OnboardPage(
                imageRes = R.mipmap.screen6,
                title = context.getString(R.string.intro6),
                description = ""
            ),
            OnboardPage(
                imageRes = R.mipmap.screen8,
                title = context.getString(R.string.intro7),
                description = ""
            )
        )
    }
}
