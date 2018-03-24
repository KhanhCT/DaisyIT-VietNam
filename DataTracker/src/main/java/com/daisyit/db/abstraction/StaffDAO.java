package com.daisyit.db.abstraction;
import java.util.List;

import com.daisyit.entity.Staff;
import com.daisyit.db.abstraction.DAOException;
public interface StaffDAO {
	
	Staff getStaff(String staffId) throws DAOException;
	
	List<Staff> getAllStaffs() throws DAOException;
	
	Staff deleteStaff(Staff staff) throws DAOException;
	
	Boolean addStaff(Staff staff) throws DAOException;
	
	void addMultiStaffs(List<Staff> staffs)  throws DAOException;
}
