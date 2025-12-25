package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pageobjects.InventoryPage;
import pageobjects.LoginPage;
import pageobjects.ProductPage;

public class ProductTest extends BaseTest {

    @BeforeMethod
    public void login() {
        new LoginPage(driver).login("standard_user", "secret_sauce");
        Assert.assertTrue(new InventoryPage(driver).isLoaded(), "Inventory page should load after login");
    }

    @Test(description = "Open product details and verify it loads", testName = "Verifying opened product details")
    public void atc001_openProductDetails() {
        InventoryPage ip = new InventoryPage(driver);

        String expectedProduct = "Sauce Labs Backpack";
        ProductPage pdp = ip.openProduct(expectedProduct);

        Assert.assertTrue(pdp.isLoaded(), "Product detail page didn't load!");
        Assert.assertEquals(pdp.getProductTitle(), expectedProduct, "Wrong product was opened");
    }
    
    @Test
    public void atc002_addRemoveFromProductPage_updatesBadge() {
        InventoryPage ip = new InventoryPage(driver);
        ProductPage pdp = ip.openProduct("Sauce Labs Backpack");

        int before = pdp.getCartBadgeCount();
        pdp.addToCart();
        Assert.assertEquals(pdp.getCartBadgeCount(), before + 1);

        pdp.removeFromCart();
        Assert.assertEquals(pdp.getCartBadgeCount(), before);
    }

    @Test
    public void atc003_backToProducts_returnsToInventory() {
        InventoryPage ip = new InventoryPage(driver);
        ProductPage pdp = ip.openProduct("Sauce Labs Backpack");

        pdp.backToInventory();
        Assert.assertTrue(new InventoryPage(driver).isLoaded(), "Should return to inventory page");
    }

}
