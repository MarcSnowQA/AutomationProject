package tests;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pageobjects.*;

public class CheckoutTests extends BaseTest {

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

    @Test(description = "Complete order with valid checkout info (1 item)")
    public void atc001_completeOrder1Item() {
        InventoryPage ip = new InventoryPage(driver);
        ip.addToCart("Sauce Labs Backpack");
        ip.openCart();

        CartPage cp = new CartPage(driver);
        CheckoutStepOnePage step1 = cp.clickCheckout();
        
        CheckoutStepTwoPage step2 = step1.continueToOverview("First", "Last", "12345");
        Assert.assertTrue(step2.isLoaded(), "Overview page should load");

        CheckoutCompletePage complete = step2.clickFinish();
        Assert.assertTrue(complete.isLoaded(), "Complete page should load");
        Assert.assertEquals(complete.getHeaderText(), "Thank you for your order!");

        InventoryPage finalIp = complete.clickBackHome();
        Assert.assertTrue(finalIp.isLoaded(), "Back Home returns to Inventory page");
    }

    @Test(description = "Checkout info validation: all fields empty")
    public void atc002_checkoutValidationEmpty() {
        InventoryPage ip = new InventoryPage(driver);
        ip.addToCart("Sauce Labs Backpack");
        ip.openCart();

        CartPage cp = new CartPage(driver);
        CheckoutStepOnePage step1 = cp.clickCheckout();
        
        step1.continueToOverview("", "", "");
        Assert.assertTrue(step1.getErrorMessage().contains("First Name is required"));
    }

    @Test(description = "Remove product from cart and continue shopping")
    public void atc003_removeProductAndContinueShopping() {
        InventoryPage ip = new InventoryPage(driver);
        ip.addToCart("Sauce Labs Backpack");
        ip.openCart();

        CartPage cp = new CartPage(driver);
        cp.removeItem("Sauce Labs Backpack");
        Assert.assertEquals(cp.getCartItemsCount(), 0, "Product disappears from cart");

        InventoryPage inv = cp.clickContinueShopping();
        Assert.assertTrue(inv.isLoaded(), "Continue Shopping returns to Inventory page");
        Assert.assertEquals(getCartBadgeCount(), 0, "Cart badge updates accordingly");
    }

    @Test(description = "Complete order with multiple items (2 items)")
    public void atc004_completeOrder2Items() {
        InventoryPage ip = new InventoryPage(driver);
        ip.addToCart("Sauce Labs Backpack");
        ip.addToCart("Sauce Labs Bike Light");
        ip.openCart();

        CartPage cp = new CartPage(driver);
        Assert.assertEquals(cp.getCartItemsCount(), 2, "Verify both items present");

        CheckoutStepOnePage step1 = cp.clickCheckout();
        CheckoutStepTwoPage step2 = step1.continueToOverview("Test", "User", "00000");
        
        double itemTotal = step2.getItemTotal();
        double expectedItemTotal = 29.99 + 9.99; // Backpack + Bike Light
        Assert.assertEquals(itemTotal, expectedItemTotal, 0.01, "Item Total equals sum of item prices");

        double tax = step2.getTax();
        double total = step2.getTotal();
        Assert.assertEquals(total, itemTotal + tax, 0.01, "Total = Item Total + Tax");

        CheckoutCompletePage complete = step2.clickFinish();
        Assert.assertTrue(complete.isLoaded(), "Checkout Complete page shown");
    }

    @Test(description = "Checkout info validation: missing Last Name")
    public void atc005_checkoutValidationMissingLastName() {
        new InventoryPage(driver).addToCart("Sauce Labs Backpack");
        new InventoryPage(driver).openCart();

        CartPage cp = new CartPage(driver);
        CheckoutStepOnePage step1 = cp.clickCheckout();
        
        step1.continueToOverview("First", "", "12345");
        Assert.assertTrue(step1.getErrorMessage().contains("Last Name is required"));
    }

    @Test(description = "Checkout info validation: missing Postal Code")
    public void atc006_checkoutValidationMissingPostalCode() {
        new InventoryPage(driver).addToCart("Sauce Labs Backpack");
        new InventoryPage(driver).openCart();

        CartPage cp = new CartPage(driver);
        CheckoutStepOnePage step1 = cp.clickCheckout();
        
        step1.continueToOverview("First", "Last", "");
        Assert.assertTrue(step1.getErrorMessage().contains("Postal Code is required"));
    }

    @Test(description = "Checkout Step One: Cancel returns to Cart")
    public void atc007_checkoutStepOneCancel() {
        new InventoryPage(driver).addToCart("Sauce Labs Backpack");
        new InventoryPage(driver).openCart();

        CartPage cp = new CartPage(driver);
        CheckoutStepOnePage step1 = cp.clickCheckout();
        
        cp = step1.clickCancel();
        Assert.assertTrue(cp.isLoaded(), "User is returned to Cart page");
        Assert.assertEquals(cp.getCartItemsCount(), 1, "Previously added items are still present");
    }

    @Test(description = "Checkout Overview: Cancel returns to Inventory")
    public void atc008_checkoutOverviewCancel() {
        new InventoryPage(driver).addToCart("Sauce Labs Backpack");
        new InventoryPage(driver).openCart();

        CartPage cp = new CartPage(driver);
        CheckoutStepOnePage step1 = cp.clickCheckout();
        
        CheckoutStepTwoPage step2 = step1.continueToOverview("First", "Last", "12345");
        InventoryPage ip = step2.clickCancel();
        
        Assert.assertTrue(ip.isLoaded(), "User is returned to Inventory page");
        Assert.assertEquals(getCartBadgeCount(), 1, "Cart badge still reflects items in cart");
    }

    @Test(description = "Checkout Overview: verify price math (3 items)")
    public void atc009_checkoutOverviewVerifyMath3Items() {
        InventoryPage ip = new InventoryPage(driver);
        ip.addToCart("Sauce Labs Backpack"); // 29.99
        ip.addToCart("Sauce Labs Bike Light"); // 9.99
        ip.addToCart("Sauce Labs Bolt T-Shirt"); // 15.99
        ip.openCart();

        CartPage cp = new CartPage(driver);
        CheckoutStepOnePage step1 = cp.clickCheckout();
        
        CheckoutStepTwoPage step2 = step1.continueToOverview("John", "Doe", "90210");
        
        double expectedTotal = 29.99 + 9.99 + 15.99;
        Assert.assertEquals(step2.getItemTotal(), expectedTotal, 0.01, "Item Total equals sum of prices");
        Assert.assertEquals(step2.getTotal(), step2.getItemTotal() + step2.getTax(), 0.01, "Total matches Item Total + Tax");
    }

    @Test(description = "Finish order clears the cart")
    public void atc010_finishOrderClearsCart() {
        InventoryPage ip = new InventoryPage(driver);
        ip.addToCart("Sauce Labs Backpack");
        ip.openCart();

        CartPage cp = new CartPage(driver);
        CheckoutStepOnePage step1 = cp.clickCheckout();
        CheckoutStepTwoPage step2 = step1.continueToOverview("A", "B", "C");
        CheckoutCompletePage complete = step2.clickFinish();
        
        ip = complete.clickBackHome();
        ip.openCart();
        cp = new CartPage(driver);
        
        Assert.assertEquals(cp.getCartItemsCount(), 0, "Cart is empty after completing checkout");
        Assert.assertEquals(getCartBadgeCount(), 0, "Cart badge is 0/hidden");
    }

    @Test(description = "User cannot skip Step One and open Overview directly")
    public void atc011_cannotSkipStepOne() {
        driver.get("https://www.saucedemo.com/checkout-step-two.html");
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-one.html") || driver.getCurrentUrl().contains("inventory.html") || driver.getCurrentUrl().contains("cart.html"), 
            "User is blocked from Overview");
        Assert.assertTrue(driver.findElement(By.cssSelector("[data-test='error']")).isDisplayed(), "Error should be displayed to user");
    }

}
