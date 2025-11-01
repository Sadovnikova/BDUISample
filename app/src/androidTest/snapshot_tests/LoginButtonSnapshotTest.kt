import android.view.ViewGroup
import android.widget.Button
import android.graphics.Color
import android.widget.FrameLayout
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

class LoginButtonSnapshotTest {

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun primaryButton_default() {
        val context = paparazzi.context

        val container = FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(24, 24, 24, 24)
        }

        val button = Button(context).apply {
            text = "Войти"
            setBackgroundColor(Color.parseColor("#007AFF"))
            setTextColor(Color.WHITE)
        }

        container.addView(button)
        paparazzi.snapshot(container)
    }
}