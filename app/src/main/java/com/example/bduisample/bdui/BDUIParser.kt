package com.example.bduisample.bdui

import com.google.gson.JsonObject
import com.google.gson.JsonParser

object BDUIParser {
    fun parse(json: String): Node.Screen {
        val root = JsonParser.parseString(json).asJsonObject
        val title = root.get("title")?.asString
        val background = root.get("background")?.asString
        val rootNode = BDUIParser.parseView(root.getAsJsonObject("root"))
        return Node.Screen(title, background, rootNode)
    }

    private fun parseView(obj: JsonObject): Node.ViewNode {
        return when (val type = obj.get("type").asString) {
            "column" -> Node.ViewNode.Column(
                padding = obj.get("padding")?.asInt,
                spacing = obj.get("spacing")?.asInt,
                card = obj.get("card")?.asBoolean,
                children = obj.getAsJsonArray("children")?.map { BDUIParser.parseView(it.asJsonObject) } ?: emptyList()
            )
            "row" -> Node.ViewNode.Row(
                spacing = obj.get("spacing")?.asInt,
                card = obj.get("card")?.asBoolean,
                children = obj.getAsJsonArray("children")?.map { BDUIParser.parseView(it.asJsonObject) } ?: emptyList()
            )
            "text" -> Node.ViewNode.Text(
                text = obj.get("text").asString,
                style = obj.get("style")?.asString
            )
            "image" -> Node.ViewNode.Image(
                url = obj.get("url").asString,
                widthDp = obj.get("widthDp")?.asInt,
                heightDp = obj.get("heightDp")?.asInt,
                contentDescription = obj.get("contentDescription")?.asString,
                shape = obj.get("shape")?.asString
            )
            "button" -> Node.ViewNode.Button(
                text = obj.get("text").asString,
                action = obj.get("action")?.asJsonObject?.let { a ->
                    Node.ViewNode.Action(
                        type = a.get("type").asString,
                        message = a.get("message")?.asString,
                        asset = a.get("asset")?.asString
                    )
                }
            )
            "textField" -> Node.ViewNode.TextField(
                hint = obj.get("hint")?.asString,
                inputType = obj.get("inputType")?.asString
            )

            "productCard" -> Node.ViewNode.ProductCard(
                imageUrl = obj.get("imageUrl").asString,
                title = obj.get("title").asString,
                price = obj.get("price").asString,
                buttonText = obj.get("buttonText")?.asString ?: "Купить",
                action = obj.get("action")?.asJsonObject?.let { a ->
                    Node.ViewNode.Action(
                        type = a.get("type").asString,
                        message = a.get("message")?.asString,
                        asset = a.get("asset")?.asString
                    )
                }
            )

            "spacer" -> Node.ViewNode.Spacer(
                weight = obj.get("weight")?.asFloat,
                widthDp = obj.get("widthDp")?.asInt,
                heightDp = obj.get("heightDp")?.asInt
            )

            else -> error("Unknown type: $type")
        }
    }
}
