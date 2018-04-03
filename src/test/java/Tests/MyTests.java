package Tests;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class MyTests {
	
	public WebDriver driver;
	public ExtentReports extent;
	public ExtentTest extenttest; 
	
	@BeforeTest
	public void setExtent()
	{
		extent = new ExtentReports(System.getProperty("user.dir")+ "/test-output/ExtentReport.html",true);
		extent.addSystemInfo("Host Name", "Bhushan Windows");
		extent.addSystemInfo("User Name", "Bhushan");
		extent.addSystemInfo("Environment", "QA");				
	}
	
	@AfterTest
	public void endReport()
	{
		extent.flush();
		extent.close();
	}
	
	public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException
	{
		String dateName = new SimpleDateFormat("yyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		
		String destination = System.getProperty("user.dir") + "FailedTestScreenshot/" + screenshotName + dateName + ".png"; 
		File finalDestination = new File(destination); 
		FileUtils.copyFile(src, finalDestination);
		
		return destination; 
	
	}
	
	@BeforeMethod
	public void setup()
	{
		System.setProperty("webdriver.chrome.driver", "F:\\chromedriver_Updates\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(13, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
		driver.get("http://google.com");

	}
	
	
	@Test
	public void GoogleTest()
	{
		extenttest = extent.startTest("GoogleTest");   // we have to type this for each @Test method
		String title = driver.getTitle();
		Assert.assertEquals(title, "Google");
	}
	
	@Test
	public void clickLink()
	{
		extenttest = extent.startTest("clickLink");   
		driver.findElement(By.linkText("Images22")).click();
		
	}
	
	
	@AfterMethod
	public void tearDown(ITestResult result) throws IOException
	{
		if(result.getStatus()== ITestResult.FAILURE)
		{
			extenttest.log(LogStatus.FAIL, "TEST CASES FAILED ARE " + result.getName());
			extenttest.log(LogStatus.FAIL, "TEST CASES FAILED ARE " + result.getThrowable());
			
			String screenshotPath = MyTests.getScreenshot(driver, result.getName());
			extenttest.log(LogStatus.FAIL, extenttest.addScreenCapture(screenshotPath));
		}
		else if(result.getStatus()== ITestResult.SKIP)
		{
			extenttest.log(LogStatus.SKIP, "TEST CASES SKIPPED ARE " + result.getName());
			
		}
		
		extent.endTest(extenttest);		
		driver.quit();
	}
	
//	@Test
//	public void test2()
//	{
//		Assert.assertEquals(true, true);
//	}
//	
//	@Test
//	public void test3()
//	{
//		Assert.assertEquals(true, true);
//	}
	

}
