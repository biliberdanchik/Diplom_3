package page.objects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PersonalAccountPage {

    private final WebDriver driver;

    public static final String URL_PERSONAL_ACCOUNT = "https://stellarburgers.nomoreparties.site/account/profile";
    public static final By PROFILE = By.xpath(".//*[text() = 'Профиль']");
    public static final By ORDER_HISTORY = By.xpath(".//*[text() = 'История заказов']");
    public static final By BUTTON_LOGOUT = By.xpath(".//button[text() = 'Выход']");
    private static final By TEXT_ABOUT_PAGE = By.xpath(".//*[text()='В этом разделе вы можете изменить свои персональные данные']");
    private static final By BUTTON_CONSTRUCTOR = By.xpath(".//p[text()='Конструктор']");
    private static final By LOGO = By.xpath(".//div[@class='AppHeader_header__logo__2D0X2']");


    public PersonalAccountPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Ожидание появления текста о разделе")
    public void waitTextAboutPage() {    //Ожидания появления текста компонента
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(TEXT_ABOUT_PAGE));
    }

    @Step("Нажатие на кнопку 'Выйти'")
    public void clickLogout() {
        driver.findElement(BUTTON_LOGOUT).click();
    }

    @Step("Нажатие на кнопку 'Конструктор'")
    public void clickConstructor() {
        driver.findElement(BUTTON_CONSTRUCTOR).click();
    }

    @Step("Нажатие на логотип Stellar Burgers")
    public void clickLogo() {
        driver.findElement(LOGO).click();
    }
}
