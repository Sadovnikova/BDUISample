
package com.example.bduisample

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BDUIKaspressoTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.simple()
) {

    @get:Rule
    val rule = activityScenarioRule<MainActivity>()

    @Test
    fun testLoginMockFlow_showsToast() = run {
        step("Выбрать мок 'bdui_form_login.json' в диалоге") {
            KView { withText("bdui_form_login.json") }.click()
        }
        step("Проверить наличие заголовка и заполнить поля") {
            KTextView { withText("Вход в аккаунт") }.isDisplayed()
            KEditText { withHint("Email") }.typeText("test@example.com")
            KEditText { withHint("Пароль") }.typeText("qwerty")
        }
        step("Нажать 'Войти' и проверить тост") {
            KButton { withText("Войти") }.click()
            device.uiDevice.wait(Until.hasObject(By.text("Успешный вход (mock)")), 3_000)
            device.screenshots.take("toast_login_mock")
        }
    }
}
