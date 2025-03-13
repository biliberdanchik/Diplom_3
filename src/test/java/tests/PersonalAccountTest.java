package tests;

import client.StellarBurgersServiceClient;
import com.github.javafaker.Faker;
import utils.WebDriverFactory;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import page.objects.LoginPage;
import page.objects.MainPage;
import page.objects.PersonalAccountPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static page.objects.LoginPage.BUTTON_LOGIN;
import static page.objects.LoginPage.URL_LOGIN_PAGE;
import static page.objects.PersonalAccountPage.*;

public class PersonalAccountTest {

    private WebDriver driver;
    private StellarBurgersServiceClient client;

    private String email;
    private String password;
    private String name;
    private String accessToken;

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
    @DisplayName("Проверка перехода по клику на кнопку 'Личный кабинет'")
    public void checkingTransitionToPersonalAccount() {
        //Авторизация
        driver.get(URL_LOGIN_PAGE);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.inputUserDataAndLogin(email, password);
        //Переход в Личный кабинет
        MainPage mainPage = new MainPage(driver);
        mainPage.clickPersonalAccount();
        //Ожидание загрузки профиля
        PersonalAccountPage personalAccountPage = new PersonalAccountPage(driver);
        personalAccountPage.waitTextAboutPage();
        //Проверка перехода на страницу профиля и отображения элементов профиля
        String urlAfterTransition = driver.getCurrentUrl();
        assertEquals("Страница профиля не отображается", URL_PERSONAL_ACCOUNT, urlAfterTransition);
        WebElement profile = driver.findElement(PROFILE);
        WebElement orderHistory = driver.findElement(ORDER_HISTORY);
        WebElement logout = driver.findElement(BUTTON_LOGOUT);
        boolean isElementsOnPage = profile.isDisplayed() && orderHistory.isDisplayed() && logout.isEnabled();
        assertTrue("Элементы отсутствуют на странице профиля", isElementsOnPage);
    }

    @Test
    @DisplayName("Проверка выхода по кнопке 'Выйти'")
    public void checkingLogout() {
        //Авторизация
        driver.get(URL_LOGIN_PAGE);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.inputUserDataAndLogin(email, password);
        //Переход в Личный кабинет
        MainPage mainPage = new MainPage(driver);
        mainPage.clickPersonalAccount();
        //Ожидание загрузки страницы профиля
        PersonalAccountPage personalAccountPage = new PersonalAccountPage(driver);
        personalAccountPage.waitTextAboutPage();
        //Нажимаем выход
        personalAccountPage.clickLogout();
        //Ожидаем загрузки страницы авторизации
        loginPage.waitHeadlineLogin();
        //Проверяем корректность открытой после перехода страницы
        String urlAfterLogout = driver.getCurrentUrl();
        assertEquals("Страница с авторизацией не отображается", URL_LOGIN_PAGE, urlAfterLogout);
        WebElement buttonLogin = driver.findElement(BUTTON_LOGIN);
        boolean isEnableButtonLogin = buttonLogin.isEnabled();
        assertTrue("Кнопка 'Войти недоступна'", isEnableButtonLogin);
    }

    @After
    public void cleanupData() {
        driver.quit();
        if (accessToken != null) {
            client.deleteUser(accessToken);
        }
    }
}
