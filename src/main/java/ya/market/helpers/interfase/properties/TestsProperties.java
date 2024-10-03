package ya.market.helpers.interfase.properties;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"file:src/main/resources/test.properties"})
public interface TestsProperties extends Config {

    /**
     * <p> метод возвращает значение ожидания ответа страницы в секундах</p>
     * <p> автор: Роман Гудков</p>
     *
     * @return число
     */
    @Config.Key("default.timeout")
    int defaultTimeout();

    /**
     * <p> метод возвращает параметр используемого драйвера</p>
     * <p> автор: Роман Гудков</p>
     *
     * @return строка
     */
    @Config.Key("webdriver")
    String webdriver();

    /**
     * <p> метод возвращает параметр пути к драйверу</p>
     * <p> автор: Роман Гудков</p>
     *
     * @return строка
     */
    @Config.Key("webdriver.path")
    String webdriverPath();

    /**
     * <p> метод возвращает тестовый url</p>
     * <p> автор: Роман Гудков</p>
     *
     * @return строка
     */
    @Config.Key("ya.market")
    String urlTest();
}