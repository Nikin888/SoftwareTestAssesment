package Test;

import java.awt.AWTException; 
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import Utils.BasePage;

public class TestSearchFunctionality extends BasePage {
	
	@Test
	public void testSearchFunctionality() throws InterruptedException{
		
		String URL = "https://www.makemytrip.com/";
		driver.get(URL);
        driver.findElement(By.xpath("//input[@id='fromCity']")).sendKeys("London,United Kingdom");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//li[@id='react-autowhatever-1-section-0-item-0']")).click();
        driver.findElement(By.xpath("//input[@id='toCity']")).sendKeys("Paris");
        Thread.sleep(2000);
        driver.findElement(By.xpath("//li[@id='react-autowhatever-1-section-0-item-0']")).click();
        driver.findElement(By.xpath("//p[text()='Tap to add a return date for bigger discounts']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//html")).click();
        driver.findElement(By.xpath("//div[@class='fsw_inputBox flightTravllers inactiveWidget ']")).click();
        driver.findElement(By.xpath("//li[@data-cy='adults-2']")).click();
        driver.findElement(By.xpath("//button[text()='APPLY']")).click();
        driver.findElement(By.xpath("//html")).click();

        driver.findElement(By.xpath("//a[text()='Search']")).click();
        WebDriverWait w = new WebDriverWait(driver,3);
        w.until(ExpectedConditions.presenceOfElementLocated (By.xpath("//p[@class='font24 blackFont whiteText appendBottom20 journey-title']")));
		
	}
	
		
	
}
