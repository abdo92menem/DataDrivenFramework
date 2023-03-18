package com.testing.testcases;


import java.io.IOException;
import java.util.Hashtable;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.testing.base.TestBase;
import com.testing.utilities.TestUtil;

public class AddCustomerTest extends TestBase {
	
	@Test(dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void addCustomerTest(Hashtable<String, String> data) throws InterruptedException {
		
		log.debug("Inside Add Customer Test");
		
		if (!data.get("Run Mode").equalsIgnoreCase("Y")) {
			
			throw new SkipException("Skipping test case as Run Mode = N");
		}
		click("addCustomer_CSS");
		type("firstName_CSS", data.get("First Name"));
		type("lastName_CSS", data.get("Last Name"));
		type("postCode_CSS", data.get("Post Code")); 
		click("addBtn_CSS");
		
		alert = wait.until(ExpectedConditions.alertIsPresent());
		Assert.assertTrue(alert.getText().contains(data.get("Alert Text")));
		alert.accept();
		log.debug("Customer Added Successfully");
		
	}
	
	@DataProvider(name = "CustomerData")
	public Object[][] getData() throws IOException {
		
		log.debug("Inside getData Function");
		
		String sheetName = "AddCustomerTest";
		
		int rows = excel.getRowCount(sheetName);
		int cols = excel.getColumnCount(sheetName);
		
		
		Object[][] data = new Object[rows - 1][cols];
		
		for (int rowNum = 2; rowNum <= rows; rowNum++) {
			
			for (int colNum = 0; colNum < cols; colNum++) {
				
				data[rowNum - 2][colNum] = excel.getCellData(sheetName, colNum, rowNum);
				
			}
			
		}
		
		return data;
		
	}

}
