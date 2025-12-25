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

    public void login(String username, String password) {
        type(userName, username);
        type(pwd, password);
        click(loginBtn);
        // No sleep here. Tests will assert next state:
        // - inventory page loaded (positive)
        // - error shown (negative)
    }

    public String getErrorMessage() {
        return getText(errMsg); // waits visible + trims
    }

    public boolean isErrorDisplayed() {
        return isDisplayedSafe(errMsg);
    }
}
