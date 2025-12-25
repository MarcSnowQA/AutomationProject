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

    @Test(description = "Products page is displayed", testName = "Display of products page")
    public void atc001_inventoryPageLoads() {
        InventoryPage ip = new InventoryPage(driver);
        Assert.assertTrue(ip.isLoaded(), "Inventory not visible");
        Assert.assertEquals(ip.getProductsCount(), 6, "Expected 6 products on inventory page");
    }

    @Test(description = "Sorting products by prices from low to high", testName = "Sorting by prices")
    public void atc002_productSorting_lowToHigh() {
        InventoryPage ip = new InventoryPage(driver);

        ip.sortBy("Price (low to high)");
        List<Double> prices = ip.getAllDisplayedPrices();

        // assert ascending
        for (int i = 1; i < prices.size(); i++) {
            Assert.assertTrue(prices.get(i) >= prices.get(i - 1),
                    "Prices are not sorted ascending. Prev=" + prices.get(i - 1) + " Curr=" + prices.get(i));
        }
    }

    @Test(description = "Product added to cart successfully and badge updates", testName = "Add product to cart")
    public void atc003_addToCart() {
        InventoryPage ip = new InventoryPage(driver);
        String product = "Sauce Labs Backpack";

        int before = ip.getCartBadgeCount();
        ip.addToCart(product);

        Assert.assertEquals(ip.getCartBadgeCount(), before + 1, "Cart badge should increment by 1");
        Assert.assertEquals(ip.getButtonTextFor(product), "Remove", "Button should change to Remove");
    }

    @Test(description = "Product removed from cart successfully", testName = "Remove product from cart")
    public void atc004_removeFromCart() {
        InventoryPage ip = new InventoryPage(driver);
        String product = "Sauce Labs Backpack";

        // ensure item is in cart first (test isolation)
        ip.addToCart(product);
        int afterAdd = ip.getCartBadgeCount();
        Assert.assertTrue(afterAdd > 0, "Precondition failed: cart should have at least 1 item");

        ip.removeFromCart(product);

        Assert.assertEquals(ip.getButtonTextFor(product), "Add to cart", "Button should change back to Add to cart");
        Assert.assertEquals(ip.getCartBadgeCount(), afterAdd - 1, "Cart badge should decrement by 1");
    }
}
