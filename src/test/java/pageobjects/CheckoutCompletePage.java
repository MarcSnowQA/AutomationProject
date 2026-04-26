package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutCompletePage extends MenuPage {

    @FindBy(css = ".complete-header")
    private WebElement completeHeader;

    @FindBy(id = "back-to-products")
    private WebElement backHomeBtn;

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Waits until the explicit Checkout Complete page attributes are fully visible on screen.
     */
    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("checkout-complete.html"));
        wait.until(ExpectedConditions.visibilityOf(completeHeader));
    }

    /**
     * Determines whether the Checkout Complete context screen is valid and successfully visible.
     *
     * @return true if visible, false otherwise.
     */
    public boolean isLoaded() {
        try {
            waitUntilLoaded();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Scrapes the primary success heading message.
     * Often reads: "Thank you for your order!"
     *
     * @return Text representation of the heading banner.
     */
    public String getHeaderText() {
        return getText(completeHeader);
    }

    /**
     * Clicks the concluding Back Home interactive link bridging the user back to Inventory products list.
     *
     * @return The standard InventoryPage class object context.
     */
    public InventoryPage clickBackHome() {
        click(backHomeBtn);
        return new InventoryPage(driver);
    }
}
