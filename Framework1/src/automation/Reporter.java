package automation;

public class Reporter 
{
	public static void log(String message) 
	{
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		System.setProperty("org.uncommons.reportng.velocity-log", "false");
		org.testng.Reporter.log(message);
		org.testng.Reporter.log("<br/>");
	}

}
