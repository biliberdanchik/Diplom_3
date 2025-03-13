package page.objects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPage {

    private final WebDriver driver;

    public static final String URL_MAIN_PAGE = "https://stellarburgers.nomoreparties.site/";
    private static final By BUTTON_PERSONAL_ACCOUNT = By.xpath(".//*[text()='Личный Кабинет']");
    private static final By BUTTON_LOGIN_ACCOUNT = By.xpath(".//button[text()='Войти в аккаунт']");
    public static final By HEADLINE_ASSEMBLE_BURGER = By.xpath(".//*[text() = 'Соберите бургер']");
    public static final By BUTTON_PLACE_ORDER = By.xpath(".//button[text() = 'Оформить заказ']");
    public static final By BUNS = By.xpath(".//span[text()='Булки']/parent::div");
    public static final By SAUCES = By.xpath(".//span[text()='Соусы']");
    public static final By FILLING = By.xpath(".//span[text()='Начинки']");
    public static final By FILLING_ELEMENT = By.xpath(".//img[@alt='Мясо бессмертных моллюсков Protostomia']");
    public static final By BUNS_ELEMENT = By.xpath(".//img[@alt='Флюоресцентная булка R2-D3']");
    public static final By SAUCES_ELEMENT = By.xpath(".//img[@alt='Соус традиционный галактический']");

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Нажатие на кнопку 'Личный Кабинет'")
    public void clickPersonalAccount() {
        driver.findElement(BUTTON_PERSONAL_ACCOUNT).click();
    }

    @Step("Нажатие на кнопку 'Войти'")
    public void clickLoginAccount() {
        driver.findElement(BUTTON_LOGIN_ACCOUNT).click();
    }

    @Step("Ожидание появления заголовка 'Соберите бургер'")
    public void waitHeadlineAssembleBurger() {
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(HEADLINE_ASSEMBLE_BURGER));
    }

}
