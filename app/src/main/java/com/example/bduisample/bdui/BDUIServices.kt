package com.example.bduisample.bdui

import android.content.Context
import kotlinx.serialization.json.Json
import java.nio.charset.Charset

object BDUIServices {
    private val json = Json { ignoreUnknownKeys = true; classDiscriminator = "type" }

    fun loadFromAssets(context: Context, fileName: String): String =
        context.assets.open(fileName).use { input ->
            val data = input.readBytes().toString(Charset.forName("UTF-8"))
            data
        //    json.decodeFromString(BDUIPage.serializer(), data)
        }
}
