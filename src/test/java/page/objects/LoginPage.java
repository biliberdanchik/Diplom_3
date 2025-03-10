package page.objects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;

    public static final String URL_LOGIN_PAGE = "https://stellarburgers.nomoreparties.site/login";
    public static final By HEADLINE_LOGIN = By.xpath(".//*[text()='Вход']");
    public static final By BUTTON_LOGIN = By.xpath(".//button[text()='Войти']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Ожидание появления заголовка 'Вход'")
    public void waitHeadlineLogin() {    //Ожидания появления текста компонента
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(HEADLINE_LOGIN));
    }
}
