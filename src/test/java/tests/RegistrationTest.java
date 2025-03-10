package tests;

import client.StellarBurgersServiceClient;
import com.github.javafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
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
    private StellarBurgersServiceClient client;

    private String name;
    private String email;
    private String password;


    @Before
    public void prepareData(){
        driver = new ChromeDriver();
        faker = new Faker();
    }

    @Test
    public void checkingSuccessfulRegistration() {
        driver.get(URL_REGISTER_PAGE);
        RegisterPage registerPage = new RegisterPage(driver);

        //Генерация данных пользователя
        name = faker.name().firstName();
        email = faker.internet().emailAddress();
        password = faker.bothify("#?#?#?");
        //Ввод данных пользователя и регистрация
        registerPage.userDataEntryAndRegistration(name,email,password);

        //Ожидание загрузки страницы авторизации
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitHeadlineLogin();

        //Проверяем, что загружена страница авторизации и доступна кнопка Войти
        String pageAfterSuccessfulRegistration = driver.getCurrentUrl();
        assertEquals("Страница с авторизацией не отображается", URL_LOGIN_PAGE, pageAfterSuccessfulRegistration);
        WebElement buttonLogin = driver.findElement(BUTTON_LOGIN);
        boolean isEnableButtonLogin = buttonLogin.isEnabled();
        assertTrue("Кнопка 'Войти недоступна'", isEnableButtonLogin);

    }

    @Test
    public void checkingRegistrationWithIncorrectPassword() {
        driver.get(URL_REGISTER_PAGE);
        RegisterPage registerPage = new RegisterPage(driver);

        //Генерация данных пользователя
        name = faker.name().firstName();
        email = faker.internet().emailAddress();
        password = faker.bothify("#?#?#");

        //Ввод данных пользователя и регистрация
        registerPage.userDataEntryAndRegistration(name,email,password);

        //Проверяем, что отображается сообщение о некорректном пароде
        WebElement incorrectPassword = driver.findElement(ERROR_INCORRECT_PASSWORD);
        boolean isError = incorrectPassword.isDisplayed();
        assertTrue("Сообщение о некорректности пароля отсутствует", isError);
    }

    @After
    public void cleanupData() {
        driver.quit();
        client = new StellarBurgersServiceClient();
        String accessToken = client.getAccessToken(email, password);
        if (accessToken != null) {
            client.deleteUser(accessToken);
        }
    }
}
