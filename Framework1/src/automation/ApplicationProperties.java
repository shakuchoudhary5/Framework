package automation;

import java.io.IOException;
import java.util.Properties;

@SuppressWarnings("serial")
public class ApplicationProperties extends Properties
{
	private static ApplicationProperties props = null;

	private ApplicationProperties() throws IOException
	{
		load(getClass().getResourceAsStream("/automation/automation.properties"));
	}

	public static synchronized ApplicationProperties getInstance() throws IOException
	{
		if (props == null)
		{
			props = new ApplicationProperties();
		}
		return props;
	}
}
