package utils;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:webdriverconfig.properties"})
public interface WebDriverConfig extends Config {

    @Key("chromeDriverPath")
    String chromeDriverPath();

    @Key("yandexDriverPath")
    String yandexDriverPath();
}
