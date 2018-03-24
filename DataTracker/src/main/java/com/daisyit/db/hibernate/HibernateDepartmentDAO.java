package com.daisyit.db.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.daisyit.db.abstraction.DAOException;
import com.daisyit.db.abstraction.DepartmentDAO;
import com.daisyit.entity.Department;
import com.daisyit.utils.Log;

public class HibernateDepartmentDAO implements DepartmentDAO{
	private Session session;

	/**
	 * Auto generated method comment
	 *
	 * @param sessionFactory
	 */
	public void setSession(Session session) {
		Log.setLogTitle(this.getClass().toString());
		this.session = session;
	}
	@Override
	public Department getDeptList(String deptId) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Department> getAllDeptLists() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Department deleteDeptList(Department Deptlist) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Department addDeptList(Department deptlist) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Department> addMultiSDeptLists(List<Department> deptLists) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getDeptListId(String deptName) throws DAOException {
		// TODO Auto-generated method stub
		Transaction trans = null;
		String sqlQuery = "SELECT deptId FROM Department WHERE deptName= :deptName";
		int deptId = 0;
		try {
			trans = this.session.beginTransaction();
			Query query = this.session.createQuery(sqlQuery);
			query.setString("deptName", deptName);
			deptId = (Integer) query.uniqueResult();
			trans.commit();
		} catch (RuntimeException e) {
			Log.writeLogError(this.getClass().getMethods().toString(), e.getMessage());
			if (trans != null) {
				trans.rollback();
			}
		}
		return deptId;
	}

}
