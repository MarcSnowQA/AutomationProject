package pageobjects;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends MenuPage {

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "checkout")
    private WebElement checkoutBtn;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingBtn;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Waits until the Cart page URL matches 'cart.html'.
     */
    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("cart.html"));
    }

    /**
     * Verifies whether the Cart Page is correctly loaded.
     *
     * @return true if the page URL is correct, false otherwise.
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
     * Returns the total amount of individual product items presently displayed in the cart.
     *
     * @return Count of product rows in the cart list.
     */
    public int getCartItemsCount() {
        return cartItems.size();
    }

    /**
     * Proceeds to the checkout flow by clicking the Checkout button.
     *
     * @return The CheckoutStepOnePage representing the next page in the workflow.
     */
    public CheckoutStepOnePage clickCheckout() {
        click(checkoutBtn);
        return new CheckoutStepOnePage(driver);
    }

    /**
     * Clicks "Continue Shopping" to navigate back to the Inventory page.
     *
     * @return The localized InventoryPage object.
     */
    public InventoryPage clickContinueShopping() {
        click(continueShoppingBtn);
        return new InventoryPage(driver);
    }

    /**
     * Removes a specific item from the cart matching the given string parameter.
     *
     * @param itemName Exact name of the product to remove.
     */
    public void removeItem(String itemName) {
        for (WebElement item : cartItems) {
            WebElement title = item.findElement(By.cssSelector(".inventory_item_name"));
            if (title.getText().trim().equalsIgnoreCase(itemName)) {
                item.findElement(By.cssSelector("button[data-test^='remove']")).click();
                return;
            }
        }
    }
}
