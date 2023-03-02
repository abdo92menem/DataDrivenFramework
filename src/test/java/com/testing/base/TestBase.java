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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import com.testing.utilities.ExcelUtility;

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
	public static ExcelUtility excel = new ExcelUtility("./src\\test\\resources\\excel\\testdata.xlsx");
	public static WebDriverWait wait;
	public static Alert alert;
	
	@BeforeSuite
	public void setUp() throws IOException {
		
		if (driver == null) {
			
			fis = new FileInputStream("./src\\test\\resources\\properties\\Config.properties");
			config.load(fis);
			log.debug("Config file loaded !!!");
			
			fis = new FileInputStream("./src\\test\\resources\\properties\\OR.properties");
			OR.load(fis);
			log.debug("OR file loaded !!!");
			
		}
		
		if (config.getProperty("browser").equals("firefox")) {
			
			System.out.println("Firefox is launched");
			System.setProperty("webdriver.gecko.driver", "./src\\test\\resources\\executable\\geckodriver.exe");
			driver = new FirefoxDriver();
			log.debug("Firefox Launched !!!");
			
		} else if (config.getProperty("browser").equals("chrome")) {
			
			System.out.println("Chrome is launched");
			System.setProperty("webdriver.chrome.driver", "./src\\test\\resources\\executable\\chromedriver.exe");
			driver = new ChromeDriver();
			log.debug("Chrome Launched !!!");
			
		} else if (config.getProperty("browser").equals("edge")) {
			
			System.out.println("Edge is launched");
			System.setProperty("webdriver.edge.driver", "./src\\test\\resources\\executable\\msedgedriver.exe");
			driver = new ChromeDriver();
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
	
	public boolean isElementPresent(By by) {
		
		try {
						
			driver.findElement(by);
			return true;
			
		} catch (NoSuchElementException e) {
			
			return false;
		}
	}
}
