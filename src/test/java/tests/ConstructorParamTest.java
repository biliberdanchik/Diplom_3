package tests;

import io.qameta.allure.junit4.DisplayName;
import utils.WebDriverFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static page.objects.MainPage.*;

@RunWith(Parameterized.class)
public class ConstructorParamTest {
    private WebDriver driver;

    private final By sectionFrom;
    private final By sectionTo;
    private final By sectionElement;
    private final boolean expectedResult;

    public ConstructorParamTest(By sectionFrom, By sectionTo, By sectionElement, boolean expectedResult) {
        this.sectionFrom = sectionFrom;
        this.sectionTo = sectionTo;
        this.sectionElement = sectionElement;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                {SAUCES, FILLING, FILLING_ELEMENT, true},
                {FILLING, BUNS, BUNS_ELEMENT, true},
                {FILLING, SAUCES, SAUCES_ELEMENT, true},
                {FILLING, BUNS, FILLING_ELEMENT, false},
                {FILLING, SAUCES, BUNS_ELEMENT, false},
                {SAUCES, FILLING, SAUCES_ELEMENT, false}
        };
    }

    @Before
    public void prepareData() {
        driver = WebDriverFactory.createWebDriver();
    }

    @Test
    @DisplayName("Проверка переходов между разделами конструктора")
    public void checkingTransitionToDifferentSection() throws InterruptedException {
        driver.get(URL_MAIN_PAGE);

        //Переходим к разделу, от которого будем проверять скролл
        driver.findElement(sectionFrom).click();
        //Переходим к проверяемому разделу
        driver.findElement(sectionTo).click();
        //Находим элемент для проверки
        WebElement element = driver.findElement(sectionElement);

        //Ай-я-яй, слип в тестах. Знаю, что слип в коде плохой тон, но тут не смог подобрать ожидание. Иначе негативные тесты работают некорректно.
        //Вычислил минимальный необходимый таймер для изменения координат проверяемого элемента.
        Thread.sleep(500);

        //Признак наличия элемента на странице по умолчанию false
        boolean isElementInViewport = false;

        //Проверяем нахождение элемента в видимой области. Если найден, присваиваем признаку true, иначе выбрасываем исключение.
        try { isElementInViewport = new WebDriverWait(driver, Duration.ofSeconds(1))
                        .until(driver -> {
                                        Rectangle rect = element.getRect();
                                        Dimension windowSize = driver.manage().window().getSize();
                                        System.out.printf("Данные элемента: X = %d, Y = %d, Width = %d, Height = %d%n",rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                                        System.out.printf("Размер страницы: Width = %d, Height = %d%n", windowSize.getWidth(), windowSize.getHeight());

                                        // условие, которое проверяем внутри явного ожидания
                                        return rect.getX() >= 0
                                                && rect.getY() >= 0
                                                && rect.getX() + rect.getWidth() <= windowSize.getWidth()
                                                && rect.getY() + rect.getHeight() <= windowSize.getHeight();
                                    });
                                } catch (TimeoutException exception) {
            System.out.println("Ожидание не выполнено: элемент не появился в видимой области за отведенное время.");
        }

        assertEquals("Расположение элемента не соответствует ожиданию", expectedResult, isElementInViewport);
    }

    @After
    public void cleanupData() {
        driver.quit();
    }
}
