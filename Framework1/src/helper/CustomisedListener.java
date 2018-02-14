package helper;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import automation.ApplicationProperties;
import automation.BaseTestScript;

public class CustomisedListener extends BaseTestScript implements ITestListener
{
	private String testcaseName;
	
	@Override
	public void onTestStart(ITestResult result) 
	{
		testcaseName = result.getInstanceName();
	}
	
	@Override
	public void onTestSuccess(ITestResult result) 
	{

	}

	@Override
	public void onTestFailure(ITestResult result)
	{
		logger.info("***** Error " + result.getName() + " test has failed *****");
		String testMethodName=result.getName().trim();
		String timestemp=systemTime();
		
		if(!result.isSuccess())
		{
			try
			{
				if(!("no").equals(ApplicationProperties.getInstance().getProperty("report.with.screenshot")))
				{
					String path = EmailHelper.captureErrorScreen(testMethodName + "_" + timestemp);
					timeout(1);
					System.setProperty("org.uncommons.reportng.escape-output","false");
					Reporter.log("<a title= \" Error ScreenShot... \" href=\"" + path
							+ "\"><img width=\"100\" height=\"100\" alt=\"" + result.getThrowable().getMessage()
							+ "\" title=\" Error ScreenShot... \" src=\"" + path + "\">" + "</a>");
				}
			}
			catch(Exception e)
			{
				logger.info(e);
			}
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) 
	{

	}
	
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) 
	{
		// This is just a piece of shit, ignore this
	}

	@Override
	public void onStart(ITestContext context) 
	{

	}

	@Override
	public void onFinish(ITestContext context) 
	{
		
	}

}
	
