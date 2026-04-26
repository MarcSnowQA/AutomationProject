package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageobjects.LoginPage;

public class LoginTests extends BaseTest {

    @Test(description = "Login with locked out user")
    public void atc001_loginWithLockedOutUser() {
        LoginPage lp = new LoginPage(driver);
        lp.login("locked_out_user", "secret_sauce");

        Assert.assertTrue(lp.isErrorDisplayed(), "Error message should be displayed");
        Assert.assertTrue(lp.getErrorMessage().contains("Username and password do not match any user in this service"),
                "Expected error message according to spreadsheet not found. Actual: " + lp.getErrorMessage());
    }

    @Test(description = "Empty Fields")
    public void atc002_emptyFields() {
        LoginPage lp = new LoginPage(driver);
        lp.login("", "");

        Assert.assertTrue(lp.isErrorDisplayed(), "Error message should be displayed");
        Assert.assertTrue(lp.getErrorMessage().contains("Username is required"),
                "Expected error message containing 'Username is required' not found. Actual: " + lp.getErrorMessage());
    }

    @Test(description = "Invalid Username")
    public void atc003_invalidUsername() {
        LoginPage lp = new LoginPage(driver);
        lp.login("wrong_user", "secret_sauce");

        Assert.assertTrue(lp.isErrorDisplayed(), "Error message should be displayed");
        Assert.assertTrue(lp.getErrorMessage().contains("Username and password do not match"),
                "Expected error message 'Username and password do not match...' not found. Actual: " + lp.getErrorMessage());
    }

    @Test(description = "Invalid Password")
    public void atc004_invalidPassword() {
        LoginPage lp = new LoginPage(driver);
        lp.login("standard_user", "wrong_pass");

        Assert.assertTrue(lp.isErrorDisplayed(), "Error message should be displayed");
        Assert.assertTrue(lp.getErrorMessage().contains("Username and password do not match"),
                "Expected error message 'Username and password do not match...' not found. Actual: " + lp.getErrorMessage());
    }

    @Test(description = "Successful login")
    public void atc005_successfulLogin() {
        LoginPage lp = new LoginPage(driver);
        lp.login("standard_user", "secret_sauce");
        
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"), "User is redirected to inventory page");
    }
}