package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutStepTwoPage extends MenuPage {

    @FindBy(id = "finish")
    private WebElement finishBtn;

    @FindBy(id = "cancel")
    private WebElement cancelBtn;

    @FindBy(css = ".summary_subtotal_label")
    private WebElement itemTotalLabel;

    @FindBy(css = ".summary_tax_label")
    private WebElement taxLabel;

    @FindBy(css = ".summary_total_label")
    private WebElement totalLabel;

    public CheckoutStepTwoPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Waits until the explicit Checkout Step Two specific URL is loaded.
     */
    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));
    }

    /**
     * Validates that the Checkout Step Two page is fully loaded and displayed.
     *
     * @return true if correctly loaded, false otherwise.
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
     * Finalizes the purchase by clicking the 'Finish' button.
     *
     * @return The resulting CheckoutCompletePage object.
     */
    public CheckoutCompletePage clickFinish() {
        click(finishBtn);
        return new CheckoutCompletePage(driver);
    }

    /**
     * Aborts the checkout transaction and clicks the back/cancel button.
     *
     * @return The resulting InventoryPage object as user is redirected.
     */
    public InventoryPage clickCancel() {
        click(cancelBtn);
        return new InventoryPage(driver);
    }

    /**
     * Retrieves the subtotal price (pre-tax) for all items in the cart.
     *
     * @return The aggregated monetary subtotal as a double.
     */
    public double getItemTotal() {
        String text = getText(itemTotalLabel);
        return Double.parseDouble(text.replace("Item total: $", "").trim());
    }

    /**
     * Retrieves the determined tax metric for the generated totals.
     *
     * @return The monetary tax as a double.
     */
    public double getTax() {
        String text = getText(taxLabel);
        return Double.parseDouble(text.replace("Tax: $", "").trim());
    }

    /**
     * Retrieves the definitive total payment requisite (tax + item subtotal).
     *
     * @return The total price evaluated securely as a double.
     */
    public double getTotal() {
        String text = getText(totalLabel);
        return Double.parseDouble(text.replace("Total: $", "").trim());
    }
}
