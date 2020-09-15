package vlados.dudos.vkino.app

import android.app.Application
import vlados.dudos.vkino.data.DataManager

class App : Application() {
    companion object {
        lateinit var dm: DataManager
    }

    override fun onCreate() {
        super.onCreate()

        dm = DataManager(baseContext)
    }
}