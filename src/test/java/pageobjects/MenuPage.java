package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MenuPage extends BasePage {

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartLink;

    // Burger menu button (top-left)
    @FindBy(id = "react-burger-menu-btn")
    private WebElement burgerBtn;

    // Menu close "X"
    @FindBy(id = "react-burger-cross-btn")
    private WebElement burgerCloseBtn;

    // Menu items
    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    @FindBy(id = "reset_sidebar_link")
    private WebElement resetAppStateLink;

    @FindBy(id = "inventory_sidebar_link")
    private WebElement allItemsLink;

    @FindBy(id = "about_sidebar_link")
    private WebElement aboutLink;

    public MenuPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Clicks the shopping cart icon located in the site header.
     */
    public void openCart() {
        click(cartLink);
    }

    /**
     * Expands the left-hand navigation burger menu and waits for options to be visible.
     */
    public void openMenu() {
        click(burgerBtn);
        wait.until(ExpectedConditions.visibilityOf(resetAppStateLink));
    }

    /**
     * Closes the left-hand navigation menu and waits until it is no longer visible.
     */
    public void closeMenu() {
        click(burgerCloseBtn);
        wait.until(ExpectedConditions.invisibilityOf(burgerCloseBtn));
    }

    /**
     * Resets the entire application state (e.g., clears cart) via the burger menu option.
     */
    public void resetAppState() {
        openMenu();
        click(resetAppStateLink);
        closeMenu();
    }

    /**
     * Logs the current user out via the burger menu option.
     */
    public void logout() {
        openMenu();
        click(logoutLink);
    }

    /**
     * Clicks the 'All Items' link inside the burger menu, redirecting to Inventory.
     */
    public void clickAllItems() {
        openMenu();
        click(allItemsLink);
    }

    /**
     * Clicks the 'About' link inside the burger menu, redirecting to saucelabs.com.
     */
    public void clickAbout() {
        openMenu();
        click(aboutLink);
    }

    /**
     * Checks whether the expanded burger menu items are actively displayed.
     *
     * @return true if visible, false otherwise.
     */
    public boolean isMenuVisible() {
        return isDisplayedSafe(logoutLink);
    }
}
