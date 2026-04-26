package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    @FindBy(id = "user-name")
    private WebElement userName;

    @FindBy(id = "password")
    private WebElement pwd;

    @FindBy(id = "login-button")
    private WebElement loginBtn;

    @FindBy(css = "[data-test='error']")
    private WebElement errMsg;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Performs login operation with the provided credentials.
     * Populates username and password fields, then clicks the login button.
     *
     * @param username the username to input
     * @param password the password to input
     */
    public void login(String username, String password) {
        type(userName, username);
        type(pwd, password);
        click(loginBtn);
    }

    /**
     * Retrieves the text of the error message displayed on the login page.
     *
     * @return String containing the error text, trimmed of whitespace
     */
    public String getErrorMessage() {
        return getText(errMsg);
    }

    /**
     * Checks if the login error message element is currently displayed on the page.
     *
     * @return true if error message is visible, false otherwise
     */
    public boolean isErrorDisplayed() {
        return isDisplayedSafe(errMsg);
    }
}
