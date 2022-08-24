package ru.gb.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.yandex.mapkit.MapKitFactory
import ru.gb.map.databinding.ActivityMainBinding
import ru.gb.map.ui.map.MapFragment
import ru.gb.map.ui.markers.MarkersFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey(MAPKIT_API_KEY)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            replace<MapFragment>(R.id.map_fragment)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
        supportFragmentManager.commit {
            replace<MarkersFragment>(R.id.markers_fragment)
            setReorderingAllowed(true)
            addToBackStack(null)
        }

    }

    companion object {
        const val MAPKIT_API_KEY = "9c50e3e6-c556-4d15-9f29-8726601261a4"
    }
}