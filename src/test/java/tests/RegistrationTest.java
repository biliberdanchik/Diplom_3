package tests;

import client.StellarBurgersServiceClient;
import com.github.javafaker.Faker;
import driver.WebDriverFactory;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import page.objects.LoginPage;
import page.objects.RegisterPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static page.objects.LoginPage.BUTTON_LOGIN;
import static page.objects.LoginPage.URL_LOGIN_PAGE;
import static page.objects.RegisterPage.ERROR_INCORRECT_PASSWORD;
import static page.objects.RegisterPage.URL_REGISTER_PAGE;

public class RegistrationTest {

    private WebDriver driver;
    private Faker faker;

    private String name;
    private String email;
    private String password;

    @Before
    public void prepareData(){
        driver = WebDriverFactory.createWebDriver();
        faker = new Faker();
    }

    @Test
    @DisplayName("Проверка успешной регистрации пользователя")
    public void checkingSuccessfulRegistration() {
        driver.get(URL_REGISTER_PAGE);
        RegisterPage registerPage = new RegisterPage(driver);
        //Генерация данных пользователя
        name = faker.name().firstName();
        email = faker.internet().emailAddress();
        password = faker.bothify("#?#?#?");
        //Ввод данных пользователя и регистрация
        registerPage.inputUserDataAndRegistration(name,email,password);
        //Ожидание загрузки страницы авторизации
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitHeadlineLogin();
        //Проверяем, что загружена страница авторизации и доступна кнопка Войти
        String urlAfterSuccessfulRegistration = driver.getCurrentUrl();
        assertEquals("Страница с авторизацией не отображается", URL_LOGIN_PAGE, urlAfterSuccessfulRegistration);
        WebElement buttonLogin = driver.findElement(BUTTON_LOGIN);
        boolean isEnableButtonLogin = buttonLogin.isEnabled();
        assertTrue("Кнопка 'Войти недоступна'", isEnableButtonLogin);

    }

    @Test
    @DisplayName("Проверка регистрации пользователя с некорректным паролем")
    public void checkingRegistrationWithIncorrectPassword() {
        driver.get(URL_REGISTER_PAGE);
        RegisterPage registerPage = new RegisterPage(driver);
        //Генерация данных пользователя
        name = faker.name().firstName();
        email = faker.internet().emailAddress();
        password = faker.bothify("#?#?#");
        //Ввод данных пользователя и регистрация
        registerPage.inputUserDataAndRegistration(name,email,password);
        //Проверяем, что отображается сообщение о некорректном пароде
        WebElement incorrectPassword = driver.findElement(ERROR_INCORRECT_PASSWORD);
        boolean isError = incorrectPassword.isDisplayed();
        assertTrue("Сообщение о некорректности пароля отсутствует", isError);
    }

    @After
    public void cleanupData() {
        driver.quit();
        StellarBurgersServiceClient client = new StellarBurgersServiceClient();
        String accessToken = client.getAccessToken(email, password);
        if (accessToken != null) {
            client.deleteUser(accessToken);
        }
    }
}
