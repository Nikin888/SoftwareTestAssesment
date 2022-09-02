package Test;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import static org.testng.Assert.assertEquals;

import java.awt.AWTException;
import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.annotations.Test;
import Utils.BasePage;

public class TestCheckoutFunctionality extends BasePage {

	@Test
	public void testCheckoutFunctionality() throws InterruptedException {

		String URL = "https://www.saucedemo.com/";
		driver.get(URL);

		driver.findElement(By.xpath("//input[@id='user-name']")).sendKeys("standard_user");
		driver.findElement(By.xpath("//input[@id='password']")).sendKeys("secret_sauce");
		driver.findElement(By.xpath("//input[@id='login-button']")).click();
		Thread.sleep(2000);
		String item1Name = driver.findElement(By.xpath("//div[@class='inventory_list']//a[@id='item_4_title_link']"))
				.getText();
		driver.findElement(By.xpath("//button[text()='Add to cart']")).click();
		String item2Name = driver.findElement(By.xpath("//div[@class='inventory_list']//a[@id='item_0_title_link']"))
				.getText();

		driver.findElement(By.xpath("//button[text()='Add to cart']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//a[@class='shopping_cart_link']")).click();

		String cartItem1name = driver.findElement(By.xpath("//a[@id='item_4_title_link']")).getText();
		String cardItemname = driver.findElement(By.xpath("//a[@id='item_0_title_link']")).getText();
		AssertJUnit.assertEquals(item1Name, cartItem1name);
		AssertJUnit.assertEquals(item2Name, cardItemname);
		driver.findElement(By.xpath("//button[@id='checkout']")).click();
		driver.findElement(By.xpath("//input[@id='first-name']")).sendKeys("Nikin");
		driver.findElement(By.xpath("//input[@id='last-name']")).sendKeys("patel");
		driver.findElement(By.xpath("//input[@id='postal-code']")).sendKeys("382721");
		driver.findElement(By.xpath("//input[@id='continue']")).click();

		String item1Price = driver
				.findElement(By.xpath(
						"//div[@class='cart_list']//div[@class='cart_item'][1]//div[@class='inventory_item_price']"))
				.getText();
		String item2Price = driver
				.findElement(By.xpath(
						"//div[@class='cart_list']//div[@class='cart_item'][2]//div[@class='inventory_item_price']"))
				.getText();

		String sum = item1Price + item2Price;
		String itemPrice = driver.findElement(By.xpath("//div[@class='summary_subtotal_label']")).getText();
		System.out.println("total price::" + itemPrice);
		driver.findElement(By.xpath("//button[@id='finish']")).click();
		String confirmationnTxt = driver.findElement(By.xpath("//h2")).getText();
		AssertJUnit.assertEquals(confirmationnTxt, "THANK YOU FOR YOUR ORDER");

	}

}
