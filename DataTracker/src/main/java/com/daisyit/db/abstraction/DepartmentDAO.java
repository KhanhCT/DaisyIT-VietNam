package com.daisyit.db.abstraction;

import java.util.List;

import com.daisyit.entity.Department;
import com.daisyit.db.abstraction.DAOException;
public interface DepartmentDAO {
	Department getDeptList(String deptId) throws DAOException;
	
	List<Department> getAllDeptLists() throws DAOException;
	
	Department deleteDeptList(Department Deptlist) throws DAOException;
	
	Department addDeptList(Department deptlist) throws DAOException;
	
	List<Department> addMultiSDeptLists(List<Department> deptLists)  throws DAOException;
	public int getDeptListId(String deptName) throws DAOException ;
}
