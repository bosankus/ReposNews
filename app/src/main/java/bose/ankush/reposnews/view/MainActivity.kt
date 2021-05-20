package bose.ankush.reposnews.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bose.ankush.reposnews.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = this@MainActivity
        }
        setContentView(binding.root)
    }
}