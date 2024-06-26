import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FirstTest {
    private final static Logger logger = LogManager.getLogger(FirstTest.class);
    private WebDriver webDriver;
    private static final By test = By.xpath("//button[contains(text(),'Change message')]");

    @BeforeAll
    public static void init() {
        logger.trace("======================================================");
        logger.trace("Скачивание WebDriverManager - начато");
        WebDriverManager.chromedriver().setup();
        logger.trace("Скачивание WebDriverManager - завершено");
        logger.trace("======================================================");
    }

    @AfterEach
    public void close() {
        logger.trace("======================================================");
        logger.trace("Закрытие браузера - начато");
        if (webDriver != null) {
            //webDriver.close();
            webDriver.quit();
        }
        logger.trace("Закрытие браузера - завершено");
        logger.trace("======================================================");
    }

    /*Открыть Chrome в headless режиме
    Перейти на https://duckduckgo.com/
    В поисковую строку ввести ОТУС
    Проверить что в поисковой выдаче первый результат Онлайн‑курсы для профессионалов, дистанционное обучение современным ...*/
    @Test
    public void headlessMode() {
        String text = "Онлайн‑курсы для профессионалов, дистанционное обучение современным ...";
        logger.trace("======================================================");
        logger.trace("Открытие браузера headlessMode - начато");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        logger.trace("Открытие браузера - завершено");
        logger.trace("Открытие сайта https://duckduckgo.com/ - начато");
        webDriver.get("https://duckduckgo.com/");
        logger.trace("Открытие сайта - завершено");
        WebElement search = webDriver.findElement(new By.ByXPath("//input[@class='searchbox_input__bEGm3']"));
        logger.trace("Клик в поиск");
        search.click();
        logger.trace("Ввод текст ОТУС");
        search.sendKeys("ОТУС");
        logger.trace("Клик enter");
        Actions actions = new Actions(webDriver);
        actions.sendKeys(Keys.ENTER).perform();
        logger.trace("Находим текст первой ссылки");
        WebElement span = webDriver.findElement(new By.ByXPath("//ol[@class='react-results--main']/li[1]//a[@data-testid='result-title-a']//span"));
        String title = span.getText();
        logger.trace("Сравниваем с эталоном");
        System.out.println("ОР " + text);
        System.out.println("ФР " + title);
        Assertions.assertEquals(text, title);
        logger.trace("======================================================");
    }

    /*
    Открыть Chrome в режиме киоска
    Перейти на https://demo.w3layouts.com/demos_new/template_demo/03-10-2020/photoflash-liberty-demo_Free/685659620/web/index.html?_ga=2.181802926.889871791.1632394818-2083132868.1632394818
    Нажать на любую картинку
    Проверить что картинка открылась в модальном окне
    * */
    @Test
    void kioskMode() {
        logger.trace("======================================================");
        logger.trace("Открытие браузера kioskMode - начато");
        ChromeOptions options = new ChromeOptions();
        webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        options.addArguments("--kiosk");
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        logger.trace("Открытие браузера - завершено");
        logger.trace("Открытие сайта картинок- начато");
        webDriver.get("https://demo.w3layouts.com/demos_new/template_demo/03-10-2020/photoflash-liberty-demo_Free/685659620/web/index.html?_ga=2.181802926.889871791.1632394818-2083132868.1632394818");
        logger.trace("Открытие сайта - завершено");
        WebElement pict = webDriver.findElement(new By.ByXPath("//li[@data-id='id-1']"));
        logger.trace("Кликаем на первую картинку");
        pict.click();
        WebElement modalForm = new WebDriverWait(webDriver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(new By.ByXPath("//div[@class='pp_overlay']")));
        System.out.println("ОР true");
        System.out.println("ФР " + modalForm.isDisplayed());
        Assertions.assertTrue(modalForm.isDisplayed());
        logger.trace("======================================================");
    }

    /*
    * Открыть Chrome в режиме полного экрана
    Перейти на https://otus.ru
    Авторизоваться под каким-нибудь тестовым пользователем(можно создать нового)
    Вывести в лог все cookie
    * */
    @Test
    public void maximizeMode() {
        logger.trace("======================================================");
        webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        webDriver.manage().window().maximize();
        logger.trace("Открытие сайта http://otus.ru - начато");
        webDriver.get("http://otus.ru");
        logger.trace("Клик на Войти");
        String btnEnter = "//button[text()='Войти']";
        WebElement button = webDriver.findElement(new By.ByXPath(btnEnter));
        button.click();
        logger.trace("Ввод почты");
        webDriver.findElement(new By.ByXPath("//div[./input[@name='email']]")).click();
        webDriver.findElement(new By.ByXPath("//input[@name='email']")).sendKeys(System.getProperty("email"));
        logger.trace("Ввод пароля");
        webDriver.findElement(new By.ByXPath("//div[./input[@type='password']]")).click();
        webDriver.findElement(new By.ByXPath("//input[@type='password']")).sendKeys(System.getProperty("pass"));
        logger.trace("Клик на Войти");
        WebElement enter = webDriver.findElement(new By.ByXPath("//button[./*[text()='Войти']]"));
        enter.click();
        logger.trace("Отобразить куки " + webDriver.manage().getCookies());
        System.out.println(webDriver.manage().getCookies());
        logger.trace("======================================================");
    }
}
