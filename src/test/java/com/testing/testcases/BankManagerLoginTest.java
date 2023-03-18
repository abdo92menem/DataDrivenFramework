package com.testing.testcases;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.testing.base.TestBase;

public class BankManagerLoginTest extends TestBase {
	
	@Test
	public void bankManagerLoginTest() throws InterruptedException, IOException {
		
//		verifyEquals("abc", "xyz");
		log.debug("Inside Login Test");
		click("bankManagerLogin_CSS");
		Thread.sleep(1000);
		
		Assert.assertTrue(isElementPresent(By.cssSelector(OR.getProperty("addCustomer_CSS"))), "Login not successfully");
		
//		log.debug("Login successfully executed");
//		Assert.fail("Login not Successful");
	}
	
}
