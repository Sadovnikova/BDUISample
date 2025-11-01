package com.example.bduisample

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.activityScenarioRule
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KTextView
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.platform.app.InstrumentationRegistry

@RunWith(AndroidJUnit4::class)
class TestLoginMockFlow_v1 : TestCase(
    kaspressoBuilder = Kaspresso.Builder.simple()
) {

    @get:Rule
    val rule = activityScenarioRule<MainActivity>()

    @Test
    fun testLoginMockFlow_v1() = run {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        step("Выбрать мок 'Форма логина' на главном экране") {
            KButton { withText("Форма логина") }.click()
        }

        step("Проверить наличие заголовка и заполнить поля") {
            KTextView { withText("Вход в аккаунт") }.isDisplayed()
            KEditText { withHint("Email") }.typeText("test@example.com")
            KEditText { withHint("Пароль") }.typeText("qwerty")
        }

        step("Проверить, что кнопка 'Войти' отображается, кликабельна и активна") {
            KButton { withText("Войти") }.apply {
                isDisplayed()
                isClickable()
                isEnabled()
            }
        }

        step("Нажать 'Войти'") {
            KButton { withText("Войти") }.click()
        }

        step("Вернемся назад по кнопке в тулбаре") {
            KButton { withContentDescription("Navigate up") }.click()
        }
    }
}


