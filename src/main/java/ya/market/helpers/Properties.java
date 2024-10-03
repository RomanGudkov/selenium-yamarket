package ya.market.helpers;

import org.aeonbits.owner.ConfigFactory;
import ya.market.helpers.interfase.properties.PagesProperties;
import ya.market.helpers.interfase.properties.TestsProperties;

public class Properties {

    /**
     * <p> переменная интерфейса к проперти значения </p>
     * <p> автор: Роман Гудков</p>
     */
    public static TestsProperties testsProperties = ConfigFactory.create(TestsProperties.class);

    /**
     * <p> переменная интерфейса к проперти значения </p>
     * <p> автор: Роман Гудков</p>
     */
    public static PagesProperties pagesProperties = ConfigFactory.create(PagesProperties.class);
}
