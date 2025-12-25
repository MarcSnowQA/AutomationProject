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

    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.visibilityOf(productTitle));
    }

    public boolean isLoaded() {
        try {
            waitUntilLoaded();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getProductTitle() {
        waitUntilLoaded();
        return getText(productTitle);
    }

    public void addToCart() {
        waitUntilLoaded();
        if (addRemoveBtn.getText().trim().equalsIgnoreCase("Add to cart")) {
            click(addRemoveBtn);
        }
    }

    public void removeFromCart() {
        waitUntilLoaded();
        if (addRemoveBtn.getText().trim().equalsIgnoreCase("Remove")) {
            click(addRemoveBtn);
        }
    }

    public int getCartBadgeCount() {
        try {
            return Integer.parseInt(cartBadge.getText().trim());
        } catch (Exception e) {
            return 0; // badge not shown means 0
        }
    }

    public void backToInventory() {
        click(backToInventory);
    }
}
