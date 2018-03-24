package com.daisyit.utils;

import java.io.File;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.daisyit.controller.Common;

public class Log {
	private static Logger logger;
	public static void setLogTitle(String className_) {
		String workingDir = null;
		if (workingDir == null) {
			workingDir = System.getProperty("user.dir");
			File file;
			if (workingDir.contains("/")) {
				file = new File(workingDir + "/log4j.properties");
				if (file.exists()) {
					PropertyConfigurator.configure(workingDir + "/log4j.properties");
				} else {
					Common.print("Log()", "Cannot find log4j.properties. Cannot write log file!!!");
				}
			} else {
				file = new File(workingDir + "\\log4j.properties");
				if (file.exists()) {
					PropertyConfigurator.configure(workingDir + "\\log4j.properties");
				} else {
					Common.print("Log()", "Cannot find log4j.properties. Cannot write log file!!!");
				}
			}

		}
		logger = Logger.getLogger(className_);
	}

	public static void writeLogInfo(String header, String payload) {
		logger.info("[" + header + "]: " + payload);
	}

	public static void writeLogDebug(String header, String payload) {
		logger.debug("[" + header + "]: " + payload);
	}

	public static void writeLogError(String header, String payload) {
		logger.error("[" + header + "]: " + payload);

	}

	public static void writeLogWarning(String header, String payload) {
		logger.warn("[" + header + "]: " + payload);

	}

	public static void writeLogFatal(String header, String payload) {
		logger.fatal("[" + header + "]: " + payload);

	}

	public Logger getLogger() {
		return logger;
	}

	@SuppressWarnings("static-access")
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}