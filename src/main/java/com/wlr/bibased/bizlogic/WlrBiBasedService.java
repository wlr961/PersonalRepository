package com.wlr.bibased.bizlogic;

import java.util.Date;
import java.util.List;

import com.wlr.bibased.dao.WlrBiBasedDao;
import com.wlr.bibased.model.BiBased;
import com.wlr.bibased.model.Degree;
import com.wlr.bibased.model.Message;
import com.wlr.bibased.model.News;
import com.wlr.bibased.model.User;
import com.wlr.bibased.utils.JdbcUtils;

/**
 * @author 
 * 服务层，在dao层上进行业务封装
 */

public class WlrBiBasedService {
	
	WlrBiBasedDao Dao=new WlrBiBasedDao();	
	
	public boolean addSelectStuBiased(BiBased bibased)
	{
		return Dao.addSelectStuBiased(bibased);
	}
	public List<BiBased> getTeaBiBasedByWhere(int pageSize,int pageIndex,String adviser)
	{
		return Dao.getTeaBiBasedByWhere(pageSize, pageIndex, adviser);
	}
	public int certenStuBiBased(String username,String bibno)
	{
		return Dao.certenStuBiBased(username,bibno);
	}
	public int selectedAlready(String username)
	{
		return Dao.selectedAlready(username);
	}
	public int getStudentCount(String username)
	{
		return Dao.getStudentCount(username);
	}
	public List<Message> getSomeMessageByWhere(int pageSize,int pageIndex,String sendid,String acceptid)
	{
		return Dao.getSomeMessageByWhere(pageSize, pageIndex, sendid,acceptid);
	}
	public List<Message> getAllMessageByWhere(int pageSize,int pageIndex,String username,String sendid,String acceptid)
	{
		return Dao.getAllMessageByWhere(pageSize, pageIndex, username,sendid,acceptid);
	}
	/**
	 * @param username
	 * @param degree
	 * @return 应用实体
	 */
	public User getUserInfo(String username,String degree)
	{
		return Dao.getUserInfo(username, degree);
	}
	
	
	/**
	 * @param username
	 * @param degree
	 * @return
	 */
	public List<User> getAllUsersByWhere(int pageSize,int pageIndex,String username , String degree)
	{
		return Dao.getAllUsersByWhere(pageSize, pageIndex, username, degree);
	}
	
	/**
	 * @param tableName 表名称
	 * @return 返回某张表的记录数
	 */
	public int getTotal(String tableName) 
	{
		return Dao.getTotal(tableName);
	}

	/**
	 * @param username
	 * @param degree
	 * @return 删除记录
	 */
	public boolean delUser(String username,String degree)
	{
		return Dao.delUser(username, degree);
	}
	
	
	public boolean addUser(User user)
	{
		return Dao.addUser(user);
	}
	
	
	public boolean updateUser(User user)
	{
		return Dao.updateUser(user);
	}
	
	
	public List<News> getAllNewsByWhere(int pageSize,int pageIndex,String title,Date startTime , Date endTime)
	{
		return Dao.getAllNewsByWhere(pageSize, pageIndex, title, startTime, endTime);
	}
	
	public boolean delRecord(String tableName,String columnName,String delScope)
	{
		return Dao.delRecord(tableName, columnName, delScope);
	}
	/**
	 * 添加毕设题目
	 * @param bibased  BiBased对象
	 * @return true:添加成功  false：添加失败
	 */
	public boolean addBiased(BiBased bibased)
	{
		// 获取毕设编号
		bibased.setBibno(JdbcUtils.randomUUID());
		return Dao.addBiased(bibased);
	}
	
	public List<BiBased> getAllBiBasedByWhere(int pageSize,int pageIndex,String adviser,String student)
	{
		return Dao.getAllBiBasedByWhere(pageSize, pageIndex, adviser,student);
	}
	
	 public BiBased getBiBased(String bibno)
	 {
		 return Dao.getBiBased(bibno);
	 }
	 
	 public boolean updateBiBased(BiBased biBased)
	 {
		 return Dao.updateBiBased(biBased);
	 }
	 
	 public List<BiBased> getAllChooseBiBasedByWhere(int pageSize,int pageIndex,String adviser,String bibtitle,String username,String selected)
	 {
		 return Dao.getAllChooseBiBasedByWhere(pageSize, pageIndex, adviser, bibtitle,username,selected);
	 }
	
	 /*2018
	 public boolean selectBiBased(String bibno,String student)
	 {
		 return Dao.selectBiBased(bibno, student);
	 }
	*/ 
	 public boolean unSelectBiBased(String bibno,String student)
	 {
		 return Dao.unSelectBiBased(bibno, student);
	 }
	 
	 public BiBased getMyBiBased(String student)
	 {
		 return Dao.getMyBiBased(student);
	 }

	 public boolean Login(String username,String usercode,String userdegree)
	 {
		 return Dao.Login(username, usercode, userdegree);
	 }
	 
	 public boolean saveStudentDegree(List<Degree> list)
	 {
		 return Dao.saveStudentDegree(list);
	 }
	 
	 public List<Degree> getStudentDegree()
	 {
		 return Dao.getStudentDegree();
	 }
	 
	 
	 public boolean saveTeacherDegree(List<Degree> list)
	 {
		 return Dao.saveTeacherDegree(list);
	 }
	 
	 public List<Degree> getTeacherDegree()
	 {
		 return Dao.getTeacherDegree();
	 }
}
