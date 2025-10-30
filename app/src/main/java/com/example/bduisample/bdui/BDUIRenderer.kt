package com.example.bduisample.bdui

import android.content.Context
import android.graphics.Color
import android.graphics.Outline
import android.text.InputType
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import coil.load
import com.google.android.material.R
import com.example.bduisample.R as bduiR
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class BDUIRenderer(
    private val context: Context,
    private val onAction: (Node.ViewNode.Action) -> Unit = {}
) {
    fun render(screen: Node.Screen): View {
        val root = renderView(screen.root)
        screen.background?.let { runCatching { root.setBackgroundColor(Color.parseColor(it)) } }
        return root
    }

    private fun renderView(node: Node.ViewNode): View = when (node) {
        is Node.ViewNode.Column -> vertical(node)
        is Node.ViewNode.Row -> horizontal(node)
        is Node.ViewNode.Text -> text(node)
        is Node.ViewNode.Image -> image(node)
        is Node.ViewNode.Button -> button(node)
        is Node.ViewNode.TextField -> textField(node)
        is Node.ViewNode.Action -> TODO()
        is Node.ViewNode.ProductCard -> productCard(node)
        is Node.ViewNode.Spacer -> spacer(node)
    }

    private fun productCard(node: Node.ViewNode.ProductCard): View {
        // Внешняя карточка с тенью и скруглением
        val card = androidx.cardview.widget.CardView(context).apply {
            radius = dpF(16f)
            cardElevation = dpF(8f)
            setCardBackgroundColor(Color.WHITE)
            useCompatPadding = true
            preventCornerOverlap = true
            val p = dp(12); setContentPadding(p, p, p, p)
            layoutParams = LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
            ).apply { setMargins(0, 0, 0, 0) }
        }

        // Контент карточки
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }

        // Картинка со скруглением
        val img = ImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(120)
            )
            load(node.imageUrl)
            clipToOutline = true
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(0, 0, view.width, view.height, dpF(12f))
                }
            }
        }

        // Заголовок
        val title = TextView(context).apply {
            text = node.title
            textSize = 16f
            setTextColor(Color.parseColor("#0D2B4D")) // тёмно-синий
            setPadding(0, dp(10), 0, dp(6))
        }

        // Чип цены
        val price = TextView(context).apply {
            text = node.price
            textSize = 14f
            setPadding(dp(12), dp(8), dp(12), dp(8))
            setTextColor(Color.parseColor("#0D2B4D"))
            background = AppCompatResources.getDrawable(context, bduiR.drawable.bg_price_chip)
        }

        // Кнопка
        val btn = com.google.android.material.button.MaterialButton(
            ContextThemeWrapper(context, com.google.android.material.R.style.Widget_Material3_Button)
        ).apply {
            text = node.buttonText
            setTextColor(Color.WHITE)
            background = AppCompatResources.getDrawable(context, bduiR.drawable.bg_btn_pink) // розовый
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = dp(10) }
            setOnClickListener {
                it.animate().scaleX(0.98f).scaleY(0.98f).setDuration(80).withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
                }.start()
                node.action?.let(onAction)
            }
        }

        container.addView(img)
        container.addView(title)
        container.addView(price)
        container.addView(btn)
        card.addView(container)
        return card
    }

    /*
    private fun productCard(node: Node.ViewNode.ProductCard): View {

        //ВОТ СЮДА ВОТКНУТЬ КОД ИЗ ДЖЕПЕТЕ КОТОРЫЙ СВЕРСТАЕТ КАРТОЧКИ

        Toast.makeText(context, "Надо чтобы джепете нашкодил витрину", Toast.LENGTH_SHORT).show() /// УДАЛИТЬ СТРОЧКУ
        return View(context) // УДАЛИТЬ
    }

    */

    private fun vertical(node: Node.ViewNode.Column): View {
        val ll = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            node.padding?.let { setPadding(dp(it), dp(it), dp(it), dp(it)) }
        }
        node.children.forEachIndexed { i, child ->
            if (child is Node.ViewNode.Image) {
                ll.addView(renderView(child))
            } else {
                val view = wrapCardIfNeeded(renderView(child), node.card)
                ll.addView(view)
            }
            if (node.spacing != null && i != node.children.lastIndex) addSpace(ll, node.spacing, true)
        }
        return ll
    }

    private fun horizontal(node: Node.ViewNode.Row): View {
        val ll = LinearLayout(context).apply { orientation = LinearLayout.HORIZONTAL }
        node.children.forEachIndexed { i, child ->
            val view = wrapCardIfNeeded(renderView(child), node.card)
            view.layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            ll.addView(view)
            if (node.spacing != null && i != node.children.lastIndex) addSpace(ll, node.spacing, false)
        }
        return ll
    }

    private fun text(node: Node.ViewNode.Text) = TextView(context).apply {
        text = node.text
        when (node.style) {
            "title" -> { textSize = 22f; setTextColor(Color.parseColor("#0D2B4D")) }
            "subtitle" -> { textSize = 18f; setTextColor(Color.parseColor("#0D2B4D")) }
            else -> textSize = 16f
        }
    }

    private fun image(node: Node.ViewNode.Image): View {
        val imageView = ImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            adjustViewBounds = true
            clipToOutline = true
        }

        // Задаём временные layoutParams — пока не узнаем реальный размер
        imageView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Загружаем изображение с Coil и слушаем результат
        imageView.load(node.url) {
            listener(
                onSuccess = { _, result ->
//                    val bmp = (result.drawable as? BitmapDrawable)?.bitmap
//                    if (bmp != null) {
//                        val width = bmp.width
//                        val height = bmp.height
                        // Обновляем layoutParams на реальные размеры
//                        imageView.post {
//                            val params = imageView.layoutParams
//                            params.width = node.widthDp ?: 0
//                            params.height = node.heightDp ?: 0
//                            imageView.layoutParams = params
//                        }
//                    }
                },
                onError = { _, _ ->
                    // fallback — если не удалось загрузить
                    imageView.layoutParams = LinearLayout.LayoutParams(
                        node.widthDp?.let { dp(it) } ?: ViewGroup.LayoutParams.MATCH_PARENT,
                        node.heightDp?.let { dp(it) } ?: dp(160)
                    )

                    imageView.setImageResource(bduiR.drawable.avatar)
                }
            )
        }

        // Добавляем скругление углов
        imageView.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, dpF(999f))
            }
        }

        return imageView
    }


    private fun button(node: Node.ViewNode.Button) = MaterialButton(
        ContextThemeWrapper(context, R.style.Widget_Material3_Button)
    ).apply {
        text = node.text
        setTextColor(Color.WHITE)
        background = AppCompatResources.getDrawable(context, com.example.bduisample.R.drawable.bg_btn_pink)
        setOnClickListener {
            it.animate().scaleX(0.98f).scaleY(0.98f).setDuration(80).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
            }.start()
            node.action?.let(onAction)
        }
    }

    private fun textField(node: Node.ViewNode.TextField): View {
        val til = TextInputLayout(context)
        val edit = TextInputEditText(til.context).apply {
            hint = node.hint
            if (node.inputType == "password") {
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
        til.addView(edit)
        return til
    }

    private fun wrapCardIfNeeded(child: View, isCard: Boolean?): View {
        if (isCard == true) {
            val card = CardView(context).apply {
                radius = dpF(16f)
                cardElevation = dpF(6f)
                setCardBackgroundColor(Color.WHITE)
                useCompatPadding = true
                preventCornerOverlap = true
                val p = dp(12); setContentPadding(p, p, p, p)
            }
            card.addView(child, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            return card
        }
        return child
    }

    private fun addSpace(parent: LinearLayout, dpVal: Int, vertical: Boolean) {
        val s = Space(context)
        val lp = if (vertical)
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(dpVal))
        else
            ViewGroup.LayoutParams(dp(dpVal), ViewGroup.LayoutParams.MATCH_PARENT)
        parent.addView(s, lp)
    }

    private fun spacer(node: Node.ViewNode.Spacer): View {
        val view = View(context)

        val params = LinearLayout.LayoutParams(
            if (node.widthDp != null) dp(node.widthDp) else 0,
            if (node.heightDp != null) dp(node.heightDp) else 0
        )

        // если задан weight — занимаем свободное пространство
        if (node.weight != null) {
            params.width = 0
            params.height = 0
            params.weight = node.weight
        }

        view.layoutParams = params

        return view


    }

    private fun dp(v: Int) = (v * context.resources.displayMetrics.density).toInt()
    private fun dpF(v: Float) = v * context.resources.displayMetrics.density
}
