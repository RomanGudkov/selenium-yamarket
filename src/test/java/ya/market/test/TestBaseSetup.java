package ya.market.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import static ya.market.helpers.Properties.testsProperties;

public class TestBaseSetup {

    /**
     * chrome драйвер
     */
    protected WebDriver chromeDriver;

    /**
     * <p> метод инициализирует и настраивает driver selenium web</p>
     * <p> автор: Роман Гудков</p>
     */
    @BeforeEach
    public void before() {
        System.setProperty(testsProperties.webdriver(), System.getenv(testsProperties.webdriverPath()));
        chromeDriver = new ChromeDriver();
        chromeDriver.manage().window().maximize();
        chromeDriver.manage().timeouts().pageLoadTimeout(testsProperties.defaultTimeout(), TimeUnit.SECONDS);
        chromeDriver.manage().timeouts().setScriptTimeout(testsProperties.defaultTimeout(), TimeUnit.SECONDS);
    }

    /**
     * <p> метод завершает работу driver selenium web</p>
     * <p> автор: Роман Гудков</p>
     */
    @AfterEach
    public void after() {
        chromeDriver.quit();
    }
}
