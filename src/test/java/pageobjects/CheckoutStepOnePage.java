package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutStepOnePage extends MenuPage {

    @FindBy(id = "first-name")
    private WebElement firstNameInput;

    @FindBy(id = "last-name")
    private WebElement lastNameInput;

    @FindBy(id = "postal-code")
    private WebElement postalCodeInput;

    @FindBy(id = "continue")
    private WebElement continueBtn;

    @FindBy(id = "cancel")
    private WebElement cancelBtn;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMsg;

    public CheckoutStepOnePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Waits until the specific Checkout Step One URL is loaded.
     */
    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("checkout-step-one.html"));
    }

    /**
     * Fills the required shipping and customer information in the page form fields.
     * Skips writing to fields for parameters passed as null or empty.
     *
     * @param first  User's First Name
     * @param last   User's Last Name
     * @param postal User's Postal Code / Zip string
     */
    public void fillInfo(String first, String last, String postal) {
        if (first != null && !first.isEmpty()) type(firstNameInput, first);
        if (last != null && !last.isEmpty()) type(lastNameInput, last);
        if (postal != null && !postal.isEmpty()) type(postalCodeInput, postal);
    }

    /**
     * Advances to the Checkout Step Two (Overview) page by clicking Continue.
     */
    public void clickContinue() {
        click(continueBtn);
    }

    /**
     * Aggregated action: Populates all user form inputs and strictly clicks Continue.
     * Used for moving directly to the Checkout Step Two page context.
     *
     * @param first  User's First Name
     * @param last   User's Last Name
     * @param postal User's Postal Code
     * @return CheckoutStepTwoPage indicating progress to the overview module.
     */
    public CheckoutStepTwoPage continueToOverview(String first, String last, String postal) {
        fillInfo(first, last, postal);
        clickContinue();
        return new CheckoutStepTwoPage(driver);
    }

    /**
     * Halts the checkout flow and interacts with the Cancel button.
     *
     * @return The previous CartPage object context.
     */
    public CartPage clickCancel() {
        click(cancelBtn);
        return new CartPage(driver);
    }

    /**
     * Detects and returns any validation error message (e.g., missing specific field) shown dynamically.
     *
     * @return Raw visible string text of the error element context.
     */
    public String getErrorMessage() {
        return getText(errorMsg);
    }
}
