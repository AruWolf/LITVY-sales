package com.litvy.litvysales

import android.app.Application
import com.litvy.litvysales.di.AppContainer
import com.litvy.litvysales.domain.useCase.InitializeAppUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LitvySalesApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        container = AppContainer(this)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val useCase = InitializeAppUseCase(
                    userRepository = container.userRepository,
                    roleRepository = container.roleRepository,
                    cashRegisterRepository = container.cashRegisterRepository,
                    cashSessionRepository = container.cashSessionRepository
                )

                useCase()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
