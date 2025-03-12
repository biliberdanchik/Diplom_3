package page.objects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PasswordResetPage {

    private final WebDriver driver;

    public static final String URL_PASSWORD_RESET = "https://stellarburgers.nomoreparties.site/forgot-password";
    private static final By BUTTON_LOGIN = By.xpath(".//*[text()='Войти']");

    public PasswordResetPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Нажатие на кнопку 'Войти'")
    public void clickLogin() {
        driver.findElement(BUTTON_LOGIN).click();
    }
}
