package automation;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import helper.DownloadVerifier;

public class BaseTestScript 
{
	protected Logger logger = Logger.getLogger(this.getClass());
	protected String line = "=================================================================";
	protected JavascriptExecutor jse;
	public static WebDriver selenium= null;
	protected static String browser;
	public static String BASE_PROJECT;
	protected static String username;
	protected static String password;
	public static String APPLICATION_URL = "";
	protected static String IP;
	private String location;
	public static String screenshotPath;

	public BaseTestScript()
	{
		PropertyConfigurator.configure("log4j.properties");
	}
	
	@BeforeSuite
	public void beforeSuite()
	{
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("dd MMM YYYY HH:mm:ss aa");
		Reporter.log(line);
		Reporter.log("Start Time: " + dateFormat.format(cal.getTime()));
		Reporter.log(line);
		readScreenShotPath();
	}
	
	@BeforeTest
	protected void beforeTest() throws IOException
	{
		getStaticData();
		try
		{         
			if (("ChromeDriver").equals(browser))
			{
				File driverPath = new File(BASE_PROJECT + "/src/resource/64BitChromeDriver/chromedriver.exe");
				System.setProperty("webdriver.chrome.driver", driverPath.getAbsolutePath());
			   
				Map<String, Object> prefs = new HashMap<String, Object>();     // Create object of HashMap Class
				//0- Default 1- Allow  2- Block
				prefs.put("profile.content_settings.pattern_pairs.*.multiple-automatic-downloads", 1 ); //download multiple files
				 
				ChromeOptions options= new ChromeOptions();    // Create object of ChromeOption class
				
				
				options.setExperimentalOption("prefs",prefs);  // Set the experimental option
				options.addArguments("disable-infobars");      //disable automation info bar
				options.addArguments("--start-maximized");     //maximize the browser when you launch it for the first time
				options.addArguments("--disable -extensions");   //Disable Notifications
				
				selenium=new ChromeDriver(options);
			}
			else if(("FirefoxDriver").equals(browser))
			{
				File driverPath = new File(BASE_PROJECT + "/src/resource/64BitFirefoxDriver/geckodriver.exe");
				System.setProperty("webdriver.gecko.driver", driverPath.getAbsolutePath());
				//  marinate/gecko driver logging
				System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
				System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
				    
				FirefoxOptions options = new FirefoxOptions();
				options.addArguments("-private");
				
				selenium = new FirefoxDriver(options);
			}
		}
		catch (UnhandledAlertException alertException)
		{
			logger.info(alertException);
			BaseTestScript.selenium.switchTo().alert().accept();
		}
		logger.info(line);
		logger.info("TESTCASE START TIME [ BEFORE TEST ] : " + systemTime());
		logger.info(line);
		jse = (JavascriptExecutor) selenium;
	}
	
	@BeforeClass
	protected void testCaseStartTime()
	{
		Reporter.log(line);
		Reporter.log("TESTCASE START TIME [ BEFORE CLASS ] : " + systemTime());
		Reporter.log(line);
	}
	
	@BeforeMethod
	protected void beforeMethod() throws IOException
	{
		timeout(1);
		timeout(2);
		selenium.get(APPLICATION_URL);
		timeout(2);
	}
	
	@AfterClass
	protected void testCaseEndTime()
	{
		Reporter.log(line);
		Reporter.log("TESTCASE END TIME [AFTER CLASS] : " + systemTime());
		Reporter.log(line);
	}
	
	
	@AfterTest
	protected void afterTest()
	{
		try
		{
			BaseTestScript.selenium.quit();
			logger.info("Service stop succesfully");
		}
		catch (UnhandledAlertException alert)
		{
			logger.info("Alter is exist due to some system click : " + alert);
			selenium.switchTo().alert().accept();
		}
		catch (WebDriverException exception)
		{
			logger.info("Due to some WebDriver Exception : " + exception);
		}
		catch (Exception e)
		{
			logger.info(e);
		}
	}
	
	@AfterSuite
	protected void afterSuite() throws IOException
	{
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("dd MMM YYYY HH:mm:ss");
		Reporter.log(line);
		Reporter.log("End Time: " + dateFormat.format(cal.getTime()));
		Reporter.log(line);
	}
	
	public void setLocation()
	{
		this.location = selenium.getCurrentUrl();
	}

	protected void getLocation()
	{
		selenium.get(this.location);
	}
	
	protected void expectedResult(String message)
	{
		Reporter.log("");
		Reporter.log("<b> <font color='black' size='2'>Expected :"+message+ "</font></b>");
		Reporter.log("=========");
	}
	
	protected void expectedResult()
	{
		Reporter.log("");
		Reporter.log("<b> <font color='black' size='2'>Expected :"+ "</font></b>");
		Reporter.log("=========");
	}
	
	protected void testCaseDevelopedBy(String scriptWriterName, String testCaseName)
	{
		Reporter.log(
				"<b> <font color='black' size='2'>Automation Case Developed by : " + scriptWriterName + "</font></b>");
		Reporter.log("<b> <font color='black' size='2'>TestCase Name : " + testCaseName + "</font></b>");
		Reporter.log("<b> <font color='black' size='2'>TestCase Start Time : " + systemTime() + "</font></b>");
		Reporter.log(line);
	}
	
	protected void scenarioHeading(int i, String heading)
	{
		logger.info("###########################  Start Scenario " + i + "   #####################");
		Reporter.log("<b> <font color='black' size='2'>Scenario " + i + " : " + heading + "</font></b>");
		Reporter.log(line);
		Reporter.log("Steps : ");
		Reporter.log("");
	}
	
	protected void testcaseID(String testcaseID)
	{
		Reporter.log("<b> <font color='black' size='2'>TestcaseID:\n" + testcaseID + "</font></b>");
		Reporter.log(line);
		Reporter.log("");
	}
	
	private void getStaticData() throws IOException
	{
		username = getUsername();
		password = getPassword();
		APPLICATION_URL = ApplicationProperties.getInstance().getProperty("test.url").trim();
		BASE_PROJECT = ApplicationProperties.getInstance().getProperty("Base.Project").trim();
		browser = ApplicationProperties.getInstance().getProperty("selenium.browser.driver").trim();
		logger.info("====================================== Local Machine DETAILS ===================================");
		logger.info("Url is detected from property file : " + APPLICATION_URL);
		logger.info("Username is detected from property file : " + username);
		logger.info("Password is detected from property file : " + password);
		logger.info("Browser is Detected : " + browser);
		logger.info("Base Project : " + BASE_PROJECT);
		logger.info(line);
	}
	
	protected String getUsername() throws IOException
	{
		return ApplicationProperties.getInstance().getProperty("test.username").trim();
	}
	
	protected String getPassword() throws IOException
	{
		return ApplicationProperties.getInstance().getProperty("test.password").trim();
	}
	
	protected static String systemTime()
	{
		Calendar calendar = new GregorianCalendar();
		String amPm;
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		if (calendar.get(Calendar.AM_PM) == 0)
		{
			amPm = "AM";
		}
		else
		{
			amPm = "PM";
		}
		return hour + "_" + minute + "_" + second + "_" + amPm;
	}

	protected void timeout(long second)
	{
		try
		{
			Thread.sleep(second * 600);
		}
		catch (Exception e)
		{
			logger.error(e);
		}
	}
	
	public static String dateAndSystemTime(String dateAndTimeFormat)
	{
		DateFormat dateFormat = new SimpleDateFormat(dateAndTimeFormat);
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public void pass(String ExpectedDescription)
	{
		Reporter.log(line);
		Reporter.log("<b> <font color='green' size='2'>PASS :" + ExpectedDescription + "</font></b>");
		Reporter.log(line);
	}
	
	public void fail(String ExpectedDescription)
	{
		Reporter.log(line);
		Reporter.log("<b> <font color='red' size='2'>FAIL :" + ExpectedDescription + "</font></b>");
		Reporter.log(line);
	}
	
	protected void BugReport(String bugDescription)
	{
		Reporter.log(line);
		Reporter.log("<b> <font color='red' size='2'>BUG DESCRIPTION :" + bugDescription + "</font></b>");
		Reporter.log(line);
	}
	
	private void readScreenShotPath()
	{
		DownloadVerifier doc = new DownloadVerifier();
		File file = new File("../dist/screenshot.txt");
		try
		{
			screenshotPath = doc.ReadTextFile(file.getCanonicalPath());
		}
		catch (IOException e)
		{
			logger.info("screenshot in disable foam : " + e);
		}
	}
	
}
