package com.daisyit.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import com.daisyit.controller.ConfigTag;

public class ConfigParser {
	InputStream inputStream;
	Properties prop ;
	public ConfigParser()
	{
		prop = new Properties();
		String workingDir = System.getProperty("user.dir");
		File file = null;
		String filePath = null;
		if (workingDir.contains("\\")) {
			filePath = workingDir + "\\config.properties";
		} else {
			filePath = workingDir + "/config.properties";
		}
		file = new File(filePath);

		try {
			inputStream = new FileInputStream(file);
			if (inputStream != null) {
				prop.load(inputStream);
			}
		} catch (FileNotFoundException e) {
			Common.print("ConfigParser", e.getMessage());
		}
		catch (IOException e) {
			Common.print("ConfigParser", e.getMessage());
		}
	}
	public Long getTimePoll()  throws IOException
	{
		Long timePoll = 0L;
		try {
			// get the property value and print it out
			String tmp = prop.getProperty(ConfigTag.TIME_SCHEDULER);
			timePoll = Long.parseLong(tmp);			
		} catch (Exception e) {
			Common.print("ConfigParser", e.getMessage());
		} finally {
			inputStream.close();
		}
		return timePoll;
	}

	public HashMap<String, String> getPropValues() throws IOException {
		HashMap<String, String> result = new HashMap<>();
		try {
			// get the property value and print it out
			String ftpServer = prop.getProperty(ConfigTag.FTP_SERVER);
			String ftpUsername = prop.getProperty(ConfigTag.FTP_USERNAME);
			String ftpPassword = prop.getProperty(ConfigTag.FTP_PASSWORD);
			String ftpPort = prop.getProperty(ConfigTag.FTP_PORT);
			String ftpDir = prop.getProperty(ConfigTag.FTP_INDIR);
			result.put(ConfigTag.FTP_SERVER, ftpServer);
			result.put(ConfigTag.FTP_USERNAME, ftpUsername);
			result.put(ConfigTag.FTP_PASSWORD, ftpPassword);
			result.put(ConfigTag.FTP_PORT, ftpPort);
			result.put(ConfigTag.FTP_INDIR, ftpDir);
		} catch (Exception e) {
			Common.print("ConfigTag", e.getMessage());
		} finally {
			inputStream.close();
		}
		return result;
	}
}