package com.example.bduisample.bdui

sealed class Node {
    data class Screen(
        val title: String?,
        val background: String?,
        val root: ViewNode
    )

    sealed class ViewNode {
        data class Column(
            val padding: Int? = null,
            val spacing: Int? = null,
            val card: Boolean? = null,
            val children: List<ViewNode> = emptyList()
        ) : ViewNode()

        data class Row(
            val spacing: Int? = null,
            val card: Boolean? = null,
            val children: List<ViewNode> = emptyList()
        ) : ViewNode()

        data class Text(val text: String, val style: String? = null) : ViewNode()

        data class Image(
            val url: String,
            val widthDp: Int? = null,
            val heightDp: Int? = null,
            val contentDescription: String? = null,
            val shape: String? = null
        ) : ViewNode()

        data class Button(val text: String, val action: Action? = null) : ViewNode()

        data class TextField(val hint: String? = null, val inputType: String? = null) : ViewNode()

        // ⬇️ NEW: Spacer — для гибких отступов/распорок
        data class Spacer(
            val weight: Float? = null,          // если задан, займёт свободное место в Row/Column
            val widthDp: Int? = null,           // опциональная фиксированная ширина
            val heightDp: Int? = null           // опциональная фиксированная высота
        ) : ViewNode()

        data class ProductCard(
            val imageUrl: String,
            val title: String,
            val price: String,
            val buttonText: String = "Купить",
            val action: Action? = null
        ) : ViewNode()

        data class Action(val type: String, val message: String? = null, val asset: String? = null): ViewNode()

    }
}
