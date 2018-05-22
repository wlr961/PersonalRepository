package com.wlr.bibased.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wlr.bibased.model.BiBased;
import com.wlr.bibased.model.Degree;
import com.wlr.bibased.model.Message;
import com.wlr.bibased.model.News;
import com.wlr.bibased.model.User;
import com.wlr.bibased.utils.JdbcUtils;

/**
 * @author 
 * 数据库底层封装
 */
public class WlrBiBasedDao {
	
	public List<String> getRecivers(String username)
	{
		Connection conn=JdbcUtils.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		List<String> list=new ArrayList<>();
		StringBuilder sql=null;
		 sql=new StringBuilder("select student from bibased where adviser="+"'"+username+"' and center=1");
		try {
			pst=conn.prepareStatement(sql.toString());
			rs=pst.executeQuery();
			while(rs.next())
			{
				String stu=new String();
				stu=rs.getString("student");
				list.add(stu);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return list;
	}
	public Message getMessInfo(int id,String username)
	{
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		ResultSet rs=null;
		Message mess = new Message();
		try {
			st = conn.createStatement();
			rs=st.executeQuery("select * from message where id="+id);			
			while(rs.next())
			{
				mess.setId(rs.getInt("id"));
				mess.setSendid(rs.getString("sendid"));
				mess.setAcceptid(rs.getString("acceptid"));
				mess.setSenddate(new java.util.Date(rs.getTimestamp("senddate").getTime()));
				mess.setReaded(rs.getInt("readed"));
				mess.setContent(rs.getString("content"));
				mess.setAppendixname(rs.getString("appendixname"));
			}
			if(mess.getReaded()==0&&username.equals(mess.getAcceptid()))
			{
				st.executeUpdate("update message set readed=1 where id="+id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				st.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return mess;		
	}
	
	public boolean delMessage(int messid)
	{
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		String adviser=null;
		try {
			st=conn.createStatement();
			int i=st.executeUpdate("delete from message where id="+messid);
		    if(i==1)
		    {
		    	return true;
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				st.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
	}
	public int addMessage(String username,String adviser,String content,String student)
	{
		Connection conn=JdbcUtils.getConn();
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		ResultSet rs=null;
		int messid=0;
		String sql="insert into message(sendid,acceptid,content,senddate)values(?,?,?,?)";
		try
		{
			pst=conn.prepareStatement(sql);
			pst.setString(1, username);
			if(!"".equals(student)&&null!=student)
			{
				pst.setString(2, student);
			}
			else
			{
				pst.setString(2, adviser);
			}
			pst.setString(3,content);
			java.sql.Timestamp sendtime=new java.sql.Timestamp(new Date().getTime());
			pst.setTimestamp(4, sendtime);
			pst.executeUpdate();
			pst2=conn.prepareStatement("select id from message where senddate='"+sendtime+"'");
			rs=pst2.executeQuery();
			if(rs.next())
			{
				messid=rs.getInt("id");
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		finally
		{
			try
			{
				pst.close();
				conn.close();
				pst2.close();
				rs.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return  messid;
	}
	public String selectedAdvserAlready(String username)
	{
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		ResultSet rs=null;
		String adviser=null;
		try {
			st=conn.createStatement();
			//获取选中的数目，为1，说明学生选择成功
			rs=st.executeQuery("select adviser from bibased where student='"+username+"' and center=1");
			if(rs.next())
			{
				adviser=rs.getString("adviser");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				st.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return adviser;
	}
	public List<News> getAllNewsByWhere()
	{		
		Connection conn=JdbcUtils.getConn();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Statement st=null;
		ResultSet rs=null;
		List<News> list=new ArrayList<>();
	    StringBuilder sb=new StringBuilder("select * from news where 1=1 ");
		try {
			st=conn.createStatement();
			rs=st.executeQuery(sb.toString());
			while(rs.next())
			{
				News news = new News();
			    news.setNewid(rs.getInt("newid"));
			    news.setTitle(rs.getString("title"));
			    news.setPublisher(rs.getString("publisher"));
			    news.setPublishdate(new Date(rs.getDate("publishdate").getTime()));
				list.add(news);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				st.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return list;
		
	}
	
	
	/**
	 * 管理员获取选题数据
	 * @param pageSize   
	 * @param pageIndex
	 * @param adviser     指导教师
	 * @param bibtitle    毕设课题名
	 * @param username    管理员账号
	 * @return    毕设课题记录集合  
	 */
	public List<BiBased> getManBiBasedByWhere(int pageSize,int pageIndex,String adviser,String bibtitle,String username)
	{
		Connection conn=JdbcUtils.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		PreparedStatement pst2=null;
		ResultSet rs2=null;
		List<BiBased> list=new ArrayList<>();
		
		StringBuilder sql=new StringBuilder("select * from bibased  where 1=1");
		//查询指导教师
		if(!"".equals(adviser)&&null!=adviser)
		{
			sql.append(" and adviser like "+"'%"+adviser+"%'");
		}
		//查询课题名
		if(!"".equals(bibtitle)&&null!=bibtitle)
		{
			sql.append(" and bibtitle like "+"'%"+bibtitle+"%'");
		}
		//查找本专业指导教师的毕设课题
		if(!"".equals(username)&&null!=username)
		{
			String someStu=username.substring(0, 6);
			sql.append(" and adviser like "+"'"+someStu+"%'");
		}
		//分页
		    sql.append(" limit "+pageSize*pageIndex+","+pageSize);
		try {
			pst=conn.prepareStatement(sql.toString());
			rs=pst.executeQuery();
			//查找所有已有学生选择的课题号
			pst2=conn.prepareStatement("select bibno from bibased where student!='' and center=0 ");
			rs2=pst2.executeQuery();
			String [] bibno=new String[100];
			int j=0;
			//遍历查找结果放入bibno数组
			while(rs2.next())
			{
				bibno[j]=rs2.getString("bibno");
				j++;
			}
			while(rs.next())
			{
				BiBased bibased = new BiBased();
			    bibased.setAdviser(rs.getString("adviser"));
			    bibased.setBibcontent(rs.getString("bibcontent"));
			    bibased.setBibno(rs.getString("bibno"));
			    bibased.setBibtitle(rs.getString("bibtitle"));
			    bibased.setCenter(rs.getInt("center"));
			    bibased.setStudent(rs.getString("student"));
			    list.add(bibased);
			    //删除重复的课题号（教师发布课题插入该课题，学生选择插入该课题，将集合中教师的删除，避免页面显示数据重复）
			    for(int i=0;i<j;i++)
			    {
			    	if(bibno[i].equals(rs.getString("bibno"))&&rs.getString("student").equals(""))
			    	{
			    		list.remove(bibased);
			    	}
			    }
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return list;
	}
	/**
	 * 学生选择操作
	 * @param bibased  毕设课题实例对象
	 * @return
	 */
	public boolean addSelectStuBiased(BiBased bibased)
	{
		Connection conn=JdbcUtils.getConn();
		//插入一条学生选择课题记录
		String sql="insert into bibased(bibno,bibtitle,bibcontent,adviser,student)values(?,?,?,?,?)";
		PreparedStatement pst=null;
		try {
			pst=conn.prepareStatement(sql);
			pst.setString(1, bibased.getBibno());
			pst.setString(2, bibased.getBibtitle());
			pst.setString(3, bibased.getBibcontent());
			pst.setString(4, bibased.getAdviser());
			pst.setString(5, bibased.getStudent());
			pst.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return false;
		}
		finally
		{
			try {
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
	/**
	 * 教师选题获取的数据
	 * @param pageSize
	 * @param pageIndex
	 * @param bibtitle   课题名
	 * @param username   教师账号
	 * @return    毕设课题记录集合
	 */
	public List<BiBased> getTeaBiBasedByWhere(int pageSize,int pageIndex,String bibtitle,String username)
	{
		Connection conn=JdbcUtils.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		PreparedStatement pst2=null;
		ResultSet rs2=null;
		List<BiBased> list=new ArrayList<>();
		StringBuilder sql=new StringBuilder("select * from bibased where 1=1");
		//查询课题名
		if(!"".equals(bibtitle)&&null!=bibtitle)
		{
			sql.append(" and bibtitle like "+"'%"+bibtitle+"%'");
		}
		//查找该教师的指导课题
		if(!"".equals(username)&&null!=username)
		{
			sql.append(" and adviser='"+username+"'");
		}
		//分页
		    sql.append(" limit "+pageSize*pageIndex+","+pageSize);
		try {
			//查找该教师指导的学生已选择的课题号
			pst2=conn.prepareStatement("select bibno from bibased where student!='' and adviser='"+username+"'");
			rs2=pst2.executeQuery();
			String [] bibno=new String[10];
			int j=0;
			while(rs2.next())
			{
				bibno[j]=rs2.getString("bibno");
				j++;
			}
			pst=conn.prepareStatement(sql.toString());
			rs=pst.executeQuery();
			while(rs.next())
			{
				BiBased bibased = new BiBased();
			    bibased.setAdviser(rs.getString("adviser"));
			    bibased.setBibcontent(rs.getString("bibcontent"));
			    bibased.setBibno(rs.getString("bibno"));
			    bibased.setBibtitle(rs.getString("bibtitle"));
			    bibased.setFgrade(rs.getString("fgrade"));
			    bibased.setFpleateacher(rs.getString("fpleateacher"));
			    bibased.setPapername(rs.getString("papername"));
			    bibased.setStudent(rs.getString("student"));
			    bibased.setWorkname(rs.getString("workname"));
			    bibased.setZgrade(rs.getString("zgrade"));
			    bibased.setZpleateacher(rs.getString("zpleateacher"));
			    bibased.setCenter(rs.getInt("center"));
			    list.add(bibased);
			    //将集合中重复数据删除
			    for(int i=0;i<j;i++)
			    {
			    	if(bibno[i].equals(rs.getString("bibno"))&&rs.getString("student").equals(""))
			    	{
			    		list.remove(bibased);
			    	}
			    	
			    }
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return list;
	}
	/**
	 * 教师确认
	 * @param username  选中该课题学生学号
	 * @param bibno     选中的课题号   
	 * @return
	 */
	public int certenStuBiBased(String username,String bibno)
	{
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		int count=-1;
		try {
			st=conn.createStatement();
            //设置选中行状态通过
			int i=st.executeUpdate("update bibased set center=1 where student='"+username+"' and bibno='"+bibno+"'");
			if(i==1)
			{
				//删除选中该课题的其他学生，和选中该课题学生的其他选择的课题
				count=st.executeUpdate("delete  from bibased where (student!='"+username+"' and bibno='"+bibno+"') "
						                                      + "or (student='"+username+"' and center=0)");
			}
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				st.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return count;
	}
	/**
	 * 学生是否选择成功
	 * @param username  学生学号
	 * @return
	 */
	public int selectedAlready(String username)
	{
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		ResultSet rs=null;
		int count=-1;
		try {
			st=conn.createStatement();
			//获取选中的数目，为1，说明学生选择成功
			rs=st.executeQuery("select count(center) from bibased where student='"+username+"' and center=1");
			if(rs.next())
			{
				count=rs.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				st.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return count;
	}
	/**
	 * 获取学生选择课题的个数
	 * @param username  学生学号
	 * @return
	 */
	public int getStudentCount(String username)
	{
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		ResultSet rs=null;
		int count=-1;
		try {
			st=conn.createStatement();
			rs=st.executeQuery("select count(student) from bibased where student='"+username+"'");
			if(rs.next())
			{
				count=rs.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				st.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return count;
	}
	
	/**
	 * 获取所有的信息
	 * @param pageSize
	 * @param pageIndex
	 * @param username
	 * @param sendid
	 * @param acceptid
	 * @return
	 */
	public List<Message> getAllMessageByWhere(int pageSize,int pageIndex,String username,String sendid)
	{		
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		ResultSet rs=null;
		List<Message> list=new ArrayList<>();
	    StringBuilder sb=new StringBuilder("select * from message where 1=1 ");
	    if(!"".equals(username)&&null!=username)
	    {
	    	sb.append(" and (acceptid='"+username+"' or sendid='"+username+"')" );
	    }
	    if(!"".equals(sendid)&&null!=sendid)
	    {
	    	sb.append(" and sendid like '%"+sendid+"%'" );
	    }
	        sb.append(" limit "+pageSize*pageIndex+","+pageSize);
		try {
			st=conn.createStatement();
			rs=st.executeQuery(sb.toString());
			while(rs.next())
			{
				Message message=new Message();
				message.setId(rs.getInt("id"));
				message.setSendid(rs.getString("sendid"));
				message.setReaded(rs.getInt("readed"));
				message.setSenddate(new java.util.Date(rs.getTimestamp("senddate").getTime()));
				list.add(message);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				st.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return list;
		
	}
	/**
	 * 获取查询信息
	 * @param pageSize
	 * @param pageIndex
	 * @param sendid
	 * @param acceptid
	 * @return
	 */
	public List<Message> getSomeMessageByWhere(int pageSize,int pageIndex,String sendid,String acceptid)
	{		
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		ResultSet rs=null;
		List<Message> list=new ArrayList<>();
	    StringBuilder sb=new StringBuilder("select * from message where 1=1 ");
	    if(!"".equals(sendid)&&null!=sendid)
	    {
	    	sb.append(" and sendid like '%"+sendid+"%'" );
	    }
	    if(!"".equals(acceptid)&&null!=acceptid)
	    {
	    	sb.append(" and acceptid like '%"+acceptid+"%'" );
	    }
	        sb.append(" limit "+pageSize*pageIndex+","+pageSize);
		try {
			st=conn.createStatement();
			rs=st.executeQuery(sb.toString());
			while(rs.next())
			{
				Message message=new Message();
				message.setId(rs.getInt("id"));
				message.setSendid(rs.getString("sendid"));
				message.setAcceptid(rs.getString("acceptid"));
				message.setContent(rs.getString("content"));
				message.setAppendixname(rs.getString("appendixname"));
				message.setSenddate(new java.util.Date(rs.getDate("senddate").getTime()));
				list.add(message);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				st.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return list;
		
	}
	/**
	 * 获取用户信息
	 * @param username 用户名
	 * @param degree 用户身份类别
	 * @return 用户实体
	 */
	public User getUserInfo(String username,String degree)
	{
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		ResultSet rs=null;
		User user = new User();
		try {
			st = conn.createStatement();
			rs=st.executeQuery("select * from Users where username="+username+" and "+"degree="+"'"+degree+"'");			
			while(rs.next())
			{
				user.setDept(rs.getString("dept"));
				user.setEmail(rs.getString("email"));
				user.setGender(rs.getInt("gender"));
				user.setIdnum(rs.getString("idnum"));
				user.setName(rs.getString("name"));
				user.setNation(rs.getString("nation"));
				user.setProfession(rs.getString("profession"));
				user.setTel(rs.getString("tel"));
				user.setUsercode(rs.getString("usercode"));
				user.setUsername(rs.getString("username"));	
				user.setDegree(rs.getString("degree"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				st.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return user;		
	}
	
	

	/**
	 * 获取第pageSize*pageIndex行开始的pageSize条User数据的集合
	 * @param pageSize  页面记录条数
	 * @param pageIndex 当前页码
	 * @param username  用户账号
	 * @param degree    用户身份类别
	 * @return 返回第pageSize*pageIndex行开始的pageSize条User数据的集合
	 */
	public List<User> getAllUsersByWhere(int pageSize,int pageIndex,String username , String degree)
	{		
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		ResultSet rs=null;
		List<User> list=new ArrayList<>();
	    StringBuilder sb=new StringBuilder("select * from users where 1=1 ");
	    if(!"".equals(username)&&null!=username)
	    {
	    	sb.append(" and username like '%"+username+"%'");
	    }
	    if(!"".equals(degree)&&null!=degree)
	    {
	    	sb.append(" or degree='"+degree+"'");
	 
	    }
	        sb.append(" limit "+pageSize*pageIndex+","+pageSize);
		try {
			st=conn.createStatement();
			rs=st.executeQuery(sb.toString());
			while(rs.next())
			{
				User user = new User();
				user.setDept(rs.getString("dept"));
				user.setEmail(rs.getString("email"));
				user.setGender(rs.getInt("gender"));
				user.setIdnum(rs.getString("idnum"));
				user.setName(rs.getString("name"));
				user.setNation(rs.getString("nation"));
				user.setProfession(rs.getString("profession"));
				user.setTel(rs.getString("tel"));
				user.setUsercode(rs.getString("usercode"));
				user.setUsername(rs.getString("username"));	
				user.setDegree(rs.getString("degree"));
				list.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				st.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return list;
		
	}
	
	/**
	 * 获取某张表的记录数
	 * @param tableName 表名称
	 * @return 返回某张表的记录数
	 */
	public int getTotal(String tableName) 
	{
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		ResultSet rs=null;
		StringBuilder sql=new StringBuilder("select count(*) total from "+tableName+" where 1=1");
	
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql.toString());
			while(rs.next())
			{
				return rs.getInt("total");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				st.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		return -1;
	}
	
	
	/**
	 * 删除User表记录
	 * @param username 账号
	 * @param degree   类别
	 * @return true：删除用户成功  false： 删除用户失败
	 */
	public boolean delUser(String username,String degree)
	{
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		try {
			st=conn.createStatement();
			st.executeUpdate("delete from users where username="+"'"+username+"'"+" and degree="+"'"+degree+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		finally
		{
			try {
				st.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return true;
	}
	/**
	 * 添加用户
	 * @param user User的实例对象
	 * @return true:添加成功  false:添加失败
	 */
	public boolean addUser(User user)
	{
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		PreparedStatement pst=null;
		try {
			String sql="insert into users values(?,?,?,?,?,?,?,?,?,?,?)";
			st=conn.createStatement();
			pst=conn.prepareStatement(sql);
			pst.setString(1, user.getName());
			pst.setInt(2, user.getGender());
			pst.setString(3, user.getNation());
			pst.setString(4, user.getDept());
			pst.setString(5, user.getProfession());
			pst.setString(6, user.getIdnum());
			pst.setString(7, user.getUsername());
			pst.setString(8, user.getUsercode());
			pst.setString(9, user.getEmail());
			pst.setString(10, user.getTel());
			pst.setString(11, user.getDegree());
			pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		finally
		{
			try {
				st.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return true;
	}
	/**
	 * 修改User表记录
	 * @param user User实例对象
	 * @return  true:修改成功  false：修改失败
	 */
	public boolean updateUser(User user)
	{
		
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		PreparedStatement pst=null;
		try {
			String sql="update users set name=?,gender=?,nation=?,dept=?,profession=?,idnum=?,usercode=?,email=?,tel=? where username=? and degree=?";
			st=conn.createStatement();
			pst=conn.prepareStatement(sql);
			pst.setString(1, user.getName());
			pst.setInt(2, user.getGender());
			pst.setString(3, user.getNation());
			pst.setString(4, user.getDept());
			pst.setString(5, user.getProfession());
			pst.setString(6, user.getIdnum());
			pst.setString(7, user.getUsercode());
			pst.setString(8, user.getEmail());
			pst.setString(9, user.getTel());
			pst.setString(10, user.getUsername());
			pst.setString(11, user.getDegree());
			pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		finally
		{
			try {
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return true;
	}
	/**
	 * 依据开始时间，结束时间和标题字符查找符合条件的公告
	 * @param pageSize   页面记录条数
	 * @param pageIndex  当前页码
	 * @param title      条件字符
	 * @param startTime  开始时间
	 * @param endTime    结束时间
	 * @return  返回在当前页符合条件的公告集合
	 */
	
	public List<News> getAllNewsByWhere(int pageSize,int pageIndex,String title,Date startTime , Date endTime)
	{		
		Connection conn=JdbcUtils.getConn();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Statement st=null;
		ResultSet rs=null;
		List<News> list=new ArrayList<>();
	    StringBuilder sb=new StringBuilder("select * from news where 1=1 ");
	    if(!"".equals(title)&&null!=title)
	    {
	    	sb.append(" and title like "+"'%"+title+"%'");
	    }
	    if(!"".equals(startTime)&&null!=startTime&&!"".equals(endTime)&&null!=endTime)
	    {
	    	sb.append(" and publishdate between "+"'"+sdf.format(startTime)+"'"+" and "+"'"+sdf.format(endTime)+"'");
	    }
	        sb.append(" limit "+pageSize*pageIndex+","+pageSize);
		try {
			st=conn.createStatement();
			rs=st.executeQuery(sb.toString());
			while(rs.next())
			{
				News news = new News();
			    news.setNewid(rs.getInt("newid"));
			    news.setTitle(rs.getString("title"));
			    news.setPublisher(rs.getString("publisher"));
			    news.setPublishdate(new java.util.Date(rs.getDate("publishdate").getTime()));
				list.add(news);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				st.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return list;
		
	}
	
	
	
	
	/**
	 *  删除BiBased或News表记录根据表名
	 * @param tableName 表明称
	 * @param columnName 列名称
	 * @param delScope 删除范围
	 * @return true：删除成功 false：删除失败
	 */
	public boolean delRecord(String tableName,String columnName,String delScope)
	{
		Connection conn=JdbcUtils.getConn();
		try {
			conn.createStatement().executeUpdate("delete from "+tableName+" where "+columnName+" in ("+delScope+")");
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		finally
		{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 添加毕设题目
	 * @param bibased  BiBased实例对象
	 * @return true：添加成功 false：添加失败
	 */
	public boolean addBiased(BiBased bibased)
	{
		Connection conn=JdbcUtils.getConn();
		String sql="insert into bibased(bibno,bibtitle,bibcontent,adviser)values(?,?,?,?)";
		PreparedStatement pst=null;
		try {
			pst=conn.prepareStatement(sql);
			pst.setString(1, bibased.getBibno());
			pst.setString(2, bibased.getBibtitle());
			pst.setString(3, bibased.getBibcontent());
			pst.setString(4, bibased.getAdviser());
			pst.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		finally
		{
			try {
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
	/**
	 * 根据用户名查找BiBased记录，在当前页
	 * @param pageSize    页面记录条数
	 * @param pageIndex   当前页面码
	 * @param username    用户账号
	 * @return 返回当前页符合用户名的BiBased记录
	 */
	public List<BiBased> getAllBiBasedByWhere(int pageSize,int pageIndex,String adviser,String student)
	{
		Connection conn=JdbcUtils.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		List<BiBased> list=new ArrayList<>();
		StringBuilder sql=new StringBuilder("select * from bibased where 1=1");
		if(!"".equals(adviser)&&null!=adviser)
		{
			sql.append(" and adviser like "+"'%"+adviser+"%'");
		}
		if(!"".equals(student)&&null!=student)
		{
			sql.append(" and student like "+"'%"+student+"%'");
		}
		    sql.append(" limit "+pageSize*pageIndex+","+pageSize);
		try {
			pst=conn.prepareStatement(sql.toString());
			rs=pst.executeQuery();
			while(rs.next())
			{
				BiBased bibased = new BiBased();
			    bibased.setAdviser(rs.getString("adviser"));
			    bibased.setBibcontent(rs.getString("bibcontent"));
			    bibased.setBibno(rs.getString("bibno"));
			    bibased.setBibtitle(rs.getString("bibtitle"));
			    bibased.setFgrade(rs.getString("fgrade"));
			    bibased.setFpleateacher(rs.getString("fpleateacher"));
			    bibased.setPapername(rs.getString("papername"));
			    bibased.setStudent(rs.getString("student"));
			    bibased.setWorkname(rs.getString("workname"));
			    bibased.setZgrade(rs.getString("zgrade"));
			    bibased.setZpleateacher(rs.getString("zpleateacher"));
				list.add(bibased);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return list;
	}
	
	
	
	

	/**
	 * @param adviser 指导老师
	 * @return 与当前指导老师相关的所有毕设信息
	 */
	public List<BiBased> getMyAllStudent(String adviser)
	{
		Connection conn=JdbcUtils.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		List<BiBased> list=new ArrayList<>();
		StringBuilder sql=new StringBuilder("select * from bibased where adviser="+"'"+adviser+"'"+" and center=1");
		
		try {
			pst=conn.prepareStatement(sql.toString());
			rs=pst.executeQuery();
			while(rs.next())
			{
				BiBased bibased = new BiBased();
			    bibased.setAdviser(rs.getString("adviser"));
			    bibased.setBibcontent(rs.getString("bibcontent"));
			    bibased.setBibno(rs.getString("bibno"));
			    bibased.setBibtitle(rs.getString("bibtitle"));
			    bibased.setFgrade(rs.getString("fgrade"));
			    bibased.setFpleateacher(rs.getString("fpleateacher"));
			    bibased.setPapername(rs.getString("papername"));
			    bibased.setStudent(rs.getString("student"));
			    bibased.setWorkname(rs.getString("workname"));
			    bibased.setZgrade(rs.getString("zgrade"));
			    bibased.setZpleateacher(rs.getString("zpleateacher"));
				list.add(bibased);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		return list;
	}
	/**
	 * 获取一条BiBased 记录依据bibno
	 * @param bibno 毕设编号
	 * @return 毕设记录
	 */
	public BiBased getBiBased(String bibno)
	{
		Connection conn=JdbcUtils.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		BiBased bibased = new BiBased();
		StringBuilder sql=new StringBuilder("select * from bibased where bibno=?");		
		try {
			pst=conn.prepareStatement(sql.toString());
			pst.setString(1, bibno);
			rs=pst.executeQuery();
			while(rs.next())
			{				
			    bibased.setAdviser(rs.getString("adviser"));
			    bibased.setBibcontent(rs.getString("bibcontent"));
			    bibased.setBibno(rs.getString("bibno"));
			    bibased.setBibtitle(rs.getString("bibtitle"));
			    bibased.setFgrade(rs.getString("fgrade"));
			    bibased.setFpleateacher(rs.getString("fpleateacher"));
			    bibased.setPapername(rs.getString("papername"));
			    bibased.setStudent(rs.getString("student"));
			    bibased.setWorkname(rs.getString("workname"));
			    bibased.setZgrade(rs.getString("zgrade"));
			    bibased.setZpleateacher(rs.getString("zpleateacher"));			
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return bibased;
	}
	
	/**
	 * 修改BiBased的记录
	 * @param biBased BiBased的实例对象
	 * @return true:修改成功 false：修改失败
	 */
	
	public boolean updateBiBased(BiBased biBased)
	{
		
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		PreparedStatement pst=null;
		try {
			String sql="update bibased set bibtitle=?,bibcontent=?,adviser=?,student=?,zpleateacher=?,fpleateacher=?,zgrade=?,fgrade=? where  bibno=?";
			st=conn.createStatement();
			pst=conn.prepareStatement(sql);
			pst.setString(1, biBased.getBibtitle());
			pst.setString(2, biBased.getBibcontent());
			pst.setString(3, biBased.getAdviser());
			pst.setString(4, biBased.getStudent());
			pst.setString(5, biBased.getZpleateacher());
			pst.setString(6, biBased.getFpleateacher());
			pst.setString(7, biBased.getZgrade());
			pst.setString(8, biBased.getFgrade());
			pst.setString(9, biBased.getBibno());
			pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		finally
		{
			try {
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return true;
	}
	
	/** 
	 * 获取第pageSize*pageIndex行开始的pageSize条BiBased数据的集合即学生的选题列表
	 * @param pageSize      页面个数
	 * @param pageIndex     当前页面
	 * @param adviser       指导教师
	 * @param bibtitle      毕设题目
	 * @param username      学生学号
	 * @param selected      是否已选中课题
	 * @return   返回第pageSize*pageIndex行开始的pageSize条BiBased数据的集合 
	 */
	public List<BiBased> getAllChooseBiBasedByWhere(int pageSize,int pageIndex,String adviser,String bibtitle,String username,String selected)
	{
		Connection conn=JdbcUtils.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		PreparedStatement pst2=null;
		ResultSet rs2=null;
		List<BiBased> list=new ArrayList<>();
		//查找课题号不重复的毕设课题记录
		StringBuilder sql=new StringBuilder("select distinct(bibno),bibtitle,bibcontent,adviser,center from bibased  where 1=1");
		//查询指导教师
		if(!"".equals(adviser)&&null!=adviser)
		{
			sql.append(" and adviser like "+"'%"+adviser+"%'");
		}
		//查询课题名
		if(!"".equals(bibtitle)&&null!=bibtitle)
		{
			sql.append(" and bibtitle like "+"'%"+bibtitle+"%'");
		}
		//查找本专业的指导教师指导的毕设题目
		if(!"".equals(username)&&null!=username)
		{
			String someStu=username.substring(0, 6);
			sql.append(" and adviser like "+"'"+someStu+"%'");
		}
		if(!"".equals(selected)&&null!=selected)
		{
			//选题未成功，查找未被通过的课题
			if(selected.equals("not"))
			{
				sql.append(" and center=0");	
			}
			//选题成功只查询个人的课题
			else if(selected.equals("yes"))
			{
				sql.append(" and student='"+username+"'");
			}
		}
		//分页
		    sql.append(" limit "+pageSize*pageIndex+","+pageSize);
		try {
			pst=conn.prepareStatement(sql.toString());
			rs=pst.executeQuery();
			//查找学生选择的课题号
			pst2=conn.prepareStatement("select bibno from bibased where student='"+username+"'");
			rs2=pst2.executeQuery();
			String [] bibno=new String[3];
			int j=0;
		    while(rs2.next())
		    {
		    	bibno[j]=rs2.getString("bibno");
		    	j++;
		    }
			while(rs.next())
			{
				BiBased bibased = new BiBased();
			    bibased.setAdviser(rs.getString("adviser"));
			    bibased.setBibcontent(rs.getString("bibcontent"));
			    bibased.setBibno(rs.getString("bibno"));
			    bibased.setBibtitle(rs.getString("bibtitle"));
			    bibased.setCenter(rs.getInt("center"));
			    //若课题号对应，将该学生学号赋值
			    for(int i=0;i<j;i++)
			    {
			    	if(bibno[i].equals(rs.getString("bibno")))
			    	{
			    		bibased.setStudent(username);
			    	}
			    }
				list.add(bibased);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return list;
	}

	/**
	 * 取消选择操作
	 * @param bibno  毕设编号
	 * @param student  学生学号
	 * @return  true：成功  flase:失败
	 */
	public boolean unSelectBiBased(String bibno,String student)
	{		
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		PreparedStatement pst=null;
		try {
			//删除此条选择记录
			String sql="delete from  bibased where student=? and bibno=?";
			st=conn.createStatement();
			pst=conn.prepareStatement(sql);
			pst.setString(1, student);
			pst.setString(2, bibno);
			pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		finally
		{
			try {
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return true;
	}
	
	/**
	 * 依据学生账号查找BiBased记录
	 * @param student 学生账号
	 * @return 返回此学生的毕设信息记录
	 */
	public BiBased getMyBiBased(String student)
	{
		Connection conn=JdbcUtils.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		StringBuilder sql=new StringBuilder("select * from bibased where student="+student+" and center=1");
		BiBased bibased = new BiBased();
		try {
			pst=conn.prepareStatement(sql.toString());
			rs=pst.executeQuery();
			while(rs.next())
			{
				
			    bibased.setAdviser(rs.getString("adviser"));
			    bibased.setBibcontent(rs.getString("bibcontent"));
			    bibased.setBibno(rs.getString("bibno"));
			    bibased.setBibtitle(rs.getString("bibtitle"));
			    bibased.setFgrade(rs.getString("fgrade"));
			    bibased.setFpleateacher(rs.getString("fpleateacher"));
			    bibased.setPapername(rs.getString("papername"));
			    bibased.setStudent(rs.getString("student"));
			    bibased.setWorkname(rs.getString("workname"));
			    bibased.setZgrade(rs.getString("zgrade"));
			    bibased.setZpleateacher(rs.getString("zpleateacher"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return bibased;
	}
	
	
	 /**
	  * 用户验证
	  * @param username 账号
	  * @param usercode 密码
	  * @param userdegree 类别
	  * @return true:验证成功  false:验证失败
	  */
	public boolean Login(String username,String usercode,String userdegree)
	{
		Connection conn=JdbcUtils.getConn();
		String sql="select * from users where username=? and usercode=? and degree=?";
		PreparedStatement pst=null;
	    ResultSet rs=null;
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, username);
			pst.setString(2, usercode);
			pst.setString(3, userdegree);
			rs=pst.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		finally
		{
			try {
				rs.close();
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
				
	}
	/**
	 * 保存学生权限
	 * @param list  权限集合
	 * @return true：添加成功  flase：添加失败
	 */
	public boolean saveStudentDegree(List<Degree> list)
	{
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		try {
		   st=conn.createStatement();
		   st.executeUpdate("delete from studentdegree");
		   for(Degree degree:list)
		   {
			   st.executeUpdate("insert into studentdegree values("+"'"+degree.getId()+"'"+",'"+degree.getText()+"'"+")");
		   }
		   return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 保存教师权限
	 * @param list  权限集合
	 * @return true：添加成功  flase：添加失败
	 */
	public boolean saveTeacherDegree(List<Degree> list)
	{
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		try {
		   st=conn.createStatement();
		   st.executeUpdate("delete from teacherdegree");
		   for(Degree degree:list)
		   {
			   st.executeUpdate("insert into teacherdegree values("+"'"+degree.getId()+"'"+",'"+degree.getText()+"'"+")");
		   }
		   return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 获取学生的权限集合
	 * @return 返回学生的权限集合
	 */
	public List<Degree> getStudentDegree()
	{
		Connection conn=JdbcUtils.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		List<Degree> list=new ArrayList<>();
		StringBuilder sql=new StringBuilder("select * from studentdegree where 1=1");
		try {
			pst=conn.prepareStatement(sql.toString());
			rs=pst.executeQuery();
			while(rs.next())
			{
				Degree degree = new Degree();
			    degree.setId(rs.getString("id"));
			    degree.setText(rs.getString("text"));
				list.add(degree);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return list;
	}
	
	/**
	 * 获取教师的权限集合
	 * @return 返回教师的权限集合
	 */
	public List<Degree> getTeacherDegree()
	{
		Connection conn=JdbcUtils.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		List<Degree> list=new ArrayList<>();
		StringBuilder sql=new StringBuilder("select * from teacherdegree where 1=1");
		try {
			pst=conn.prepareStatement(sql.toString());
			rs=pst.executeQuery();
			while(rs.next())
			{
				Degree degree = new Degree();
			    degree.setId(rs.getString("id"));
			    degree.setText(rs.getString("text"));
				list.add(degree);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return list;
	}
	
}
