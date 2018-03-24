package com.daisyit.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import org.hibernate.Session;
import com.daisyit.db.hibernate.*;
import com.daisyit.entity.Staff;
import com.daisyit.utils.Log;

public class ScheduledDownloadTask extends TimerTask {
	private Ftp ftp;
	// private List<String> preFiles = new ArrayList<>();
	private String ROOT_PATH = System.getProperty("user.dir");
	private ConfigParser parser = new ConfigParser();

	@Override
	public void run() {
		Log.setLog(this.getClass().toString());
		String ftpServer = null;
		int ftpPort = ConfigTag.DEFAULT_PORT;
		String ftpUsername = null;
		String ftpPassword = null;
		String hostDir = ConfigTag.DEFAULT_HOSTDIR;
		List<String> currentFiles = new ArrayList<>();
		// List<String> tempFiles = new ArrayList<>();
		Session session = HibernateUtil.openSession();
		HashMap<String, String> configs = null;
		HibernateDepartmentDAO hDepartmentDao = new HibernateDepartmentDAO();
		hDepartmentDao.setSession(session);
		String path = null;
		try {
			configs = parser.getPropValues();
			ftpServer = configs.get(ConfigTag.FTP_SERVER);
			ftpUsername = configs.get(ConfigTag.FTP_USERNAME);
			ftpPassword = configs.get(ConfigTag.FTP_PASSWORD);
			ftpPort = Integer.parseInt(configs.get(ConfigTag.FTP_PORT));
			hostDir = configs.get(ConfigTag.FTP_INDIR);
		} catch (NullPointerException ex) {
			Log.writeLogError( ex.getMessage());
		} catch (IOException e) {
			Log.writeLogError( e.getMessage());
		}
		ftp = new Ftp(ftpServer, ftpUsername, ftpPassword, ftpPort);
		if (ftp.isConnected()) {
			currentFiles = ftp.listFile(hostDir);
			if (currentFiles.toString() != "[]") {
				// if (currentFiles.size() != preFiles.size()) {
				Log.printInfor("New files on FTP server created or deleted");
				Log.writeLogInfo( "New files on FTP server created or deleted");
				// tempFiles.clear();
				// for (String str : currentFiles)
				// tempFiles.add(str);
				// currentFiles.removeAll(preFiles);

				for (String file : currentFiles) {
					if (ROOT_PATH.contains("/"))
						path = ROOT_PATH + "/" + file;
					else
						path = ROOT_PATH + "\\" + file;

					ftp.ftpDownloadFile(hostDir + file, path);
					if (file.contains("NSRF_STAFF") && file.endsWith(".csv")) {
						List<String[]> fileContent = new ArrayList<>();
						List<Staff> staffs = null;
						HibernateStaffDAO hStaffDao = new HibernateStaffDAO();
						hStaffDao.setSession(session);
						fileContent = CSVUtils.readCSV(path);
						staffs = new ArrayList<>();
						for (String[] content : fileContent) {
							Staff staff = new Staff();				
							staff.setCardId(content[0]);
							System.out.println(content[0]);
							staff.setStaffId(content[1]);
							try {
								staff.setStaffName(new String(content[2].getBytes("UTF-8")));
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							int deptId = hDepartmentDao.getDeptListId(content[3]);
							staff.setDeptId(deptId);
							staff.setPosition(content[4]);
							staffs.add(staff);
						}
						hStaffDao.addMultiStaffs(staffs);
					}
					try {

						File f = new File(path);
						if (f.delete()) {
							Log.printInfor(f.getName() + " is deleted!");
						} else {
							Log.printInfor("Delete operation is failed.");
						}
					} catch (Exception e) {
						Log.writeLogError( e.getMessage());
						e.printStackTrace();
					}
				}
				// preFiles.clear();
				// for (String str : tempFiles)
				// preFiles.add(str);
				// }
			}
			HibernateUtil.closeSession(session);
			ftp.disconnect();
		}

	}
}