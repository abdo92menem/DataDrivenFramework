package com.testing.testcases;

import java.util.Hashtable;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.testing.base.TestBase;
import com.testing.utilities.TestUtil;

public class OpenAccountTest extends TestBase {
	
	@Test(dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void openAccountTest(Hashtable<String, String> data) throws InterruptedException {
		
		log.debug("Inside Open Account Test");
		
		click("openAccount_CSS");
		select("customer_CSS", data.get("Customer"));
		select("currency_CSS", data.get("Currency"));
		click("process_CSS");
		
		alert = wait.until(ExpectedConditions.alertIsPresent());
		Assert.assertTrue(alert.getText().contains(data.get("Alert Text")));
		alert.accept();
		Thread.sleep(2000);
		log.debug("Account Opened Successfully");
	}

}
