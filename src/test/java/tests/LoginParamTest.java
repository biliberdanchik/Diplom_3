package tests;

import client.StellarBurgersServiceClient;
import com.github.javafaker.Faker;
import utils.WebDriverFactory;
import enums.EntryPointsForAuthorization;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import page.objects.LoginPage;
import page.objects.MainPage;
import page.objects.PasswordResetPage;
import page.objects.RegisterPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static page.objects.MainPage.*;
import static page.objects.PasswordResetPage.URL_PASSWORD_RESET;
import static page.objects.RegisterPage.URL_REGISTER_PAGE;

@RunWith(Parameterized.class)
public class LoginParamTest {

    private WebDriver driver;
    private StellarBurgersServiceClient client;

    private String email;
    private String password;
    private String name;
    private String accessToken;

    private final EntryPointsForAuthorization point;

    public LoginParamTest(EntryPointsForAuthorization point) {
        this.point = point;
    }

    @Parameterized.Parameters
    public static Object[] getPoint() {
        return new Object[] {
                EntryPointsForAuthorization.PERSONAL_ACCOUNT,
                EntryPointsForAuthorization.LOGIN_ACCOUNT,
                EntryPointsForAuthorization.PASSWORD_RESET,
                EntryPointsForAuthorization.REGISTRATION_FORM
        };
    }

    @Before
    public void prepareData(){
        driver = WebDriverFactory.createWebDriver();
        Faker faker = new Faker();
        //Генерация данных пользователя, создание и получение токена
        name = faker.name().firstName();
        email = faker.internet().emailAddress();
        password = faker.bothify("#?#?#?");
        client = new StellarBurgersServiceClient();
        accessToken = client.createUserAndGetToken(name, email, password);
    }

    @Test
    @DisplayName("Проверка авторизации пользователя")
    public void checkingAuthorizationFromDifferentPointSuccessful() {
        //Определение точки входа и переход к авторизации
        transitionToAuthorization(point);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.inputUserDataAndLogin(email, password);
        //Ожидание загрузки основной страницы
        MainPage mainPage = new MainPage(driver);
        mainPage.waitHeadlineAssembleBurger();
        //Проверяем, что загружена главная страница и доступна кнопка Оформить заказ
        String urlAfterSuccessfulLogin = driver.getCurrentUrl();
        assertEquals("Главная страница не отображается", URL_MAIN_PAGE, urlAfterSuccessfulLogin);
        WebElement buttonPlaceOrder = driver.findElement(BUTTON_PLACE_ORDER);
        boolean isEnableButtonPlaceOrder = buttonPlaceOrder.isEnabled();
        assertTrue("Кнопка 'Оформить заказ недоступна'", isEnableButtonPlaceOrder);
    }

    @After
    public void cleanupData() {
        driver.quit();
        if (accessToken != null) {
            client.deleteUser(accessToken);
        }
    }

    @Step("Переход к авторизации")
    public void transitionToAuthorization(EntryPointsForAuthorization point) {
        MainPage mainPage;
        switch (point) {
            case PERSONAL_ACCOUNT:
                System.out.println("Вход через кнопку «Личный кабинет»");
                driver.get(URL_MAIN_PAGE);
                mainPage = new MainPage(driver);
                mainPage.clickPersonalAccount();
                break;
            case LOGIN_ACCOUNT:
                System.out.println("Вход по кнопке «Войти в аккаунт» на главной");
                driver.get(URL_MAIN_PAGE);
                mainPage = new MainPage(driver);
                mainPage.clickLoginAccount();
                break;
            case REGISTRATION_FORM:
                System.out.println("Вход через кнопку в форме регистрации");
                driver.get(URL_REGISTER_PAGE);
                RegisterPage registerPage = new RegisterPage(driver);
                registerPage.clickLogin();
                break;
            case PASSWORD_RESET:
                System.out.println("Вход через кнопку в форме восстановления пароля");
                driver.get(URL_PASSWORD_RESET);
                PasswordResetPage passwordResetPage = new PasswordResetPage(driver);
                passwordResetPage.clickLogin();
                break;
        }
    }
}
