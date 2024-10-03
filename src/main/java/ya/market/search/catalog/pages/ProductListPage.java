package ya.market.search.catalog.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ya.market.helpers.LetterTranslate;
import ya.market.search.catalog.assertions.Assertion;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static ya.market.helpers.Properties.pagesProperties;
import static ya.market.helpers.Properties.testsProperties;

public class ProductListPage {

    /**
     * <p> переменная для хранения названия товара</p>
     * автор: Роман Гудков</p>
     */
    private String productName;

    /**
     * <p> селектор заголовка раздела</p>
     * <p> автор: Роман Гудков</p>
     */
    private String headSection = "//div[@data-zone-name='searchTitle']//h1";

    /**
     * <p> селектор блока фильтров</p>
     * <p> автор: Роман Гудков</p>
     */
    private String blockFilters = "//div[@data-grabber='SearchFilters']";

    /**
     * <p> драйвера для доступа к Chrome browser</p>
     * <p> автор: Роман Гудков</p>
     */
    private WebDriver chromeDriver;

    /**
     * <p> ожидает выполнение заданного сценария</p>
     * <p> автор: Роман Гудков</p>
     */
    private WebDriverWait wait;

    /**
     * <p> осуществляет симуляцию действий мыши</p>
     * <p> автор: Роман Гудков</p>
     */
    private Actions actions;

    /**
     * <p> конструктор класса</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param chromeDriver драйвер браузера
     */
    public ProductListPage(WebDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
        this.wait = new WebDriverWait(chromeDriver, testsProperties.defaultTimeout());
        this.actions = new Actions(chromeDriver);
    }

    /**
     * <p> метод проверяет переход на страницу</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param head параметр названия страницы на которую перешли
     */
    @Step("Проверяем заголовок страницы, ожидаем --{head}--")
    public void checkingTransitionOnPage(String head) {
        wait.until(visibilityOfElementLocated(By.xpath(headSection)));
        boolean checkPresence = chromeDriver.findElement(By
                .xpath(headSection)).getText().contains(head);
        Assertion.assertTrue(checkPresence, "переход в --" + head + "-- не выполнен");
    }

    /**
     * <p> метод работает с фильтром по диапазону</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param head парааметр названия фильтра
     * @param from параметр значения --От--
     * @param to   параметр значения --До--
     */
    @Step("Заполняем поля фильтра --{head}--")
    public void rangeFilter(String head, String from, String to) {
        wait.until(visibilityOfElementLocated((By.xpath(blockFilters))));

        checkNameFilter(head, "//div[@data-filter-type='range']//h4");
        setFromOnRangeFilter(head, from);
        setToOnRangeFilter(head, to);
        wait.until(visibilityOfElementLocated((By.xpath("//div[@data-auto='SerpStatic-loader']"))));
        wait.until(invisibilityOfElementLocated(By
                .xpath("//div[@data-auto='SerpStatic-loader']")));
    }

    /**
     * <p> метод задает низ диапазона</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param head      парааметр названия фильтра
     * @param valueFrom параметр значения нижнего диапазона
     */
    @Step("Задаем низ диапазона --{valueFrom}--")
    private void setFromOnRangeFilter(String head, String valueFrom) {
        String fieldFromXpath = blockFilters.concat("//div[@data-filter-type='range']//h4[text()='")
                .concat(head).concat("']/../../following-sibling::div//span[@data-auto='filter-range-min']//input");
        WebElement fromElement = chromeDriver.findElement(By.xpath(fieldFromXpath));

        checkAddingValueToField(head, valueFrom, fieldFromXpath, fromElement);
    }

    /**
     * <p> метод задает верх диапазона</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param head    парааметр названия фильтра
     * @param valueTo параметр значения верхнего диапазона
     */
    @Step("Задаём верх диапазона --{valueTo)--")
    private void setToOnRangeFilter(String head, String valueTo) {
        String fieldToXpath = blockFilters.concat("//div[@data-filter-type='range']//h4[text()='")
                .concat(head).concat("']/../../following-sibling::div//span[@data-auto='filter-range-max']//input");
        WebElement toElement = chromeDriver.findElement(By.xpath(fieldToXpath));

        checkAddingValueToField(head, valueTo, fieldToXpath, toElement);
    }

    /**
     * <p> метод работает с фильтром по критерию</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param head     параметр названия фильтра
     * @param criteria параметр списка критериев
     */
    @Step("Заполняем поля фмльтра --{head}--")
    public void enumFilter(String head, List<String> criteria) {
        wait.until(visibilityOfElementLocated((By.xpath(blockFilters))));

        checkNameFilter(head, "//div[@data-filter-type='enum']//h4");
        checkHiddenFields(head, criteria);
    }

    /**
     * <p> метод считает количество карточек товара на странице</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param product    параметр наименования товара
     * @param countCheck параметр количества отображаемых товаров
     */
    @Step("Проверяем, что карточек товара на первой странице больше --{countCheck}--")
    public void cardCountOnPage(String product, String countCheck) {
        wait.until((visibilityOfElementLocated(By
                .xpath("//div[@id='/content/page/fancyPage/searchSerpStatic']/parent::div"))));
        boolean checking = chromeDriver.findElements(By
                .xpath("//div[@data-apiary-widget-name='@light/Organic']")).size()
                > Integer.parseInt(countCheck);
        Assertion.assertTrue(checking, "карточек --" + product + "-- на странице меньше --" + countCheck);
    }

    /**
     * <p> метод проверки отображение товаров согласно установкам фильтра</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param valueFrom нижнее значение
     * @param valueTo   верхнее значение
     * @param criteria  список критериев
     */
    @Step("Проверяем товары согласно фильтрам")
    public void checkParameterInProductCard(String valueFrom, String valueTo, List<String> criteria) {
        wait.until((visibilityOfElementLocated(By
                .xpath("//div[@id='/content/page/fancyPage/searchSerpStatic']/parent::div"))));
        while (!(chromeDriver.findElements(By
                        .xpath("//div[@id='/content/page/fancyPage/searchPager']//span[text()]"))
                .size() == 0)) {
            actions.moveToElement(chromeDriver.findElement(By
                    .xpath("//div[@id='/content/page/fancyPage/searchPager']//span[text()]")))
                    .click().perform();
            wait.until((visibilityOfElementLocated(By
                    .xpath("//div[@id='/content/page/fancyPage/searchSerpStatic']/parent::div"))));
        }
        checkingPriceInCard(valueFrom, valueTo);
        checkingCriteriaInCard(criteria);
    }

    /**
     * <p> метод сохраняет название первого товара из списка товаров</p>
     * <p> автор: Роман Гудков</p>
     */
    @Step("Вернулись в начало скиска и сохранили название первого товара")
    public void getNameFromFirstProduct() {
        actions.moveToElement(chromeDriver.findElement(By
                .xpath("(//div[@data-apiary-widget-name='@light/Organic']" +
                        "//span[@itemprop='name'])[1]")));
        wait.until(visibilityOfElementLocated(By
                .xpath("(//div[@data-apiary-widget-name='@light/Organic']"
                        + "//span[@itemprop='name'])[1]")));
        productName = chromeDriver.findElement(By
                .xpath("(//div[@data-apiary-widget-name='@light/Organic']" +
                        "//span[@itemprop='name'])[1]")).getText();
    }

    /**
     * <p> метод заполняет строку поиска</p>
     * <p> автор: Роман Гудков</p>
     */
    @Step("Вводим в строку поиска название сохраненного товара")
    public void addValueToSearch() {
        String searchField = "//input[@id='header-search']";
        actions.moveToElement(chromeDriver.findElement(By.xpath(searchField)));
        wait.until(visibilityOfElementLocated(By.xpath(searchField)));
        chromeDriver.findElement(By.xpath(searchField)).click();
        chromeDriver.findElement(By.xpath(searchField)).sendKeys(productName);
        String actualValue = chromeDriver.findElement(By.xpath(searchField)).getAttribute("value");
        boolean isMatch = actualValue != null && actualValue.equals(productName);
        Assertion.assertTrue(isMatch, "строка поиска не заполнена значением " + productName);
    }

    /**
     * <p> метод кликает по кнопке поиска</p>
     * <p> автор: Роман Гудков</p>
     */
    @Step("Кликаем по кнопке --Найти--")
    public void clickOnFind() {
        chromeDriver.findElement(By
                .xpath("//span[text()='Найти']/parent::button[@data-auto='search-button']")).click();
    }

    /**
     * <p> метод сравниввает результаты поиска с запросом</p>
     * <p> автор: Роман Гудков</p>
     */
    @Step("Проверяем результаты после запроса товара")
    public void checkingResult() {
        wait.until(visibilityOfElementLocated(By.xpath("//div[@data-auto='SerpList']")));
        List<WebElement> elements = chromeDriver.findElements(By
                .xpath("//div[@data-apiary-widget-name='@light/Organic']//span[@itemprop='name']"));
        boolean nameContain = elements.stream()
                .anyMatch(e -> e.getText().contains(productName));
        Assertion.assertTrue(nameContain, "в результатах поиска не найдено --" + productName + "--");
    }

    /**
     * <p> метод проверяет наличие элемента по его названию</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param value     параметр названия фильтра
     * @param fileXpath параметр Xpath селектора
     */
    @Step("Проверяем наличие фильтра --{value}--")
    private void checkNameFilter(String value, String fileXpath) {
        List<WebElement> element = chromeDriver.findElements(By.xpath(fileXpath));
        boolean checkPresence = element.stream()
                .anyMatch(e -> e.getText().trim().equals(value));
        Assertion.assertTrue(checkPresence, "фильтр --" + value + "-- не найден");
    }

    /**
     * <p> метод проверяет наличие кнопки раскрытия списка критериев</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param head     параметр названия фильтра
     * @param criteria параметр список критериев
     */
    private void checkHiddenFields(String head, List<String> criteria) {
        if (chromeDriver.findElements(By
                .xpath("//div[@data-filter-type='enum']//h4[text()='" + head + "']" +
                        "/ancestor::legend/following-sibling::div//div[@data-baobab-name='showMoreFilters']" +
                        "/button")).size() > 0) {
            criteriaCalculate(head, criteria);
        } else {
            checkFieldFilter(head, criteria);
            for (String criteriaItem : criteria) {
                String checkboxXpath = "//div[@data-filter-type='enum']//h4[text()='"
                        + head + "']/../../following-sibling::div//span[text()='" + criteriaItem + "'] /../..";
                checkCheckboxStatus(head, criteriaItem, checkboxXpath);
            }
        }
    }

    /**
     * <p> метод добавлят поля в фильтр</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param head     параметр названия фильтра
     * @param criteria параметр списка критериев
     */
    @Step("Выбираем криттерий --{criteria}--")
    private void criteriaCalculate(String head, List<String> criteria) {
        int position = 0;

        for (String criteriaItem : criteria) {
            openMoreParameters(head);
            while (true) {
                actions.moveToElement(chromeDriver.findElement(By
                        .xpath("//div[@data-item-index='" + position + "']"))).perform();
                WebElement criteriaName = chromeDriver.findElement(By
                        .xpath("//div[@data-item-index='" + position
                                + "']//span/span/following-sibling::span"));
                String checkboxXpath = "//div[@data-item-index='" + position + "']//label";
                boolean checkboxMarked = chromeDriver.findElement(By.xpath(checkboxXpath))
                        .getAttribute("aria-checked").equals("false");
                boolean itemEquals = criteriaName.getText().equals(criteriaItem);
                if (itemEquals && checkboxMarked) {
                    chromeDriver.findElement(By.xpath(checkboxXpath)).click();
                    wait.until(visibilityOfElementLocated((By
                            .xpath("//div[@data-auto='SerpStatic-loader']"))));
                    wait.until(invisibilityOfElementLocated(By
                            .xpath("//div[@data-auto='SerpStatic-loader']")));
                    checkCheckboxStatus(criteriaItem);
                    position = 0;
                    break;
                }
                if (chromeDriver.findElements(By
                        .xpath("//div[@data-item-index='" + (position + 1) + "']")).size() > 0) {
                    position++;
                    continue;
                }
                Assertion.assertTrue(false, "критерий --" + criteriaItem + "-- не найден");
            }
        }
    }

    /**
     * <p> метод разворачивает скрытый список критериев</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param head параметр название фильта
     */
    private void openMoreParameters(String head) {
        int loop = pagesProperties.loopCount();
        boolean loopCheck = false;
        while (loop >= 0 && !loopCheck) {
            loop--;
            WebElement elementXpath = chromeDriver.findElement(By
                    .xpath("//div[@data-filter-type='enum']//h4[text()='" + head + "']" +
                            "/ancestor::legend/following-sibling::div//div[@data-baobab-name='showMoreFilters']/button"));
            if (elementXpath.getAttribute("aria-expanded").equals("false")) {
                chromeDriver.findElement(By
                        .xpath("//div[@data-filter-type='enum']//h4[text()='" + head + "']" +
                                "/ancestor::legend/following-sibling::div//button")).click();
                wait.until(invisibilityOfElementLocated((By
                        .xpath("//div[@data-filter-type='enum']//h4[text()='" + head + "']" +
                                "/ancestor::legend/parent::fieldset/div/ul"))));
                wait.until(visibilityOfElementLocated((By
                        .xpath("//div[@data-filter-type='enum']//h4[text()='" + head + "']" +
                                "/ancestor::legend/following-sibling::div//div[@data-test-id='virtuoso-item-list']"))));
                continue;
            }
            loopCheck = true;
        }
    }

    /**
     * <p> метод проверяет наличие элемента по его названию, список</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param head  параметр названия фильтра
     * @param value параметр список критериев
     */
    @Step("Проверяем существование критериев --{value}--")
    private void checkFieldFilter(String head, List<String> value) {
        String nameCriteriaXpath = "//div[@data-filter-type='enum']//h4[text()='".concat(head)
                .concat("']/../../following-sibling::div//span[text()]");

        List<WebElement> element = chromeDriver.findElements(By.xpath(nameCriteriaXpath));
        for (String itemValue : value) {
            boolean checkPresence = element.stream()
                    .anyMatch(e -> e.getText().equals(itemValue));
            Assertion.assertTrue(checkPresence, "критерий --" + itemValue + "-- не найден");
        }
    }

    /**
     * <p> метод добавлят поля в фильтр</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param criteriaItem параметр название критерия
     */
    @Step("Выбираем --{criteriaItem}--")
    private void checkCheckboxStatus(String criteriaItem) {
        boolean checkboxControl = chromeDriver.findElement(By
                        .xpath("//span[text()='" + criteriaItem + "']/ancestor::label"))
                .getAttribute("aria-checked").equals("true");
        Assertion.assertTrue(checkboxControl, "критерий --" + criteriaItem + "-- не отмечен");
    }

    /**
     * <p> метод добавлят поля в фильтр</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param head          параметр названия фильтра
     * @param criteriaItem  параметр название критерия
     * @param checkboxXpath параметр селектор xpath
     */
    @Step("Выбираем криттерий --{criteriaItem}--")
    private void checkCheckboxStatus(String head, String criteriaItem, String checkboxXpath) {
        if (chromeDriver.findElement(By.xpath(checkboxXpath)).getAttribute("aria-checked").equals("false")) {
            chromeDriver.findElement(By.xpath(checkboxXpath)).click();
            wait.until(visibilityOfElementLocated((By.xpath("//div[@data-auto='SerpStatic-loader']"))));
            wait.until(invisibilityOfElementLocated(By
                    .xpath("//div[@data-auto='SerpStatic-loader']")));
        }
        boolean checkPresence = chromeDriver.findElement(By.xpath(checkboxXpath))
                .getAttribute("aria-checked").equals("true");
        Assertion.assertTrue(checkPresence, "для --" + head + "-- критерий --" + criteriaItem + "-- не отмечен");
    }

    /**
     * <p> метод устанавливает значение в поле</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param head       параметр названия фильтра
     * @param value      параметр устанавлтваемого значения
     * @param fieldXpath параметр Xpath селектора
     * @param element    параметр элемента на странице
     */
    @Step("Проверяем установку --{value}--")
    private void checkAddingValueToField(String head, String value, String fieldXpath, WebElement element) {
        int loop = pagesProperties.loopCount();
        boolean loopCheck = false;

        while (loop >= 0 && !loopCheck) {
            element.clear();
            element.sendKeys(value);
            String valueOnField = chromeDriver.findElement(By
                    .xpath(fieldXpath)).getAttribute("value");
            loopCheck = value.equals(valueOnField);
            loop--;
        }
        Assertion.assertTrue(loopCheck, "значение --" + value + "-- для фильтра --" + head + "-- не задано");
    }

    /**
     * <p> метод проверяет заголовок товара по выбранным в фильтре критериям</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param criteria параметр список критериев фильтра
     * @return число
     */
    @Step("Проверяем --{productName}-- и --{criteria}-- в названии товара")
    private void checkingCriteriaInCard(List<String> criteria) {
        List<WebElement> elements = chromeDriver.findElements(By
                .xpath("//div[@data-apiary-widget-name='@light/Organic']//span[@itemprop='name']"));
        List<String> collect = elements.stream()
                .filter(e -> !criteria.stream().anyMatch(c -> {
                    String webText = e.getText().toLowerCase();
                    if (Pattern.compile("([a-z]+[а-я])|\\s([а-я][a-z]+)|([a-z]+[а-я][a-z]+)").matcher(webText).find()
                    ) {
                        webText = LetterTranslate.convertCyrillicToLatin(webText);
                    }
                    return webText.contains(c.toLowerCase());
                }))
                .map(WebElement::getText)
                .collect(Collectors.toList());
        Assertion.assertTrue(collect.size() == 0, "из " + elements.size() + " заголовков "
                + collect.size() + " не соответствуют фильтру " + criteria + " это \n \t" + collect);
    }

    /**
     * <p> метод сравнивает стоимость товара в границах фильтра фильтре</p>
     * <p> автор: Роман Гудков</p>
     *
     * @param valueFrom параметр значения -от-
     * @param valueTo   параметр значения -до-
     */
    @Step("Проверяем стоимость товара в границах фильтра")
    private void checkingPriceInCard(String valueFrom, String valueTo) {
        List<WebElement> elements = chromeDriver.findElements(By
                .xpath("//div[@data-apiary-widget-name='@light/Organic']//span[@itemprop='name']" +
                        "/ancestor::div[@data-baobab-name='title']/parent::div/following-sibling::div" +
                        "//span[contains(text(),'Цена')]"));
        int fromPrice = Integer.parseInt(valueFrom);
        int toPrice = Integer.parseInt(valueTo);
        List<String> collect = elements.stream()
                .map(WebElement::getText)
                .map(e -> {
                    String elementPrice = e.replaceAll("[^\\d*₽+]", "");
                    String twoPrice = elementPrice.replaceAll("₽", " ");
                    if (twoPrice.isEmpty()) return String.valueOf(0);
                    int spaceIndex = twoPrice.indexOf(" ");
                    return twoPrice.substring(0, spaceIndex);
                })
                .mapToInt(e -> Integer.parseInt(e))
                .filter(e -> e < fromPrice || e > toPrice)
                .mapToObj(e -> String.valueOf(e))
                .collect(Collectors.toList());
        Assertion.assertTrue(collect.size() == 0, "из " + elements.size() + " цен "
                + collect.size() + " не соответствуют установкам фильтра, это - " + collect + " ₽");
    }
}