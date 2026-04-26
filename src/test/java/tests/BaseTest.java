package tests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.Utils;

public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = buildChromeOptions();
        driver = new ChromeDriver(options);

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        driver.manage().window().maximize();

        driver.get(Utils.readProperty("url"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        // Screenshot only on failure
        if (result.getStatus() == ITestResult.FAILURE && driver != null) {
            takeScreenshot(result.getMethod().getMethodName());
            attachScreenshotToAllure();
        }
        if (driver != null) {
            driver.quit();
        }
    }

    @io.qameta.allure.Attachment(value = "Screenshot on Failure", type = "image/png")
    public byte[] attachScreenshotToAllure() {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (WebDriverException e) {
            return new byte[0];
        }
    }

    protected ChromeOptions buildChromeOptions() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-features=PasswordLeakDetection,PasswordManagerOnboarding");
        options.addArguments("--no-first-run");
        options.addArguments("--no-default-browser-check");
        return options;
    }

    protected void takeScreenshot(String testName) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destDir = new File("screenshots");
            if (!destDir.exists()) destDir.mkdirs();

            File dest = new File(destDir, testName + "_" + System.currentTimeMillis() + ".png");
            Files.copy(src.toPath(), dest.toPath());
        } catch (IOException | WebDriverException ignored) {
            // don't fail test teardown
        }
    }

    /* -------------------- Data Providers -------------------- */

    @DataProvider(name = "validUsers")
    public Object[][] validUsers() {
        return new Object[][]{
                {"standard_user", "secret_sauce"},
                {"problem_user", "secret_sauce"},
                {"performance_glitch_user", "secret_sauce"}
        };
    }

    @DataProvider(name = "invalidUsers")
    public Object[][] invalidUsers() {
        return new Object[][]{
                {"locked_out_user", "secret_sauce", "locked out"},
                {"wrong_user", "secret_sauce", "do not match"},
                {"standard_user", "wrong_pass", "do not match"},
                {"", "", "Username is required"}
        };
    }
}
