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

    @Test(description = "Open product details")
    public void atc001_openProductDetails() {
        InventoryPage ip = new InventoryPage(driver);

        String expectedProduct = "Sauce Labs Backpack";
        ProductPage pdp = ip.openProduct(expectedProduct);

        Assert.assertTrue(pdp.isLoaded(), "Product detail page didn't load!");
        Assert.assertEquals(pdp.getProductTitle(), expectedProduct, "Wrong product was opened");
    }
    
    @Test(description = "Add and remove item from Cart")
    public void atc002_addAndRemoveItemFromCart() {
        InventoryPage ip = new InventoryPage(driver);
        ProductPage pdp = ip.openProduct("Sauce Labs Backpack");

        int before = pdp.getCartBadgeCount();
        pdp.addToCart();
        Assert.assertEquals(pdp.getCartBadgeCount(), before + 1, "Product is added to cart, cart counter is increased");

        pdp.removeFromCart();
        Assert.assertEquals(pdp.getCartBadgeCount(), before, "Product is removed from cart, cart counter is decreased");
    }

    @Test(description = "Navigate back to products")
    public void atc003_navigateBackToProducts() {
        InventoryPage ip = new InventoryPage(driver);
        ProductPage pdp = ip.openProduct("Sauce Labs Backpack");

        pdp.backToInventory();
        Assert.assertTrue(new InventoryPage(driver).isLoaded(), "User is redirected back to the inventory page");
    }

}
