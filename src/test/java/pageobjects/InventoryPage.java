package pageobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class InventoryPage extends MenuPage {

    @FindBy(css = ".inventory_item")
    private List<WebElement> invItems;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> invItemNames;

    @FindBy(css = ".product_sort_container")
    private WebElement sortDropdown;

    @FindBy(css = ".shopping_cart_link")
    private WebElement shoppingCartLink;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(css = ".title")
    private WebElement titleLabel;

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    // ---- Page state ----

    /**
     * Waits until the Inventory page URL contains 'inventory.html' and all main list elements are visible.
     */
    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("inventory.html"));
        wait.until(ExpectedConditions.visibilityOf(titleLabel));
        wait.until(ExpectedConditions.visibilityOfAllElements(invItemNames));
    }

    /**
     * Safely checks if the Inventory page correctly loaded and displays the correct title.
     *
     * @return true if successfully loaded, false otherwise.
     */
    public boolean isLoaded() {
        try {
            waitUntilLoaded();
            return "Products".equalsIgnoreCase(titleLabel.getText().trim());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Determines how many products are currently listed on the view.
     *
     * @return Number of listed product elements.
     */
    public int getProductsCount() {
        waitUntilLoaded();
        return invItems.size();
    }

    // ---- Sorting ----

    /**
     * Selects an option from the filter dropdown by its exact visible text.
     *
     * @param visibleText Expected text such as "Price (low to high)".
     */
    public void sortBy(String visibleText) {
        wait.until(ExpectedConditions.visibilityOf(sortDropdown));
        new Select(sortDropdown).selectByVisibleText(visibleText);
    }

    /**
     * Iterates over all listed products to extract their monetary price representations.
     *
     * @return List of parsed double prices representing the sorted/unsorted catalog view.
     */
    public List<Double> getAllDisplayedPrices() {
        waitUntilLoaded();
        List<Double> prices = new ArrayList<>();

        for (WebElement item : invItems) {
            String priceText = item.findElement(By.cssSelector(".inventory_item_price"))
                                   .getText().trim(); // like "$29.99"
            prices.add(Double.parseDouble(priceText.replace("$", "")));
        }
        return prices;
    }

    // ---- Cart badge ----

    /**
     * Determines the total amount shown dynamically in the cart header overlay badge.
     *
     * @return Number of items detected; Returns 0 if none are visible.
     */
    public int getCartBadgeCount() {
        try {
            // badge is not present when count == 0
            wait.until(ExpectedConditions.visibilityOf(shoppingCartLink));
            if (!cartBadge.isDisplayed()) return 0;
            return Integer.parseInt(cartBadge.getText().trim());
        } catch (Exception e) {
            return 0;
        }
    }

    // ---- Add/Remove by product name (FIXED) ----

    private WebElement findItemContainerByName(String name) {
        waitUntilLoaded();
        for (WebElement item : invItems) {
            String itemName = item.findElement(By.cssSelector(".inventory_item_name")).getText().trim();
            if (itemName.equalsIgnoreCase(name)) {
                return item;
            }
        }
        throw new NoSuchElementException("Product not found on Inventory page: " + name);
    }

    /**
     * Adds a product to the cart by simulating a button click, finding it by its exact name.
     *
     * @param name Target exact text matching a particular product.
     */
    public void addToCart(String name) {
        WebElement item = findItemContainerByName(name);
        WebElement button = item.findElement(By.cssSelector("button.btn_inventory"));
        if (button.getText().trim().equalsIgnoreCase("Add to cart")) {
            click(button);
        }
    }

    /**
     * Removes a particular product from the cart by finding it by name and clicking the Remove specific button.
     *
     * @param name Target exact text matching a particular product.
     */
    public void removeFromCart(String name) {
        WebElement item = findItemContainerByName(name);
        WebElement button = item.findElement(By.cssSelector("button.btn_inventory"));
        if (button.getText().trim().equalsIgnoreCase("Remove")) {
            click(button);
        }
    }

    /**
     * Retrieves the text shown universally over a product's action button.
     *
     * @param name Target exact text matching the product bounding grid.
     * @return Button string reading either "Add to cart" or "Remove".
     */
    public String getButtonTextFor(String name) {
        WebElement item = findItemContainerByName(name);
        return item.findElement(By.cssSelector("button.btn_inventory")).getText().trim();
    }

    // ---- Navigation ----

    /**
     * Navigates to a specific Product Detail page by clicking on the given item's title in the catalog.
     *
     * @param itemName Exact name text indicating the product to navigate into.
     * @return A newly contextualized ProductPage.
     */
    public ProductPage openProduct(String itemName) {
        waitUntilLoaded();
        for (WebElement title : invItemNames) {
            if (title.getText().trim().equalsIgnoreCase(itemName)) {
                click(title);
                return new ProductPage(driver);
            }
        }
        throw new NoSuchElementException("Product title not found: " + itemName);
    }

    /**
     * Redirects the user directly up to the global shopping cart summary view page.
     */
    public void openCart() {
        click(shoppingCartLink);
    }
}
