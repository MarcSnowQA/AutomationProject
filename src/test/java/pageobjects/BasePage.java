package pageobjects;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    private final JavascriptExecutor js;

    // Turn this on only when debugging (highlight slows tests a bit)
    private final boolean highlight;

    protected BasePage(WebDriver driver) {
        this(driver, Duration.ofSeconds(10), false);
    }

    protected BasePage(WebDriver driver, Duration timeout, boolean highlight) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
        this.js = (JavascriptExecutor) driver;
        this.highlight = highlight;

        PageFactory.initElements(driver, this);
    }

    /* -------------------- Wait helpers -------------------- */

    protected WebElement waitVisible(WebElement el) {
        return wait.until(ExpectedConditions.visibilityOf(el));
    }

    protected WebElement waitClickable(WebElement el) {
        return wait.until(ExpectedConditions.elementToBeClickable(el));
    }

    protected Alert waitAlert() {
        return wait.until(ExpectedConditions.alertIsPresent());
    }

    /* -------------------- Actions -------------------- */

    protected void type(WebElement el, String text) {
        waitVisible(el);
        doHighlight(el, "yellow");
        el.clear();
        el.sendKeys(text);
    }

    protected void typeNumber(WebElement el, int num) {
        type(el, String.valueOf(num));
    }

    protected void click(WebElement el) {
        waitClickable(el);
        doHighlight(el, "red");

        try {
            el.click();
        } catch (ElementNotInteractableException e) {
            // Fallback: JS click (useful for overlays/animations)
            jsClick(el);
        }
    }

    protected String getText(WebElement el) {
        waitVisible(el);
        doHighlight(el, "green");
        return el.getText().trim();
    }

    protected String getValue(WebElement el) {
        waitVisible(el);
        doHighlight(el, "green");
        return el.getAttribute("value");
    }

    protected boolean isDisplayedSafe(WebElement el) {
        try {
            return el.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /* -------------------- Alerts -------------------- */

    protected void alertAccept() {
        waitAlert().accept();
    }

    protected void alertSendKeysAndAccept(String text) {
        Alert alert = waitAlert();
        alert.sendKeys(text);
        alert.accept();
    }

    /* -------------------- JS helpers -------------------- */

    protected void jsClick(WebElement el) {
        js.executeScript("arguments[0].click();", el);
    }

    protected void scrollIntoView(WebElement el) {
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", el);
    }

    private void doHighlight(WebElement el, String color) {
        if (!highlight) return;

        try {
            String original = el.getAttribute("style");
            if (original == null) original = "";

            String style = original + " border: 2px solid " + color + ";";
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", el, style);

            // tiny pause just for visibility while debugging (NOT for stability)
            try { Thread.sleep(120); } catch (InterruptedException ignored) {}

            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", el, original);
        } catch (Exception ignored) {
            // Never fail the test because highlight failed
        }
    }
}
