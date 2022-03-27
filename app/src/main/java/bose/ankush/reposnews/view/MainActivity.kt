package bose.ankush.reposnews.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bose.ankush.reposnews.databinding.ActivityMainBinding
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var splitInstallManager: SplitInstallManager
    private lateinit var request: SplitInstallRequest
    private val DYNAMIC_FEATURE = "weather_feature"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = this@MainActivity
        }
        setContentView(binding.root)

    }
}