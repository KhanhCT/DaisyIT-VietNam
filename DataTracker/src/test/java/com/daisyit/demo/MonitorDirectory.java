package com.daisyit.demo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;

import com.daisyit.controller.CSVUtils;
import com.daisyit.controller.ConfigParser;
import com.daisyit.controller.ConfigTag;
import com.daisyit.db.hibernate.HibernateDepartmentDAO;
import com.daisyit.db.hibernate.HibernateStaffDAO;
import com.daisyit.db.hibernate.HibernateUtil;
import com.daisyit.entity.Staff;

public class MonitorDirectory {

	public static void main(String[] args) throws IOException, InterruptedException {
		HashMap<String, String> configs = null;
		ConfigParser parser = new ConfigParser();
		configs = parser.getPropValues();
		String path = configs.get(ConfigTag.FTP_INDIR);

		Path faxFolder = Paths.get(path);
		WatchService watchService = FileSystems.getDefault().newWatchService();
		// faxFolder.register(watchService,
		// StandardWatchEventKinds.ENTRY_CREATE);
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
		// faxFolder.register(watchService,
		// StandardWatchEventKinds.ENTRY_DELETE);
		System.out.println("Starting service........................");
		boolean valid = true;
		do {
			WatchKey watchKey = watchService.take();

			for (@SuppressWarnings("rawtypes")
			WatchEvent event : watchKey.pollEvents()) {
				if (StandardWatchEventKinds.ENTRY_MODIFY.equals(event.kind())) {
					Session session = HibernateUtil.openSession();
					HibernateDepartmentDAO hDepartmentDao = new HibernateDepartmentDAO();
					hDepartmentDao.setSession(session);
					HibernateStaffDAO hStaffDao = new HibernateStaffDAO();
					hStaffDao.setSession(session);

					String file = event.context().toString();
					System.out.println("File Created:" + file);
					if (file.contains("NSRP_STAFF") && file.endsWith(".csv")) {
						List<String[]> fileContent = new ArrayList<>();
						List<Staff> staffs = null;
						fileContent = CSVUtils.readCSV(path + "//" + file);
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
						}
						if (staffs != null)
							hStaffDao.addMultiStaffs(staffs);
					}
					faxFolder.resolve(file);
					HibernateUtil.closeSession(session);
				}
			}
			valid = watchKey.reset();
		} while (valid);

	}
}