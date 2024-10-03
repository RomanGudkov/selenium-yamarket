package ya.market.helpers;

import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class DataProvider {

    /**
     * <p> метод возврвщает тестовые параметры</p>
     * <p> автор: Роман Гудков</p>
     *
     * @return поток аргументов
     */
    public static Stream<Arguments> providerCheckingLaptopProduct() {
        List<String> brand = new ArrayList<>(Arrays.asList("HP", "Lenovo"));
        return Stream.of(
                Arguments.of("Электроника", "Ноутбуки",
                        "Цена, ₽", "10000", "30000", "Производитель", "12", brand)
        );
    }
}
