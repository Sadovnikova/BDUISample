package com.example.bduisample.bdui

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BDUIPage(
    val title: String? = null,
    val background: String? = null,
    val root: BDUINode
)

@Serializable
sealed class BDUINode {
    @Serializable @SerialName("column")
    data class Column(
        val padding: Int? = 16,
        val spacing: Int? = 8,
        val children: List<BDUINode>
    ) : Node()

    @Serializable @SerialName("row")
    data class Row(
        val spacing: Int? = 8,
        val children: List<BDUINode>
    ) : Node()

    @Serializable @SerialName("text")
    data class Text(
        val text: String,
        val style: TextStyle? = null
    ) : Node()

    @Serializable @SerialName("image")
    data class Image(
        val url: String? = null,
        val asset: String? = null,
        val widthDp: Int? = null,
        val heightDp: Int? = null,
        val contentDescription: String? = null
    ) : Node()

    @Serializable @SerialName("spacer")
    data class Spacer(val heightDp: Int) : Node()

    @Serializable @SerialName("input")
    data class Input(
        val key: String,
        val hint: String? = null
    ) : Node()

    @Serializable @SerialName("button")
    data class Button(
        val text: String,
        val action: Action
    ) : Node()
}

@Serializable
sealed class Action {
    @Serializable @SerialName("toast")
    data class Toast(val message: String) : Action()

    @Serializable @SerialName("open_url")
    data class OpenUrl(val url: String) : Action()
}

@Serializable
enum class TextStyle { title, subtitle, body, caption }
