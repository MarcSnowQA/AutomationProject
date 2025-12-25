package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MenuPage extends BasePage{
	
	@FindBy(css = ".shopping_cart_link")
	private WebElement cartLink;
	
	

	public MenuPage(WebDriver driver) {
		super(driver);
	}
	
	public void openCart() {
		click(driver.findElement(By.cssSelector(".shopping_cart_link")));
	}

}
