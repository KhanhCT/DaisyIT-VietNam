package com.daisyit.demo;

import java.util.Date;
import java.util.Timer;

import com.daisyit.controller.Common;
import com.daisyit.controller.ConfigParser;
import com.daisyit.controller.ScheduledDownloadTask;
import com.daisyit.utils.Log;
public class FileImportService {

	public static void main(String[] args) {
		
		System.setProperty("file.encoding", "UTF-8");
		long timePoll = 10000;
		Timer time = new Timer(); // Instantiate Timer Object
		ScheduledDownloadTask st = new ScheduledDownloadTask(); // Instantiate
		ConfigParser parser = new ConfigParser();
		Log.setLogTitle("Main()");
		// SheduledTask
		Date alarm = new Date();
		try {
			Common.print("FileImportService()","Starting.....");
			Log.writeLogInfo("FileImportService()", "Starting........");
			timePoll = parser.getTimePoll();
		} catch (Exception ex) {
			Common.print("FileImportService()","Oops! Please make sure that the PERIOD TIME is correct!" + ex.getMessage());
			Log.writeLogError("FileImportService()", "Oops! Please make sure that the PERIOD TIME is correct!" + ex.getMessage() );
			System.exit(0);
		}
		time.schedule(st, alarm, timePoll);
	}

}
