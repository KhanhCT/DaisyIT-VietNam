package com.daisyit.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.daisyit.utils.Log;

public class Ftp {
	private String ftpServer;
	private int ftpPort;
	private String ftpUsername;
	private String ftpPassword;
	private FTPClient ftpClient;
	// private Log log = new Log(this.getClass().getName());

	public Ftp(String ftpServer, String ftpUsername, String ftpPassword, int ftpPort) {
		boolean status;
		int replyCode;
		Log.setLogTitle(this.getClass().toString());
		FTPClientConfig ftpClientConfig = new FTPClientConfig(FTPClientConfig.SYST_NT);
		ftpClientConfig.setServerTimeZoneId("Asia/Saigon");
		ftpClient = new FTPClient();
		ftpClient.configure(ftpClientConfig);

		try {
			ftpClient.connect(ftpServer, ftpPort);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setAutodetectUTF8(true);
			replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				if (Common.DEBUG)
					Common.print("Ftp()", "FTP Server refuse connection!!");
					Log.writeLogError(this.getClass().toString(), "FTP Server refuse connection!!");
				ftpClient.disconnect();
			}
			
			status = ftpClient.login(ftpUsername, ftpPassword);
			if (!status) {
				Common.print("Ftp()", "Username or password incorrect!!");
				Log.writeLogWarning(this.getClass().toString(), "Username or password incorrect!!");
				ftpClient.disconnect();
				//System.exit(0);
			}
			

			ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setKeepAlive(true);
			ftpClient.setControlKeepAliveTimeout(100000);
			
		//	String encoded = new String(utf8_path.getBytes("UTF-8"), "ISO-8859-1");
			//ftpClient.changeWorkingDirectory(utf8_path);
			if (ftpClient.isConnected()) {
				Log.writeLogInfo(this.getClass().toString(), "FTP connected!!");
				Common.print("Ftp()", "FTP connected!!");
			}

		} catch (IOException e) {
			Log.writeLogInfo(this.getClass().toString(), "Oops! Ftp disconnected");
			Common.print("Ftp()", "Oops! Ftp disconnected: " + e.getMessage());
		}

	}

	public void ftpDownloadFile(String source, String destination) {
		File localfile = new File(destination);
		OutputStream outputStream;
		try {
			outputStream = new BufferedOutputStream(new FileOutputStream(localfile));
		
	        boolean success = ftpClient.retrieveFile(source, outputStream);
	        if (success) 
	        	Common.print("Ftp()","Ftp file successfully download.");
	        outputStream.close();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			Common.print("Ftp()","Error occurs in downloading files from ftp Server : " + ex.getMessage());
		}

//		try (FileOutputStream fos = new FileOutputStream(destination)) {
//			ftpClient.retrieveFile(source, fos);
//			fos.flush();
//			fos.close();
//			System.out.println("FTP download file successfull!!");
//		} catch (IOException ex) {
//			// if (Common.DEBUG)
//			System.out.println("Can not download file on FTP Server");
//			// log.writeLogError("ftpDownloadFile()", " Can not download file on FTP
//			// Server");
//		}finally {
//			
//		}
	}

	public void ftpDeleteFile(String urlFile) {
		boolean status = false;
		if (ftpClient.isConnected()) {
			try {
				status = ftpClient.deleteFile(urlFile);
				if (status) {
					// if (Common.DEBUG)
					// log.writeLogInfo("ftpDeleteFile()", "File deleted " + urlFile + "
					// Successfully!!");
				} else {
					// if (Common.DEBUG)
					// log.writeLogError("ftpDeleteFile()", "File cannot delete");
				}
			} catch (IOException e) {
				// if (Common.DEBUG)
				// log.writeLogError("ftpDeleteFile()", "Delete File Error");
				e.printStackTrace();
			}
		}

	}

	public boolean checkExistFile(String fileName, String hostDir) {
		boolean status = false;
		List<String> listFiles = null;
		listFiles = this.listFile(hostDir);
		if (listFiles != null) {
			return listFiles.contains(fileName);
		}
		return status;
	}

	public void ftpUploadFile(String source, String hostDir, String fileName) {
		File sourceFile = new File(source);
		if (sourceFile.exists() && ftpClient.isConnected()) {
			InputStream input;
			try {
				input = new FileInputStream(sourceFile);
				ftpClient.storeFile(hostDir + fileName, input);
				// if (Common.DEBUG)
				// log.writeLogInfo("ftpUploadFile()", "FTP upload file successfull!!");
			} catch (FileNotFoundException e) {
				// if (Common.DEBUG)
				// log.writeLogError("ftpUploadFile()", "File not found");
				e.printStackTrace();
			} catch (IOException ex) {
				// if (Common.DEBUG)
				// log.writeLogError("ftpUploadFile()", "FTP Upload Fail");
				ex.printStackTrace();
			}
		}
	}

	public void disconnect() {
		if (ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
				// if (Common.DEBUG)
				// log.writeLogInfo("disconnect()", "FTP Disconnected!!!");
			} catch (IOException ex) {
				// if (Common.DEBUG)
				// log.writeLogError("disconnect()", "FTP can not disconnect!!!");
				ex.printStackTrace();
			}
		}
	}

	public List<String> listFile(String path) {
		List<String> list = null;
		if (ftpClient.isConnected()) {
			list = new ArrayList<>();
			String fileName = null;	
			try {
				FTPFile[] files = ftpClient.listFiles(path);
				for (FTPFile file : files) {
					fileName = file.getName();
					list.add(fileName);
				}

			} catch (IOException e) {
				// if (Common.DEBUG)
				 Log.writeLogError("listFile()", "Oops! Can not get file from FTP " + e.getMessage());
			}
		}
		return list;
	}

	public boolean isConnected() {
		return ftpClient.isConnected();
	}

	public String getFtpServer() {
		return ftpServer;
	}

	public void setFtpServer(String ftpServer) {
		this.ftpServer = ftpServer;
	}

	public int getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(int ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFtpUsername() {
		return ftpUsername;
	}

	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}

}
