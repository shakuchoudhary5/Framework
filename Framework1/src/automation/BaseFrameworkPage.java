package automation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class BaseFrameworkPage
{
	private Logger logger;
	protected WebDriver selenium;
	protected Actions build;
	protected JavascriptExecutor jse;
	public static final int Seconds = 30;
	
	public BaseFrameworkPage(WebDriver driver)
	{
		PropertyConfigurator.configure("log4j.properties");
		this.selenium = driver;
		logger = Logger.getLogger(this.getClass().getName());
		PageFactory.initElements(new AjaxElementLocatorFactory(selenium, Seconds), this);
		build = new Actions(selenium);
		jse = (JavascriptExecutor) selenium;
	}
	
	public void getHighlightElement(final WebElement element)
	{
		try
		{
			Wait<WebDriver> wait = new WebDriverWait(selenium, Seconds);
			// Wait for search to complete
			wait.until(new ExpectedCondition<Boolean>()
			{
				@Override
				public Boolean apply(WebDriver webDriver)
				{
					return element != null;
				}
			});
			((JavascriptExecutor) selenium).executeScript("arguments[0].style.border='2px solid red'", element);
		}
		catch (Exception e)
		{
			logger.info("Fail to highlight the Element");
		}
	}
	
	public void moveToElement(By by)
	{
		try
		{
			getHighlightElement(selenium.findElement(by));
			build.moveToElement(selenium.findElement(by)).build().perform();
		}
		catch (NoSuchElementException e)
		{
			logger.info(e.getMessage());
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
		}
	}
	
	public void click(By by)
	{
		moveToElement(by);
		selenium.findElement(by).click();
	}
	
	public void sendKeys(By by, String value) throws IOException
	{
		waitForParticularElementExist(by, Seconds);
		try
		{
			moveToElement(by);
			timeIntervel();
			clear(by);
			selenium.findElement(by).sendKeys(value);
		}
		catch (NoSuchElementException e)
		{
			Assert.assertTrue(false, "Fail to send keys from text box : " + by + " on page : " + e.getMessage());
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
		}
	}
	
	public void clear(By by) throws IOException
	{
		try
		{
			getHighlightElement(selenium.findElement(by));
			selenium.findElement(by).clear();
		}
		catch (NoSuchElementException e)
		{
			Assert.assertTrue(false, "Fail to clear value from text box : " + by + " on page : " + e.getMessage());
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
		}
	}
	
	public boolean isDisplayed(By by)
	{
		try
		{
			moveToElement(by);
			getHighlightElement(selenium.findElement(by));
			return selenium.findElement(by).isDisplayed();
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public boolean isDisplayedAllXPath(By... bys)
	{
		try
		{
			for (By by : bys)
			{
				moveToElement(by);
				getHighlightElement(selenium.findElement(by));
				if (!selenium.findElement(by).isDisplayed())
					return false;
			}
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public void moveScroll(By by, int value) throws IOException
	{
		WebElement scrollArea = selenium.findElement(by);
		jse.executeScript("arguments[0].scrollTop = arguments[1];", scrollArea, value);
	}
	
	public void Scroll(int xPoint, int yPoint)
	{
		jse.executeScript("window.scrollBy(" + xPoint + "," + yPoint + ")");
		timeIntervel(2);
	}
	
	public void ScrollToElement(By by) throws IOException
	{
		jse.executeScript("arguments[0].scrollIntoView(true);", selenium.findElement(by));
		timeIntervel(2);
	}
	
	public void ScrollToElement(WebElement ele) throws IOException
	{
		jse.executeScript("arguments[0].scrollIntoView(true);", ele);
		timeIntervel();
	}
	
	public void scrollToBottom()
	{
		timeIntervel(2);
		((JavascriptExecutor) selenium).executeScript("window.scrollTo(0, Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight))");
		timeIntervel(2);
	}
	
	public void scrollToTop()
	{
		Boolean vertscrollStatus = (Boolean) jse.executeScript("return document.documentElement.scrollHeight>document.documentElement.clientHeight;");
		if (vertscrollStatus)
		{
			selenium.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.HOME);
			timeIntervel(2);
		}
		else
		{
			jse.executeScript("scrollBy(0, -1000)");
		}
	}
	
	public synchronized void timeIntervel(int time)
	{
		try
		{
			wait(1000*time);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public synchronized void timeIntervel()
	{
		try
		{
			wait(500);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void switchTab()
	{
		timeIntervel(3);
		ArrayList<String> tabs2 = new ArrayList<>(selenium.getWindowHandles());
		selenium.switchTo().window(tabs2.get(1));
		timeIntervel(3);
	}
	
	public void backToTab()
	{
		ArrayList<String> tabs2 = new ArrayList<>(selenium.getWindowHandles());
		selenium.close();
		timeIntervel(3);
		selenium.switchTo().window(tabs2.get(0));
	}
	
	public void waitForParticularElement(final By element, int waitForSeconds)
	{

		try
		{
			Wait<WebDriver> wait = new WebDriverWait(selenium, waitForSeconds);
			// Wait for search to complete
			wait.until(ExpectedConditions.visibilityOfElementLocated(element));

		}
		catch (Exception e)
		{
			logger.error(e + " Fail to Wait for element for " + element.toString());
		}
	}
	
	public void waitUntilElementDisplays(final By element) throws IOException
	{
		int i = 1;
		do
		{
			try
			{
				selenium.findElement(element).isDisplayed();
				timeIntervel(1);
				i++;
			}
			catch (NoSuchElementException e)
			{
				timeIntervel();
				logger.info("waiting for element : " + element.toString() + " :  Waiting Time [ " + i + " ] out of " + Seconds);
				break;
			}
			catch (StaleElementReferenceException ser)
			{
				timeIntervel();
				selenium.navigate().refresh();
				timeIntervel(3);
			}
		}
		while (i <= 200);
	}
	
	public void waitForParticularElementExist(final By element, int waitForSeconds)
	{

		try
		{
			Wait<WebDriver> wait = new WebDriverWait(selenium, waitForSeconds);
			// Wait for search to complete
			wait.until(ExpectedConditions.presenceOfElementLocated((element)));

		}
		catch (Exception e)
		{
			logger.error(e + " Element not present in DOM " + element.toString());
		}
	}
	
	public String getCSSValue(By by, String attributeName)
	{
		waitForParticularElement(by, Seconds);
		try
		{
			getHighlightElement(selenium.findElement(by));
			return selenium.findElement(by).getCssValue(attributeName).trim();
		}
		catch (NoSuchElementException e)
		{
			logger.error("Fail to get CSS value from : " + by + " on page : " + e.getMessage());
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
		}
		return null;
	}
	
	public String getWindowPopUpMsgAndAccept(By xpath,By Value)
	{
		timeIntervel(2);
		String msg = "";
		try
		{
			String window=selenium.getWindowHandle();
			selenium.switchTo().window(window);
			msg=getText(xpath);
			click(Value);
			
			timeIntervel(1);
			return msg;
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
			return "";
		}
	}
	
	public String getText(By by)
	{
		waitForParticularElement(by, Seconds);
		try
		{
			getHighlightElement(selenium.findElement(by));
			return selenium.findElement(by).getText().trim();
		}
		catch (NoSuchElementException e)
		{
			Assert.assertTrue(false, "Fail to get text value from : " + by + " on page : " + e.getMessage());

		}
		catch (Exception e)
		{
			logger.info(e.getMessage());

		}
		return null;
	}
	
	public String getPopupMsg_fromSystemPopup_andAccept()
	{
		timeIntervel(2);
		String msg = "";
		try
		{
			timeIntervel(1);
			msg = selenium.switchTo().alert().getText();
			timeIntervel(2);
			selenium.switchTo().alert().accept();
			timeIntervel(1);
			return msg;
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
			return "";
		}
	}
	
	public void alertAcceptandDismiss(boolean value)
	{
		selenium.switchTo().alert();
		timeIntervel(2);
		if(value)
		{
			selenium.switchTo().alert().accept();	
		}
		else
		{
			selenium.switchTo().alert().dismiss();
		}
		
		timeIntervel(1);
	}
	
	public void getAlert(String message, int second)
	{
		try
		{
			((JavascriptExecutor) BaseTestScript.selenium).executeAsyncScript(
					"setTimeout(arguments[0]);setTimeout(function(){window.alert('" + message + "'); });");
			timeout(second);
			BaseTestScript.selenium.switchTo().alert().accept();
		}
		catch (Exception e)
		{
			logger.info("Fail To Generated or Handled Alert...!!! " + e);
		}
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
	
	public int getNumberOfListOfElements(By by)
	{
		try
		{
			return selenium.findElements(by).size();
		}
		catch(Exception e)
		{
			logger.info(e.getMessage());
			return 0;
		}
	}
}
