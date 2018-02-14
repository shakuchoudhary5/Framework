package automation;

import java.io.IOException;

public class Constant 
{
	public static String getBaseProject()
	{
		try
		{
			return ApplicationProperties.getInstance().getProperty("Base.Project").trim();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public static final String filepath=getBaseProject()+"/src/resource/testDocuments/";
	
	public static final String SYSTEM_USER_HOME = System.getProperty("user.home");
	public static final String FILE_PATH_DOWNLOADED = SYSTEM_USER_HOME + "\\Downloads\\";
}
