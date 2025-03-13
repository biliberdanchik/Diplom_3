package tests;

import client.StellarBurgersServiceClient;
import com.github.javafaker.Faker;
import utils.WebDriverFactory;
import enums.MethodsNavigatingToConstructor;
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
import page.objects.PersonalAccountPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static page.objects.LoginPage.URL_LOGIN_PAGE;
import static page.objects.MainPage.*;

@RunWith(Parameterized.class)
public class PersonalAccountTransitionConstructorParamTest {
    private WebDriver driver;
    private StellarBurgersServiceClient client;

    private String email;
    private String password;
    private String name;
    private String accessToken;

    private final MethodsNavigatingToConstructor method;

    public PersonalAccountTransitionConstructorParamTest(MethodsNavigatingToConstructor method) {
        this.method = method;
    }

    @Parameterized.Parameters
    public static Object[] getMethod() {
        return new Object[] {
                MethodsNavigatingToConstructor.BUTTON_CONSTRUCTOR,
                MethodsNavigatingToConstructor.LOGO
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
    @DisplayName("Проверка перехода к конструктору")
    public void checkingTransitionToConstructor() {
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
        //Выбор метода перехода к конструктору
        transitionToConstructor(method);
        //Ожидание загрузки страницы
        mainPage.waitHeadlineAssembleBurger();
        //Проверяем, что загружена главная страница и доступны кнопки
        String urlAfterTransitionToConstructor = driver.getCurrentUrl();
        assertEquals("Главная страница не отображается", URL_MAIN_PAGE, urlAfterTransitionToConstructor);
        WebElement buttonPlaceOrder = driver.findElement(BUTTON_PLACE_ORDER);
        WebElement buns = driver.findElement(BUNS);
        WebElement sauces = driver.findElement(SAUCES);
        WebElement filling = driver.findElement(FILLING);
        boolean isElementsDisplayed = buttonPlaceOrder.isEnabled() & buns.isDisplayed() & sauces.isDisplayed() & filling.isDisplayed();
        assertTrue("Элементы конструктора не отображаются", isElementsDisplayed);
    }

    @After
    public void cleanupData() {
        driver.quit();
        if (accessToken != null) {
            client.deleteUser(accessToken);
        }
    }

    @Step("Переход к конструктору")
    public void transitionToConstructor(MethodsNavigatingToConstructor method) {
        PersonalAccountPage personalAccountPage;
        switch (method) {
            case BUTTON_CONSTRUCTOR:
                personalAccountPage = new PersonalAccountPage(driver);
                personalAccountPage.clickConstructor();
                break;
            case LOGO:
                personalAccountPage = new PersonalAccountPage(driver);
                personalAccountPage.clickLogo();
                break;
        }
    }
}
