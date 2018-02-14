package helper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.tools.ant.Task;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import automation.ApplicationProperties;
import automation.BaseTestScript;
import org.apache.commons.io.FileUtils;

public class EmailHelper extends Task
{
	private String folderPath = "";
	
	@Override
	public void execute()
	{
		if (folderPath != null)
		{
			saveScreenShotPath(folderPath);
		}
		log(folderPath);
	}
	
	private void saveScreenShotPath(String folderpath)
	{
		try
		{
			File file = new File("../dist/screenshot.txt");
			PrintWriter writer = new PrintWriter(file.getCanonicalPath(), "UTF-8");
			writer.println(folderpath);
			writer.close();
		}
		catch (IOException e)
		{
			System.err.println(e);
//			logger.info(e);
		}
	}
	

	public void setfolderPath(String folderPath)
	{
		this.folderPath = folderPath;
	}

	
	protected static String captureErrorScreen(String timeStemp) throws IOException
	{
		return captureErrorScreen(null, timeStemp);
	}

	protected static String captureErrorScreen(String screenName, String timeStemp) throws IOException
	{
		String path = "";
		if (!"No".equalsIgnoreCase(ApplicationProperties.getInstance().getProperty("report.with.screenshot")))
		{
			try
			{
				WebDriver augmentedDriver = new Augmenter().augment(BaseTestScript.selenium);
				path = BaseTestScript.screenshotPath + screenName + timeStemp + ".jpeg";
				File error = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(error, new File(path));
			}
			catch (Exception exception)
			{
				exception.getMessage();
			}
		}
		return path;
	}
}
