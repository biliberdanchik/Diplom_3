package page.objects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage {

    private final WebDriver driver;

    public static final String URL_REGISTER_PAGE = "https://stellarburgers.nomoreparties.site/register";

    private static final By FIELD_NAME = By.xpath(".//label[text()='Имя']/parent::div/input");
    private static final By FIELD_EMAIL = By.xpath(".//label[text()='Email']/parent::div/input");
    private static final By FIELD_PASSWORD = By.xpath(".//input[@name='Пароль']");
    private static final By BUTTON_REGISTER = By.xpath(".//button[text()='Зарегистрироваться']");
    private static final By BUTTON_LOGIN = By.xpath(".//*[text()='Войти']");
    public static final By ERROR_INCORRECT_PASSWORD = By.xpath(".//p[text()='Некорректный пароль']");



    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Заполнение полей при регистрации и нажатие на кнопку 'Зарегистрироваться'")
    public void inputUserDataAndRegistration(String name, String email, String password) {
        driver.findElement(FIELD_NAME).sendKeys(name);
        driver.findElement(FIELD_EMAIL).sendKeys(email);
        driver.findElement(FIELD_PASSWORD).sendKeys(password);
        driver.findElement(BUTTON_REGISTER).click();
    }

    @Step("Нажатие на кнопку 'Войти'")
    public void clickLogin() {
        driver.findElement(BUTTON_LOGIN).click();
    }
}
