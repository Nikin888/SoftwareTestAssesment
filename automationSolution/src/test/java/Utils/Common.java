package Utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

//import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

/**
 * Define Common Webdriver
 * 
 * @author whimstay
 */
public class Common {

	Date date = new Date();
	protected Wait<WebDriver> wait;
	protected WebDriver driver;

	public Common(WebDriver driver) {

		this.driver = driver;
	}

	/**
	 * Find web-element for given locator.
	 * 
	 * @param elementName
	 * @return
	 */
	public WebElement findElement(String elementName) {

		String locator;

		locator = elementName;

		int count = 0;
		while (count < 4) {
			try {
				if (locator.startsWith("link=") || locator.startsWith("LINK=")) {
					locator = locator.substring(5); // remove "link=" from
													// locator
					try {
						if (locator.contains(" "))
							return driver.findElement(By.partialLinkText(locator));

						return driver.findElement(By.linkText(locator));
					} catch (Exception e) {
						return null;
					}
				}
				if (locator.startsWith("id=")) {
					locator = locator.substring(3); // remove "id=" from locator
					try {
						return driver.findElement(By.id(locator));
					} catch (Exception e) {
						return null;
					}
				} else if (locator.startsWith("//")) {
					try {
						return driver.findElement(By.xpath(locator));
					} catch (Exception e) {
						return null;
					}
				} else if (locator.startsWith("css=")) {

					locator = locator.substring(4); // remove "css=" from
													// locator
					try {
						return driver.findElement(By.cssSelector(locator));
					} catch (Exception e) {
						return null;
					}
				} else if (locator.startsWith("name=")) {

					locator = locator.substring(5); // remove "name=" from
													// locator
					try {
						return driver.findElement(By.name(locator));
					} catch (Exception e) {
						return null;
					}
				} else {
					try {
						return driver.findElement(By.id(locator));
					} catch (Exception e) {
						return null;
					}

				}
			} catch (StaleElementReferenceException e) {
				e.toString();

				count = count + 1;
				// System.out.println("Trying["+
				// count+"] to recover from a stale element :" +
				// e.getMessage());

			}
			count = count + 4;
		}

		return null;

	}

	public void dismissAlert() {

		pause(4);
		Alert alert = driver.switchTo().alert();
		alert.dismiss();

	}

	public String acceptAlert1() {

		pause(4);
		Alert alert = driver.switchTo().alert();
		String alerttext = alert.getText();

		alert.accept();
		return alerttext;
	}

	/**
	 * Refresh Page
	 * 
	 */
	public void refreshPage() {

		driver.navigate().refresh();
	}

	/**
	 * Current Date to Tomorrow's Date
	 * 
	 * @throws ParseException
	 * 
	 */
	public String currentDate() throws ParseException {

		Date dateNow = new Date();
		SimpleDateFormat dateformatyyyyMMdd = new SimpleDateFormat("yyyy-MM-dd"); // Date
																					// Format
		String date_to_string = dateformatyyyyMMdd.format(dateNow);
		String untildate = date_to_string; // Current Date
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format

		return untildate;

	}

	/**
	 * Clicks on visible or not visible element.
	 * 
	 * @param element Web element.
	 */
	public void jsClick(WebElement element) {
		((JavascriptExecutor) driver).executeScript("return arguments[0].click();", element);
		// this.waitForAjax("0");
	}

	/**
	 * TomorrowDate
	 * 
	 * @return
	 * @throws ParseException
	 */
	public String tomorrowDate() throws ParseException {

		Date dateNow = new Date();
		SimpleDateFormat dateformatyyyyMMdd = new SimpleDateFormat("yyyy-MM-dd"); // Date
																					// Format
		String date_to_string = dateformatyyyyMMdd.format(dateNow);
		String untildate = date_to_string;// Current Date
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance(); // Get Calendar Instance
		cal.setTime(dateFormat.parse(untildate));

		// Tomorrow's Date
		cal.add(Calendar.DATE, 1); // Date Increase by One
		String convertedDate = dateFormat.format(cal.getTime());
		System.out.println("Date increase by one :: " + convertedDate);

		return convertedDate;

	}

	/**
	 * Gets current time in the following format Month, Date, Hours, Minutes,
	 * Seconds, Millisecond.
	 * 
	 * @return Current date.
	 */
	public String getCurrentTimeStampString() {

		Date date = new Date();

		SimpleDateFormat sd = new SimpleDateFormat("MMddHHmmssSS");
		TimeZone timeZone = TimeZone.getDefault();
		Calendar cal = Calendar.getInstance(new SimpleTimeZone(timeZone.getOffset(date.getTime()), "GMT"));
		sd.setCalendar(cal);
		return sd.format(date);
	}

	/**
	 * Log given message to Reporter output.
	 * 
	 * @param msg Message/Log to be reported.
	 */
	public void log(String msg) {
		Reporter.log("Step :: " + msg);

	}

	/**
	 * Checks whether the visibility of Element Located
	 * 
	 * @param by
	 * @return
	 */
	public ExpectedCondition<WebElement> visibilityOfElementLocated(final By by) {
		return new ExpectedCondition<WebElement>() {

			public WebElement apply(WebDriver driver) {
				WebElement element = driver.findElement(by);
				return element.isDisplayed() ? element : null;
			}
		};
	}

	/**
	 * Wait up to String locator present
	 * 
	 * @param selector
	 */
	public void waitForElement(String selector) {
		wait = new WebDriverWait(driver, 50);
		try {
			wait.until(visibilityOfElementLocated(By.cssSelector(selector)));
		} catch (TimeoutException e) {
		}
	}

	/**
	 * Wait up to By element present
	 * 
	 * @param element
	 */
	public void waitForElement(By element) {
		wait = new WebDriverWait(driver, 30);
		try {
			wait.until(visibilityOfElementLocated(element));
		} catch (TimeoutException e) {
		}
	}

	/**
	 * Generates random symbols;
	 * 
	 * @param length Length of the generated symbols.
	 * 
	 * @return StringBuffer object.
	 */
	public static String generateRandomChars(int length) {
		String total = "iokijfmnbxcvfrpqsdfgvcxzdferiuytjndifur";
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			char _char = total.charAt((int) (Math.random() * 100) % total.length());
			buf.append(_char);
		}
		return buf.toString();
	}

	public String getemailString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 10) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	/**
	 * Generate Random Number in Length
	 * 
	 * @param length
	 * @return
	 */
	public static int GenerateRandomNumber(int length) {

		Random rand = new Random();
		int num = rand.nextInt(length);
		int numNoRange = rand.nextInt();
		return numNoRange;

	}

//	public String generateRandomNumber(int length){
//		  return RandomStringUtils.randomNumeric(length);
//		 }

	/**
	 * Generate Random Number1
	 * 
	 * @param
	 * @return
	 */
	public static String generateRandomNumber1() {

		String result = null;
		for (int i = 1; i < 10; i++) {
			int Random = (int) (Math.random() * 100000);
			result = String.valueOf(Random);
		}
		return result;

		// return RandomStringUtils.randomNumeric(length);
	}

	/**
	 * Get text in a given element.
	 * 
	 * @param elementName Locator of element.
	 * 
	 * @return text in given element.
	 */
	public String getText(String elementName) {

		String text;

		try {
			text = this.findElement(elementName).getText();

		} catch (Exception e) {

			text = "Element was not found";
		}

		return text;
	}

	public void open(String url) {

		driver.get(url);
	}

	/**
	 * Get value of given element dynamically.
	 * 
	 * @param locator Locator of element.
	 * 
	 * @return Dynamic value.
	 */
	public String getValue(String locator) {

		return this.findElement(locator).getAttribute("value");
	}

	/**
	 * Checks if given element is being displayed on page.
	 * 
	 * @param elementName Locator of element. test the element present if mot
	 *                    contains the elemennts is ok
	 * @return true if displayed else false.
	 */
	public boolean isElementDisplayed(String elementName) {

		WebElement webElement;
		try {
			webElement = this.findElement(elementName);
			return webElement.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Wait till given element is present.
	 * 
	 * @param locator Locator of element.
	 */
	public void waitForConditionIsElementPresent(String locator) {

		for (int second = 0;; second++) {
			if (second >= 10) {
				break;
			}

			try {
				if (isElementPresent(locator))
					break;
			} catch (Throwable failure) {
			}

			pause(1000);
		}

	}

	/**
	 * Checks if element loaded in browser memory.
	 * 
	 * @param locator Locator of element.
	 * @return true if loaded else false.
	 */
	public boolean isElementPresent(String locator) {

		WebElement webElement = this.findElement(locator);
		if (webElement != null) {
			return true;
		} else {

			return false;
		}
	}

	public boolean existsElement(String xpath) {
		try {
			driver.findElement(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}

	/**
	 * Assertion to check that given element is not present.
	 * 
	 * @param locator Locator of element.
	 */
	public void assertElementNotPresent(String locator) {

		Assert.assertFalse(isElementPresent(locator));
	}

	/**
	 * Assertion to check that given element is present.
	 * 
	 * @param locator Locator of element.
	 */
	public void assertElementPresent(String locator) {

		Assert.assertTrue(isElementPresent(locator));
	}

	/**
	 * Pauses for given seconds.
	 * 
	 * @param secs
	 */
	public void pause(int secs) {
		try {
			Thread.sleep(secs * 200);
		} catch (InterruptedException interruptedException) {

		}
	}

	/**
	 * Clears and type new value into given text-box.
	 * 
	 * @param locator
	 * 
	 * @param string  New text/value.
	 */
	public void type(String locator, String string) {

		this.findElement(locator).clear();
		this.findElement(locator).sendKeys(string);

	}

	public void ZoomOut() throws AWTException, InterruptedException {
		new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//button[@class='close-button close-button-ctm-time btn btn-primary']"))).click();
		Thread.sleep(3000);
		Robot robo = new Robot();
		robo.keyPress(KeyEvent.VK_META);
		robo.keyPress(KeyEvent.VK_MINUS);
		Thread.sleep(3000);
	}

	public void Click(String Element) throws InterruptedException {
		Thread.sleep(2000);
		new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Element))).click();
		Thread.sleep(2000);
	}

	public void ScrollDownPage() throws InterruptedException {

		Thread.sleep(2000);
		JavascriptExecutor j = (JavascriptExecutor) driver;
		j.executeScript("window.scrollBy(0,800)");
		System.out.println("Step :: Scrolled Down");
		log("Step :: Scrolled Down");
		Thread.sleep(2000);

	}

	public void ScrollUpPage() throws InterruptedException {

		Thread.sleep(2000);
		JavascriptExecutor j = (JavascriptExecutor) driver;
		j.executeScript("window.scrollBy(800,0)");
		System.out.println("Step :: Scrolled Up");
		log("Step :: Scrolled Up");
		Thread.sleep(2000);

	}
}
