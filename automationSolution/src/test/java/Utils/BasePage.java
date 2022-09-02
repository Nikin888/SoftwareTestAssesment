package Utils;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

public class BasePage {

	static Properties configProperties = null;
	static Properties xpathProperties = null;

	protected WebDriver driver;
	private Common common = new Common(driver);
	public static String currentTest; // current running test
	protected String seleniumHub; // Selenium hub IP
	protected String seleniumHubPort; // Selenium hub port
	protected String targetBrowser; // Target browser
	protected static String test_data_folder_path = null;
	// screen-shot folder
	protected static String screenshot_folder_path = null;
	protected static Logger logger = Logger.getLogger("testing");
	@BeforeMethod(alwaysRun = true)
	public void setUp(Method method) throws MalformedURLException, FileNotFoundException {
		currentTest = method.getName(); // get Name of current test.

		String SCREENSHOT_FOLDER_NAME = "screenshots";
		String TESTDATA_FOLDER_NAME = "test_data";

		test_data_folder_path = new File(TESTDATA_FOLDER_NAME).getAbsolutePath();
		screenshot_folder_path = new File(SCREENSHOT_FOLDER_NAME).getAbsolutePath();

		DesiredCapabilities capability = null;
		String driverPath = getPropertyValue("driverPath");
		String browser = getPropertyValue("browser");
		String headless = getPropertyValue("headless");

// 			Mac Path
		System.setProperty("webdriver.chrome.driver", driverPath);
    
//			Windowd Path  
//			System.setProperty("webdriver.chrome.driver",
//					"C://Users//baps//Documents//chromedriver.exe");

// 			THis for windows
//			ChromeOptions chromeOptions = new ChromeOptions();

		// this is for headless browser code
//			chromeOptions.addArguments("headless");

//			driver = new ChromeDriver(chromeOptions);

//			this for mac to open chrome
		if (browser.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver", driverPath);
			// WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			if (headless.equals("true")) {
				options.addArguments("headless");
				// options.addArguments("--headless");
				//options.addArguments("window-size=1536x768");
				// options.addArguments("--headless", "window-size=1024,768", "--no-sandbox");
				driver.manage().window().maximize();
			}

			driver = new ChromeDriver(options);
		} else if (browser.equals("firefox")) {
			System.setProperty("webdriver.gecko.driver", driverPath);
			// WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}
		if (!headless.equals("true")) {
			driver.manage().window().maximize();
			// driver.manage().window().fullscreen();
		}

		driver.manage().window().maximize();
	}

	protected Properties getConfigProperties() {
		if (configProperties == null) {
			configProperties = this.loadProperties(
					Paths.get("src/test/java/Config").toAbsolutePath().normalize().toString() + "//config.properties");

		}
		return configProperties;
	}

	protected String getPropertyValue(String value) {
		Properties properties = getConfigProperties();
		return properties.getProperty(value);
	}

	private Properties loadProperties(String filePath) {
		File file = new File(filePath);
		FileInputStream fileInput = null;

		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Properties properties = new Properties();

		try {
			properties.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return properties;
	}

	/**
	 * After Method {TearDown}
	 *
	 * @param testResult
	 */
	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult testResult) {
		try {

			String testName = testResult.getName();

			if (!testResult.isSuccess()) {

				/* Print test result to Jenkins Console */
				System.out.println();
				System.out.println("TEST FAILED - " + testName);
				System.out.println();
				// System.out.println("ERROR MESSAGE: "
				// + testResult.getThrowable());
				System.out.println("\n");
				Reporter.setCurrentTestResult(testResult);

				/* Make a screenshot for test that failed */
				// Create refernce of TakesScreenshot
				TakesScreenshot ts = (TakesScreenshot) driver;
				File source = ts.getScreenshotAs(OutputType.FILE);
				try {
					FileHandler.copy(source, new File("./Screenshots/" + testResult.getName() + ".png"));
					System.out.println("Screenshot taken");
				}

				catch (Exception e) {
					System.out.println("Exception while taking screenshot " + e.getMessage());
				}

				String screenshotName = common.getCurrentTimeStampString() + testName;
				Reporter.log("<br> <b>Please look to the screenshot - </b>");
			} else {
				System.out.println("TEST PASSED - " + testName + "\n"); // Print
				// test
				// result
				// to
				// Jenkins
				// Console
			}

			driver.manage().deleteAllCookies();
			driver.quit();

		} catch (Throwable throwable) {

		}
	}

}
