package com.daisyit.production;

import java.util.Date;
import java.util.Timer;

import com.daisyit.controller.ConfigParser;
import com.daisyit.controller.MonitorTask;
import com.daisyit.utils.Log;
public class FileImportService {

	public static void main(String[] args) {
		
		System.setProperty("file.encoding", "UTF-8");
		long timePoll = 10000;
		Timer time = new Timer(); // Instantiate Timer Object
		MonitorTask st = new MonitorTask(); // Instantiate
		ConfigParser parser = new ConfigParser();
		Log.setLog(FileImportService.class.getName());
		Log.writeLogInfo("Starting service........................");
		Log.printInfor("Starting service........................");
		// SheduledTask
		Date alarm = new Date();
		try {
			timePoll = parser.getTimePoll();
		} catch (Exception ex) {
			Log.printInfor("Oops! Please make sure that the PERIOD TIME is correct!" + ex.getMessage());
			Log.writeLogError("Oops! Please make sure that the PERIOD TIME is correct!" + ex.getMessage() );
			System.exit(0);
		}
		time.schedule(st, alarm, timePoll);
	}

}
