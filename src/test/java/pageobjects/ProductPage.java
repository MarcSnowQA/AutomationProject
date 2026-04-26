package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductPage extends MenuPage {

    @FindBy(css = ".inventory_details_name.large_size")
    private WebElement productTitle;

    @FindBy(css = "button.btn_inventory")
    private WebElement addRemoveBtn;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(id = "back-to-products")
    private WebElement backToInventory;

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Waits until the specific Product Page details (like the product title) are visible on screen.
     */
    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.visibilityOf(productTitle));
    }

    /**
     * Verifies whether the Product Page is fully loaded.
     *
     * @return true if loaded and visible, false otherwise.
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
     * Fetches the large product title displayed on the Product Details page.
     *
     * @return The product name as a String.
     */
    public String getProductTitle() {
        waitUntilLoaded();
        return getText(productTitle);
    }

    /**
     * Clicks the "Add to cart" button if the product is not already in the cart.
     */
    public void addToCart() {
        waitUntilLoaded();
        if (addRemoveBtn.getText().trim().equalsIgnoreCase("Add to cart")) {
            click(addRemoveBtn);
        }
    }

    /**
     * Clicks the "Remove" button if the product is currently in the cart.
     */
    public void removeFromCart() {
        waitUntilLoaded();
        if (addRemoveBtn.getText().trim().equalsIgnoreCase("Remove")) {
            click(addRemoveBtn);
        }
    }

    /**
     * Reads the floating shopping cart badge count.
     *
     * @return Integer representing the amount of items in the cart (0 if badge is not shown).
     */
    public int getCartBadgeCount() {
        try {
            return Integer.parseInt(cartBadge.getText().trim());
        } catch (Exception e) {
            return 0; // badge not shown means 0
        }
    }

    /**
     * Navigates back to the Inventory (Products List) page.
     */
    public void backToInventory() {
        click(backToInventory);
    }
}
