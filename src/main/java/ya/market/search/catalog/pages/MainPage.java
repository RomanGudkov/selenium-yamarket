package ya.market.search.catalog.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ya.market.search.catalog.assertions.Assertion;

import java.util.*;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static ya.market.helpers.Properties.pagesProperties;
import static ya.market.helpers.Properties.testsProperties;

public class MainPage {

    /**
     * <p> селектор кнопки --каталог--</p>
     * <p> автор: Роман Гудков</p>
     */
    private String buttonCatalog = "//div[@data-zone-name='catalog']//span[text()='Каталог']";

    /**
     * <p> селектор блока списка разделов в динамическом блоке каталога </p>
     * <p> автор: Роман Гудков</p>
     */
    private String blockSectionsInDynamicCatalog = "//div[@data-zone-name='catalog-content'] //ul[@role='tablist']";

    /**
     * <p> драйвера для доступа к Chrome browser</p>
     * <p> автор: Роман Гудков</p>
     */
    private WebDriver chromeDriver;

    /**
     * <p> осуществляет симуляцию действий мыши</p>
     * <p> автор: Роман Гудков</p>
     */
    private Actions actions;

    /**
     * <p> ожидает выполнение заданного сценария</p>
     * <p> автор: Роман Гудков</p>
     */
    private WebDriverWait wait;

    /**
     * <p> структура блока категорий</p>
     * <p> автор: Роман Гудков</p>
     */
    private Map<String, List<String>> categoryContentFromSectionCatalog;

    /**
     * <p> конструктор класса</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param chromeDriver драйвер браузера
     */
    public MainPage(WebDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
        this.wait = new WebDriverWait(chromeDriver, testsProperties.defaultTimeout());
        this.actions = new Actions(chromeDriver);
        categoryContentFromSectionCatalog = new HashMap<>();
    }

    /**
     * <p> метод выполняет переход в каталог</p>
     * <p> автор: Роман Гудков</p>
     */
    @Step("Кликаем по кнопке --Каталог--")
    public void goToCatalogMenu() {
        wait.until(visibilityOfElementLocated(By.xpath(buttonCatalog)));
        chromeDriver.findElement(By.xpath(buttonCatalog)).click();
    }

    /**
     * <p> метод выбирает раздел каталога</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param section параметр названия раздела
     */
    @Step("Наводим курсор на раздел --{section}--")
    public void hoverFromSectionCatalog(String section) {
        wait.until(visibilityOfElementLocated(By.xpath(blockSectionsInDynamicCatalog)));
        String sectionXpath = blockSectionsInDynamicCatalog
                .concat("//span[text()='").concat(section).concat("']");
        checkSectionCatalogInDOM(section, sectionXpath);
        hoverRepeat(section, sectionXpath);
        createListCategoryOfSections();
    }

    /**
     * <p> метод выполняет переход в подкатегорию каталога</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param subcategory параметр названия подкатегории
     */
    @Step("Переходим в подкатегорию --{subcategory}--")
    public void goToSubcategoryFromCatalog(String subcategory) {
        String subcategoryXpath = "//div[@role='tabpanel'] //div[@data-auto='category']//a[text()='"
                .concat(subcategory).concat("']");

        checkSubcategoryInSection(subcategory);
        chromeDriver.findElement(By.xpath(subcategoryXpath)).click();
    }

    /**
     * <p> метод проверяет существование раздела в каталоге</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param checking параметр названия запрошенного раздела
     * @param xPath    селектор раздела в DOM-дереве
     */
    @Step("Проверяем существование раздела")
    private void checkSectionCatalogInDOM(String checking, String xPath) {
        boolean checkPresence = chromeDriver.findElements(By.xpath(xPath)).size() > 0;

        Assertion.assertTrue(checkPresence, "раздел --" + checking + "-- не найден");
    }

    /**
     * <p> метод проверяет существование подкатегории в каталоге</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param checking параметр назввания запрошенной категории
     */
    @Step("Проверяем существование --{checking}--")
    private void checkSubcategoryInSection(String checking) {
        boolean checkPresence = categoryContentFromSectionCatalog.values().stream()
                .anyMatch(e -> e.stream()
                        .anyMatch(s -> s.trim().equals(checking.trim())));
        Assertion.assertTrue(checkPresence, "подкатегория --" + checking + "-- не найдена");
    }

    /**
     * <p> метод составляет карту категорий и их подкатегорий</p>
     * <p> автор: Роман Гудков</p>
     */
    private void createListCategoryOfSections() {
        List<WebElement> nameCategoryList = chromeDriver.findElements(By
                .xpath("//div[@role='tabpanel']//div[@data-auto='category']//div[@role='heading']"));
        for (WebElement elementCategory : nameCategoryList) {
            String nameCategory = elementCategory.getText();

            openList(nameCategory);

            String subcategoryXpath = "//div[@role='tabpanel'] //div[@data-auto='category']//a[text()='"
                    .concat(nameCategory).concat("']/parent::div/.. /following-sibling::ul/li//a");
            List<WebElement> nameSubcategory = chromeDriver.findElements(By.xpath((subcategoryXpath)));
            List<String> collect = nameSubcategory.stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());
            categoryContentFromSectionCatalog.put(nameCategory, collect);
        }
    }

    /**
     * <p> метод разворачивает список подкатегорий</p>
     * <p> автор: Роман Гудков</p>
     */
    private void openList(String nameCategory) {
        String visibilityXpath = "//div[@data-auto='category']/div[@role='heading']//a[text()='"
                .concat(nameCategory).concat("']/ancestor::div[@data-auto='category']//li/span/span[text()='Ещё']");
        actions.moveToElement(chromeDriver.findElement(By.xpath(("//div[@role='tabpanel']" +
                "//div[@data-auto='category']//div[@role='heading']")))).perform();

        boolean checkPresence = chromeDriver.findElements(By.xpath(visibilityXpath)).size() > 0;
        if (checkPresence) {
            actions.moveToElement(chromeDriver.findElement(By.xpath((visibilityXpath)))).perform();
            wait.until(visibilityOfElementLocated(By.xpath(visibilityXpath)));
            chromeDriver.findElement(By.xpath((visibilityXpath))).click();
        }
    }

    /**
     * <p> метод наводит курсор на раздел и проверяет это</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param section      параметр названия раздела
     * @param sectionXpath селектор xpath раздела
     * @return boolean
     */
    @Step("Выполняем переход в --{section}--")
    private void hoverRepeat(String section, String sectionXpath) {
        String nameSectionXpath = "//div[@role='heading']/a";
        int loop = pagesProperties.loopCount();
        boolean loopCheck = true;
        while (loop >= 0 && loopCheck) {
            loop--;
            actions.moveToElement(chromeDriver.findElement(By.xpath(sectionXpath))).perform();
            if (chromeDriver.findElement(By.xpath(nameSectionXpath)).getText().contains(section)) {
                break;
            }
            int size = chromeDriver.findElements(By
                    .xpath("//div[@data-apiary-widget-id='/content/header/header/catalogEntrypoint/catalog']" +
                            "//ul[@role='tablist']/li")).size();
            actions.moveToElement(chromeDriver.findElement(By
                    .xpath("(//div[@data-apiary-widget-id='/content/header/header/catalogEntrypoint/catalog']" +
                            "//ul[@role='tablist']/li)[" + size + "]"))).perform();
        }
        Assertion.assertTrue(loopCheck, "переход в раздел --" + section + "-- не выполнен");
    }
}