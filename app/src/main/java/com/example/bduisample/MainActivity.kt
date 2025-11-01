package com.example.bduisample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bduisample.bdui.BDUIParser
import com.example.bduisample.bdui.BDUIRenderer
import com.example.bduisample.bdui.BDUIServices
import com.example.bduisample.bdui.Node
import com.example.bduisample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var renderer: BDUIRenderer

    private val backStack = ArrayDeque<String>()
    private var currentAsset: String? = null

    private val mocks = listOf(
        "bdui_home.json",
        "bdui_shop.json",
        "bdui_form_login.json",
        "bdui_profile.json",
        "bdui_news.json"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Sample BDUI App"

        renderer = BDUIRenderer(this) { action ->
            when (action.type) {
                "toast" -> {
                    Toast.makeText(this, action.message.orEmpty(), Toast.LENGTH_LONG).show()
                }

                "open_mock" -> {
                    action.asset?.let { renderPage(it, push = true) }
                }

                "pick_mock" -> {
                    pickMockAndRender()
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backStack.isNotEmpty()) {
                    val prev = backStack.removeLast()
                    renderPage(prev, push = false)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        // 3) Кнопка «стрелка» в тулбаре — СНАРУЖИ
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        renderPage("bdui_main_menu.json", push = false)
    }

    /** Загружает экран из assets/bdui/<assetName> и показывает его */
    private fun renderPage(assetName: String, push: Boolean = true) {
        if (push) currentAsset?.let { backStack.addLast(it) }
        currentAsset = assetName

        val json = assets.open("bdui/$assetName")
            .bufferedReader(Charsets.UTF_8)
            .use { it.readText() }

        val screen = BDUIParser.parse(json)
        showScreen(screen)
        updateUpButton()
    }

    /** Если используешь сервис загрузки моков */
    private fun loadAndRender(fileName: String) {
        // Если BDUIServices.loadFromAssets возвращает JSON-строку:
        val json = BDUIServices.loadFromAssets(this, fileName)
        val screen = BDUIParser.parse(json)
        showScreen(screen)
        updateUpButton()
    }

    private fun showScreen(screen: Node.Screen) {
        title = screen.title ?: "BDUI Sample"
        val view = renderer.render(screen)
        binding.bduiContainer.removeAllViews()
        binding.bduiContainer.addView(view)
    }

    private fun pickMockAndRender() {
        val items = mocks.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Выберите BDUI-мок")
            .setItems(items) { _, which ->
                renderPage(items[which], push = true)
            }
            .setCancelable(true)
            .show()
    }

    private fun updateUpButton() {
        val hasBack = backStack.isNotEmpty()
        supportActionBar?.setDisplayHomeAsUpEnabled(hasBack)
        supportActionBar?.setHomeButtonEnabled(hasBack)
    }
}
