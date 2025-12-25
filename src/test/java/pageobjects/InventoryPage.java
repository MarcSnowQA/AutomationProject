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

    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("inventory.html"));
        wait.until(ExpectedConditions.visibilityOf(titleLabel));
        wait.until(ExpectedConditions.visibilityOfAllElements(invItemNames));
    }

    public boolean isLoaded() {
        try {
            waitUntilLoaded();
            return "Products".equalsIgnoreCase(titleLabel.getText().trim());
        } catch (Exception e) {
            return false;
        }
    }

    public int getProductsCount() {
        waitUntilLoaded();
        return invItems.size();
    }

    // ---- Sorting ----

    public void sortBy(String visibleText) {
        wait.until(ExpectedConditions.visibilityOf(sortDropdown));
        new Select(sortDropdown).selectByVisibleText(visibleText);
    }

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

    public void addToCart(String name) {
        WebElement item = findItemContainerByName(name);
        WebElement button = item.findElement(By.cssSelector("button.btn_inventory"));
        if (button.getText().trim().equalsIgnoreCase("Add to cart")) {
            click(button);
        }
    }

    public void removeFromCart(String name) {
        WebElement item = findItemContainerByName(name);
        WebElement button = item.findElement(By.cssSelector("button.btn_inventory"));
        if (button.getText().trim().equalsIgnoreCase("Remove")) {
            click(button);
        }
    }

    public String getButtonTextFor(String name) {
        WebElement item = findItemContainerByName(name);
        return item.findElement(By.cssSelector("button.btn_inventory")).getText().trim();
    }

    // ---- Navigation ----

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

    public void openCart() {
        click(shoppingCartLink);
    }
}
