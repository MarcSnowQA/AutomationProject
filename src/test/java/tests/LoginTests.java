package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pageobjects.LoginPage;
public class LoginTests extends BaseTest{
@DataProvider(name="invalidLogins")
public Object[][] invalidLogins() {
    return new Object[][]{
        {"locked_out_user", "secret_sauce", "Epic sadface: Sorry, this user has been locked out."},
        {"", "", "Epic sadface: Username is required"},
        {"mark1234", "secret_sauce", "Epic sadface: Username and password do not match any user in this service"},
        {"standard_user", "1234", "Epic sadface: Username and password do not match any user in this service"},
    };
}

@Test(dataProvider="invalidLogins")
public void login_invalid_shouldShowError(String u, String p, String expected) {
    LoginPage lp = new LoginPage(driver);
    lp.login(u, p);

    Assert.assertTrue(lp.isErrorDisplayed(), "Error message should be displayed");
    Assert.assertEquals(lp.getErrorMessage(), expected);
}

@Test
public void atc05_successfulLogin() {
    LoginPage lp = new LoginPage(driver);
    lp.login("standard_user", "secret_sauce");
    Assert.assertTrue(driver.getCurrentUrl().contains("inventory"), "User should land on inventory page");
}
}