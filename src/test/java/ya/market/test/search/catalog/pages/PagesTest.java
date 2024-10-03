package ya.market.test.search.catalog.pages;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ya.market.search.catalog.pages.MainPage;
import ya.market.search.catalog.pages.ProductListPage;
import ya.market.search.catalog.steps.StepsAssertions;
import ya.market.test.TestBaseSetup;

import java.util.List;

import static ya.market.helpers.Properties.testsProperties;

public class PagesTest extends TestBaseSetup {

    /**
     * <p> метод задает шаги тестирования</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param section      параметр раздела каталога
     * @param subcategory  параметр подкатегории каталога
     * @param diapason     параметр названия фильтра по диапазону
     * @param from         параметр значения -От-
     * @param to           параметр значения -До-
     * @param criteria     параметр названия фильтра по критериям
     * @param countProduct параметр количества товара
     * @param brand        списоок критериев для фильтра
     */
    @Feature("Проверка результатов поиска")
    @DisplayName("Поиск товара с параметрами фильтра")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @MethodSource("ya.market.helpers.DataProvider#providerCheckingLaptopProduct")
    public void stepNexPageTest(String section, String subcategory, String diapason,
                                String from, String to, String criteria,
                                String countProduct, List<String> brand) {
        chromeDriver.get(testsProperties.urlTest());
        MainPage mainPage = new MainPage(chromeDriver);
        mainPage.goToCatalogMenu();
        mainPage.hoverFromSectionCatalog(section);
        mainPage.goToSubcategoryFromCatalog(subcategory);
        ProductListPage productListPage = new ProductListPage(chromeDriver);
        StepsAssertions stepsAssertions = new StepsAssertions();
        stepsAssertions.transitionToProductPage(productListPage, subcategory);
        productListPage.rangeFilter(diapason, from, to);
        productListPage.enumFilter(criteria, brand);
        stepsAssertions.cardCountOnPageProduct(productListPage, subcategory, countProduct);
        stepsAssertions.nameAllProduct(productListPage, from, to, brand);
        productListPage.getNameFromFirstProduct();
        productListPage.addValueToSearch();
        productListPage.clickOnFind();
        stepsAssertions.checkingSearchResult(productListPage);
    }
}