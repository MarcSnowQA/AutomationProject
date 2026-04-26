package tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pageobjects.InventoryPage;
import pageobjects.LoginPage;

public class InventoryTests extends BaseTest {

    @BeforeMethod
    public void login() {
        new LoginPage(driver).login("standard_user", "secret_sauce");
        Assert.assertTrue(new InventoryPage(driver).isLoaded(), "Inventory page should load after login");
    }

    @Test(description = "Display of all products")
    public void atc001_displayOfAllProducts() {
        InventoryPage ip = new InventoryPage(driver);
        Assert.assertTrue(ip.isLoaded(), "Inventory not visible");
        Assert.assertEquals(ip.getProductsCount(), 6, "Expected 6 products on inventory page");
    }

    @Test(description = "Add to Cart button")
    public void atc002_addToCartButton() {
        InventoryPage ip = new InventoryPage(driver);
        String product = "Sauce Labs Backpack";

        int before = ip.getCartBadgeCount();
        ip.addToCart(product);

        Assert.assertEquals(ip.getButtonTextFor(product), "Remove", "Button should change to Remove");
        Assert.assertEquals(ip.getCartBadgeCount(), before + 1, "Cart badge should increment by 1");
    }

    @Test(description = "Remove from Cart")
    public void atc003_removeFromCart() {
        InventoryPage ip = new InventoryPage(driver);
        String product = "Sauce Labs Backpack";

        ip.addToCart(product);
        int afterAdd = ip.getCartBadgeCount();
        Assert.assertTrue(afterAdd > 0, "Precondition failed: cart should have at least 1 item");

        ip.removeFromCart(product);

        Assert.assertEquals(ip.getCartBadgeCount(), afterAdd - 1, "Cart badge should decrement by 1");
        Assert.assertEquals(ip.getButtonTextFor(product), "Add to cart", "Button should change back to Add to cart");
    }

    @Test(description = "Sort products (Price Low-High)")
    public void atc004_sortProducts() {
        InventoryPage ip = new InventoryPage(driver);

        ip.sortBy("Price (low to high)");
        List<Double> prices = ip.getAllDisplayedPrices();

        for (int i = 1; i < prices.size(); i++) {
            Assert.assertTrue(prices.get(i) >= prices.get(i - 1),
                    "Prices are not sorted ascending. Prev=" + prices.get(i - 1) + " Curr=" + prices.get(i));
        }
    }
}
