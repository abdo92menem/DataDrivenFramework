package com.testing.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.testing.utilities.ExcelUtility;
import com.testing.utilities.TestUtil;

public class TestBase {
	
	   /*
	    * This will contain:
	    * WebDriver
	    * Properties
	    * Logs - log4j jar, .log files, log4j.properties, Logger class
	    * Extent Reports
	    * DB
	    * Excel
	    * Mail
	    * ReportNg
	    * Jenkins
	    */
	
	public static WebDriver driver;
	public static Properties config = new Properties();
	public static Properties OR = new Properties();
	public static FileInputStream fis;
	public static Logger log = LogManager.getLogger("devpinoyLogger");
	public static ExcelUtility excel = new ExcelUtility("./src/test/resources/excel/testdata.xlsx");
	public static WebDriverWait wait;
	public static Alert alert;
	public static ThreadLocal<ExtentTest> testReport = new ThreadLocal<ExtentTest>();
	public static WebElement dropdown;
	public static String browser;
	
	@BeforeSuite
	public void setUp() throws IOException {
		
		if (driver == null) {
			
			fis = new FileInputStream("./src/test/resources/properties/Config.properties");
			config.load(fis);
			log.debug("Config file loaded !!!");
			
			fis = new FileInputStream("./src/test/resources/properties/OR.properties");
			OR.load(fis);
			log.debug("OR file loaded !!!");
			
		}
		
		if ((System.getenv("browser") != null) && !System.getenv("browser").isEmpty()) {
			
			browser = System.getenv("browser");
		} else {
			
			browser = config.getProperty("browser");
		}
		
		config.setProperty("browser", browser);
		
		if (config.getProperty("browser").equals("firefox")) {
			
			System.out.println("Firefox is launched");
			System.setProperty("webdriver.gecko.driver", "./src/test/resources/executable/geckodriver.exe");
			driver = new FirefoxDriver();
			log.debug("Firefox Launched !!!");
			
		} else if (config.getProperty("browser").equals("chrome")) {
			
			ChromeOptions co = new ChromeOptions();
			co.addArguments("--remote-allow-origins=*");
			System.out.println("Chrome is launched");
			System.setProperty("webdriver.chrome.driver", "./src\\test/resources/executable/chromedriver.exe");
			driver = new ChromeDriver(co);
			log.debug("Chrome Launched !!!");
			
		} else if (config.getProperty("browser").equals("edge")) {
			
			System.out.println("Edge is launched");
			System.setProperty("webdriver.edge.driver", "./src/test/resources/executable/msedgedriver.exe");
			driver = new EdgeDriver();
			log.debug("Edge Launched !!!");
			
		}
		
		driver.navigate().to(config.getProperty("testSiteURL"));
		log.debug("Navigeted to: " + config.getProperty("testSiteURL"));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(config.getProperty("implicitWait"))));
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		
	}
	
	@AfterSuite
	public void tearDown() {
		
		if (driver != null) {
			
			driver.quit();
			
		}
		
		log.debug("Test Execution completed !!!");
	}
	
	public static void verifyEquals(String actual, String expected) throws IOException {
		
		try {
			
			Assert.assertEquals(actual, expected);
			
		} catch (Throwable t) {
			
			TestUtil.captureScreenshot();
			testReport.get().fail("<b>" + "<font color=" + "red>" + "Screenshot of failure" + "</font>" + "</b>",
					MediaEntityBuilder.createScreenCaptureFromPath(TestUtil.screenshotName)
							.build());
			
			String failureLogg = "Verification Failed with Exception: " + t.getMessage();
			Markup m = MarkupHelper.createLabel(failureLogg, ExtentColor.RED);
			testReport.get().log(Status.FAIL, m);
		}
	}
	
	public void click(String locator) {
		
		if (locator.endsWith("_CSS")) {
			
			driver.findElement(By.cssSelector(OR.getProperty(locator))).click();
			
		} else if (locator.endsWith("_XPATH")) {
			
			driver.findElement(By.xpath(OR.getProperty(locator))).click();
			
		} else if (locator.endsWith("_ID")) {
			
			driver.findElement(By.id(OR.getProperty(locator))).click();
			
		}
		
		locator = locator.split("_")[0];
		
		testReport.get().log(Status.INFO, "Clicking On: " + locator);
		
	}
	
	public void type(String locator, String value) {
		
		if (locator.endsWith("_CSS")) {
			
			driver.findElement(By.cssSelector(OR.getProperty(locator))).sendKeys(value);
			
		} else if (locator.endsWith("_XPATH")) {
			
			driver.findElement(By.xpath(OR.getProperty(locator))).sendKeys(value);
			
		} else if (locator.endsWith("_ID")) {
			
			driver.findElement(By.id(OR.getProperty(locator))).sendKeys(value);
			
		}
		
		locator = locator.split("_")[0];
		
		testReport.get().log(Status.INFO, "Typing in: " + locator + ". Entererd value as " + value);
		
	}
	
	public void select(String locator, String value) {
		
		if (locator.endsWith("_CSS")) {
			
			dropdown = driver.findElement(By.cssSelector(OR.getProperty(locator)));
			
		} else if (locator.endsWith("_XPATH")) {
			
			dropdown = driver.findElement(By.xpath(OR.getProperty(locator)));
			
		} else if (locator.endsWith("_ID")) {
			
			dropdown = driver.findElement(By.id(OR.getProperty(locator)));
			
		}
		
		Select select = new Select(dropdown);
		select.selectByVisibleText(value);
		
		locator = locator.split("_")[0];
		
		testReport.get().log(Status.INFO, "Selecting from dropdown \"" + locator + "\" value as " + value);
	}
	
	public boolean isElementPresent(By by) {
		
		try {
						
			driver.findElement(by);
			return true;
			
		} catch (NoSuchElementException e) {
			
			return false;
		}
	}
}
