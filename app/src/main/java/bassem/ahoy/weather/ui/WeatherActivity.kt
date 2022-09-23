package bassem.ahoy.weather.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import bassem.ahoy.weather.R

class WeatherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    }
}