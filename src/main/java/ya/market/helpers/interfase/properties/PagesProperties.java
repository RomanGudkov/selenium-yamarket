package ya.market.helpers.interfase.properties;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"file:src/main/resources/page.properties"})
public interface PagesProperties extends Config {

    /**
     * <p> метод возвращает значение количества повторений цикла</p>
     * <p> автор: Роман Гудков</p>
     * @return число
     */
    @Config.Key("loop.count")
    int loopCount();
}