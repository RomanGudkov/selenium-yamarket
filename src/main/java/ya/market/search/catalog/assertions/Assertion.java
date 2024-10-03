package ya.market.search.catalog.assertions;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

public class Assertion {

    /**
     * <p> метод проверяет входные данные </p>
     * <p> автор: Роман Гудков </p>
     *
     * @param checkPresence параметр boolean
     * @param message      параметр входных данных теста
     */
    @Step("Проверяем отсутствие ошибки --{message}--")
    public static void assertTrue(boolean checkPresence, String message) {
        Assertions.assertTrue(checkPresence, message);
    }
}