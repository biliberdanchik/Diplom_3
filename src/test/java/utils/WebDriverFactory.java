package utils;

import enums.Browser;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverFactory {
    private static final String BROWSER_PROPERTY = "browser";
    private static final String DEFAULT_BROWSER = "chrome";
    private static final String WEBDRIVER_CHROME_DRIVER_PROPERTIES = "webdriver.chrome.driver";

    public static WebDriver createWebDriver() {
        WebDriver driver = null;
        Browser browser = getActiveBrowser();
        WebDriverConfig webDriverConfig = ConfigFactory.create(WebDriverConfig.class);

        switch(browser) {
            case YANDEX: {
                System.setProperty(WEBDRIVER_CHROME_DRIVER_PROPERTIES, webDriverConfig.yandexDriverPath());
                driver = new ChromeDriver();
                break;
            }
            case CHROME: {
                System.setProperty(WEBDRIVER_CHROME_DRIVER_PROPERTIES, webDriverConfig.chromeDriverPath());
                driver = new ChromeDriver();
                break;
            }

            default: throw new IllegalArgumentException("Неподдерживаемый браузер " + browser);
        }
        return driver;
    }

    private static Browser getActiveBrowser() {
        String browserName = System.getProperty(BROWSER_PROPERTY, DEFAULT_BROWSER);
        return Browser.valueOf(browserName.toUpperCase());
    }
}
