package ya.market.search.catalog.steps;

import io.qameta.allure.Step;
import ya.market.search.catalog.pages.ProductListPage;

import java.util.List;

public class StepsAssertions {

    /**
     * <p> метод вызывает проверку названия раздела/p>
     * <p> автор: Роман Гудков</p>
     *
     * @param productListPage параметр экземпляра объекта
     * @param title       параметр названия раздела
     */
    @Step("Проверяем переход на страницу --{title}--")
    public void transitionToProductPage(ProductListPage productListPage, String title) {
        productListPage.checkingTransitionOnPage(title);
    }

    /**
     * <p> метод вызывает проверку количества отображаемых товаров</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param productListPage параметр экземпляра объекта
     * @param product     параметр названия товара
     * @param count       параметр колтчества товаров
     */
    @Step("Проверяем количество карточек товара --{product}-- на странице")
    public void cardCountOnPageProduct(ProductListPage productListPage, String product, String count) {
        productListPage.cardCountOnPage(product, count);
    }

    /**
     * <p> метод вызывает проверку соответствия заданным фильтрам</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param productListPage параметр экземпляра объекта
     */
    @Step("Проверяем соответствие товара выбранным критериям")
    public void nameAllProduct(ProductListPage productListPage, String valueFrom,
                               String valueTo, List<String> criteria) {
        productListPage.checkParameterInProductCard(valueFrom, valueTo, criteria);
    }

    /**
     * <p> метод вызывает проверку результатов запроса товара</p>
     * <p> автор: Роман Гудков</p>
     * @param productListPage параметр экземпляра объекта
     */
    @Step("Проверяем результат запроса товара")
    public void checkingSearchResult(ProductListPage productListPage) {
        productListPage.checkingResult();
    }
}