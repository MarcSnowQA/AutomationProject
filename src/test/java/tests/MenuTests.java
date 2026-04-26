package tests;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pageobjects.*;

public class MenuTests extends BaseTest {

    @BeforeMethod
    public void login() {
        new LoginPage(driver).login("standard_user", "secret_sauce");
        Assert.assertTrue(new InventoryPage(driver).isLoaded(), "Inventory page should load after login");
    }

    private int getCartBadgeCount() {
        try {
            return Integer.parseInt(driver.findElement(By.cssSelector(".shopping_cart_badge")).getText().trim());
        } catch (Exception e) {
            return 0;
        }
    }

    @Test(description = "Open burger menu")
    public void menu001_openBurgerMenu() {
        MenuPage mp = new MenuPage(driver);
        mp.openMenu();
        Assert.assertTrue(mp.isMenuVisible(), "Menu sidebar opens; menu links are visible");
    }

    @Test(description = "Close burger menu with X")
    public void menu002_closeBurgerMenuWithX() {
        MenuPage mp = new MenuPage(driver);
        mp.openMenu();
        mp.closeMenu();
        Assert.assertFalse(mp.isMenuVisible(), "Menu sidebar closes");
    }

    @Test(description = "Open cart from header icon")
    public void menu003_openCartFromHeaderIcon() {
        MenuPage mp = new MenuPage(driver);
        mp.openCart();
        CartPage cp = new CartPage(driver);
        Assert.assertTrue(cp.isLoaded(), "User navigates to Cart page");
    }

    @Test(description = "All Items link returns to Inventory")
    public void menu004_allItemsReturnsToInventory() {
        MenuPage mp = new MenuPage(driver);
        mp.openCart(); // Go somewhere else first
        mp.clickAllItems();
        InventoryPage ip = new InventoryPage(driver);
        Assert.assertTrue(ip.isLoaded(), "User navigates to Inventory page");
    }

    @Test(description = "Logout from burger menu")
    public void menu005_logout() {
        MenuPage mp = new MenuPage(driver);
        mp.logout();
        Assert.assertTrue(driver.getCurrentUrl().contains("saucedemo.com") && !driver.getCurrentUrl().contains("inventory"), "User is logged out");
    }

    @Test(description = "Reset App State clears cart badge")
    public void menu006_resetAppStateClearsCart() {
        InventoryPage ip = new InventoryPage(driver);
        ip.addToCart("Sauce Labs Backpack");
        Assert.assertEquals(getCartBadgeCount(), 1, "Verify cart badge shows count > 0");

        ip.resetAppState();
        Assert.assertEquals(getCartBadgeCount(), 0, "Cart badge becomes 0/hidden");
    }

    @Test(description = "Reset App State keeps user logged in")
    public void menu007_resetAppStateKeepsUserLoggedIn() {
        InventoryPage ip = new InventoryPage(driver);
        ip.resetAppState();
        Assert.assertTrue(ip.isLoaded(), "User remains logged in");
    }

    @Test(description = "Reset App State resets 'Remove' buttons back to 'Add to cart'")
    public void menu008_resetAppStateResetsButtons() {
        InventoryPage ip = new InventoryPage(driver);
        ip.addToCart("Sauce Labs Backpack");
        Assert.assertEquals(ip.getButtonTextFor("Sauce Labs Backpack"), "Remove");

        ip.resetAppState();
        Assert.assertEquals(ip.getButtonTextFor("Sauce Labs Backpack"), "Add to cart", "Product's button changes back to Add to cart");
    }

    @Test(description = "About link opens Sauce Labs page")
    public void menu009_aboutLinkOpensSauceLabs() {
        MenuPage mp = new MenuPage(driver);
        mp.clickAbout();
        Assert.assertTrue(driver.getCurrentUrl().contains("saucelabs.com"), "User navigates to Sauce Labs website");
    }

    @Test(description = "Menu open/close does not break page actions")
    public void menu010_menuOpenCloseDoesNotBreakActions() {
        InventoryPage ip = new InventoryPage(driver);
        ip.openMenu();
        ip.closeMenu();
        ProductPage pp = ip.openProduct("Sauce Labs Backpack");
        Assert.assertTrue(pp.isLoaded(), "Page still works: product details page opens normally");
    }

    @Test(description = "Menu works on Cart page")
    public void menu011_menuWorksOnCartPage() {
        MenuPage mp = new MenuPage(driver);
        mp.openCart();
        CartPage cp = new CartPage(driver);
        cp.clickAllItems();
        Assert.assertTrue(new InventoryPage(driver).isLoaded(), "User navigates to Inventory page successfully");
    }

    @Test(description = "Menu works on Product Details page")
    public void menu012_menuWorksOnProductDetailsPage() {
        InventoryPage ip = new InventoryPage(driver);
        ProductPage pp = ip.openProduct("Sauce Labs Backpack");
        pp.clickAllItems();
        Assert.assertTrue(new InventoryPage(driver).isLoaded(), "User navigates to Inventory page successfully");
    }
}
