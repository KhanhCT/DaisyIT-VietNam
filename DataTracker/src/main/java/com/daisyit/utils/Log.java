package com.daisyit.utils;

import java.io.File;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
public class Log {
	private static Logger logger;
	private static String className;
	
	public static void setLog(String className_) {
		String workingDir = null;
		className = className_;
		if (workingDir == null) {
			workingDir = System.getProperty("user.dir");		
			File file;
			if (workingDir.contains("/")) {
				file = new File(workingDir + "/log4j.properties");
				if (file.exists()) {
					PropertyConfigurator.configure(workingDir + "/log4j.properties");
				} else {
					Log.printInfor("Cannot find log4j.properties. Cannot write log file!!!");
				}
			} else {
				file = new File(workingDir + "\\log4j.properties");
				if (file.exists()) {
					PropertyConfigurator.configure(workingDir + "\\log4j.properties");
				} else {
					Log.printInfor("Cannot find log4j.properties. Cannot write log file!!!");
				}
			}
		}
		logger = Logger.getLogger(className_);
		
	}
	public static void printInfor(String body) {
		System.out.println("[" + className+ "]: " + body);
	}
	public static void writeLogInfo(String payload) {
		logger.info("[" + className + "]: " + payload);
	}

	public static void writeLogDebug( String payload) {
		logger.debug("[" + className + "]: " + payload);
	}

	public static void writeLogError(String payload) {
		logger.error("[" + className + "]: " + payload);

	}

	public static void writeLogWarning(String payload) {
		logger.warn("[" + className + "]: " + payload);

	}

	public static void writeLogFatal(String payload) {
		logger.fatal("[" + className + "]: " + payload);

	}

	public Logger getLogger() {
		return logger;
	}

	@SuppressWarnings("static-access")
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}