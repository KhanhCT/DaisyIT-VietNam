package com.daisyit.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.hibernate.Session;

import com.daisyit.db.hibernate.HibernateDepartmentDAO;
import com.daisyit.db.hibernate.HibernateStaffDAO;
import com.daisyit.db.hibernate.HibernateUtil;
import com.daisyit.entity.Staff;
import com.daisyit.utils.Log;

public class MonitorTask extends TimerTask {

	@Override
	public void run() {
		Log.setLog(this.getClass().toString());
		HashMap<String, String> configs = null;
		ConfigParser parser = new ConfigParser();
		try {
			configs = parser.getPropValues();
		} catch (IOException e) {
			Log.printInfor("Not found configuration file. System shutdown!");
			Log.writeLogError(e.getMessage());
			System.exit(0);
		}
		String uri = configs.get(ConfigTag.FTP_INDIR);
		File dir = new File(uri);
		if (!dir.isDirectory()) {
			Log.writeLogError(uri + " is not existed");
			Log.printInfor(uri + " is not existed");
			System.exit(0);

		}
		File[] files = dir.listFiles((FileFilter) FileFileFilter.FILE);
		if (files != null) {
			Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
			for (File file : files) {
				Session session = HibernateUtil.openSession();
				HibernateDepartmentDAO hDepartmentDao = new HibernateDepartmentDAO();
				hDepartmentDao.setSession(session);
				HibernateStaffDAO hStaffDao = new HibernateStaffDAO();
				hStaffDao.setSession(session);

				String fileName = file.getName();
				if (fileName.contains("NSRP_STAFF") && fileName.endsWith(".csv")) {
					Log.printInfor("Updating file: " + fileName);
					List<String[]> fileContent = new ArrayList<>();
					List<Staff> staffs = null;
					fileContent = CSVUtils.readCSV(uri + "//" + fileName);
					staffs = new ArrayList<>();
					for (String[] content : fileContent) {
						if (content != null && content.length >= 5) {
							Staff staff = new Staff();
							staff.setCardId(content[0]);
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
						else {
							String errorEntry = "";
							for(String str : content)
								errorEntry +=  str + " ";
							Log.printInfor("Ignore entry " + errorEntry + " .Because of error format");
							Log.writeLogWarning("Ignore entry " + content.toString() + " .Because of error format");
						}
					}
					if (staffs != null)
						hStaffDao.addMultiStaffs(staffs);
				}
				HibernateUtil.closeSession(session);
			}
		}

	}

}
