package helper;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.log4j.Logger;
import automation.Constant;

public class DownloadVerifier 
{
	Logger logger = Logger.getLogger(DownloadVerifier.class);
	
	public boolean verifyDownloadedFile(String fileName,int time)
	{
		int timeInMinutes=time*60;
		int count=0;
		File file=new File(Constant.FILE_PATH_DOWNLOADED+fileName);
		
		while(count<=timeInMinutes)
		{
			if(file.exists() && !file.isDirectory())
			{
				logger.info("File Exist");
				return true;
			}
			logger.info("File Exist");
			count++;
		}
			
		return false;
	}
	
	public void deleteDownloadedFileORFolder(String docPath)
	{
		File directory= new File(Constant.FILE_PATH_DOWNLOADED+docPath);
		
		if(directory.isDirectory())
		{
			String[] list=directory.list();
			for(String l:list)
			{
				File entry= new File(directory,l);
				entry.delete();
			}
			logger.info("Delete Directory");
		}
		else if(directory.isFile())
		{
			directory.delete();
			logger.info("Delete Files");
		}
		else
		{
			logger.info("No Directory and File Found");
		}
	}
	
	public void moveAllFiles() throws IOException
	{
		String downloadFileNewPath= Constant.SYSTEM_USER_HOME+"\\Downloads_backup";
		File source=new File(Constant.FILE_PATH_DOWNLOADED);
		File destinationPath=new File(downloadFileNewPath);
		
		Path dir = Paths.get(downloadFileNewPath);
		
		if (!Files.exists(dir))
		{
			Files.createDirectories(dir);
		}
		else
		{
			logger.info("Directory is Already Exists...!!!");
		}
		
		try
		{
			File[] onlyFiles = source.listFiles((FileFilter) FileFileFilter.FILE);
			for(File file: onlyFiles)
			{
				InputStream inStream= new FileInputStream(file);
				OutputStream outStream = new FileOutputStream(destinationPath + "/" + file.getName());
				
				byte[] buffer = new byte[1024];
			  
				int length;
				
	    	    //copy the file content in bytes
	    	    while ((length = inStream.read(buffer)) > 0)
	    	    {
	    	    	outStream.write(buffer, 0, length);
	    	    }
	    	    inStream.close();
	    	    outStream.close();
			}
		}
		catch(Exception e)
		{
			logger.info("Exception:-", e);
		}
		deleteAllFilesFromDirectory();
	}	
	
	public void deleteAllFilesFromDirectory()
	{
		logger.info("In Delete All Files From Directory.");
		File directory = new File(Constant.FILE_PATH_DOWNLOADED);
		File[] files = directory.listFiles();

		for (File file : files)
		{
			if (!file.delete())
			{
				logger.info("Failed to delete " + file);
			}
		}
	}
	
	public void deleteSpecificFile(String fileName)
	{
		try
		{
			File folder = new File(Constant.FILE_PATH_DOWNLOADED);
			File[] myfile = folder.listFiles();
			for (int i = 0; i < myfile.length; i++)
			{
				if (myfile[i].getName().contains(fileName))
				{
					myfile[i].delete();
				}
			}
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
		}
	}
	
	public String ReadTextFile(String path) throws IOException
	{
		logger.info("======== In Read File Data ========");
		logger.info("File Path ::=> " + path);
		Scanner scanner = new Scanner(new File(path));
		String contents = scanner.useDelimiter("\\Z").next();
		logger.info(contents);
		scanner.close();
		return contents;
	}

}
