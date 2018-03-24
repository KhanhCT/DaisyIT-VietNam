package com.daisyit.db.hibernate;

import java.io.File;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.daisyit.utils.Log;

public class HibernateUtil {
	
	private static SessionFactory sessionFactory = null;
	
	@SuppressWarnings("deprecation")
	private static SessionFactory getSessionFactory() {
		Log.setLog(HibernateUtil.class.getName());
		if (sessionFactory == null) {
			try {
				String workingDir = System.getProperty("user.dir");
				File file = null;
				if (workingDir.contains("\\")) {
					file = new File(workingDir + "\\hibernate.cfg.xml");
				}
				if (workingDir.contains("/")) {
					file = new File(workingDir + "/hibernate.cfg.xml");
				}
				sessionFactory = new Configuration().configure(file).buildSessionFactory();
				Log.writeLogInfo("Connect to database successfully!");
			} catch (Exception ex) {
				// Make sure you log the exception, as it might be swallowed
				Log.writeLogError("Cannot connect to database. Connect refuse!!!");

			}
		}

		return sessionFactory;
	}

	public static Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	public static Session openSession() {
		return getSessionFactory().openSession();
	}
	public static void closeSession(Session session)
	{
		session.flush();
		session.close();
		
	}
}