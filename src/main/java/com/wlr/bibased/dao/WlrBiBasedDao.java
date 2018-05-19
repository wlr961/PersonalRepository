package com.wlr.bibased.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	
	public boolean addSelectStuBiased(BiBased bibased)
	{
		Connection conn=JdbcUtils.getConn();
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
	public List<BiBased> getTeaBiBasedByWhere(int pageSize,int pageIndex,String adviser)
	{
		Connection conn=JdbcUtils.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		List<BiBased> list=new ArrayList<>();
		//2018 where 1=1
		StringBuilder sql=new StringBuilder("select * from bibased where 1=1");
		if(!"".equals(adviser)&&null!=adviser)
		{
			sql.append(" and adviser='"+adviser+"'");
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
	 * 教师确认
	 * @param username
	 * @param bibno
	 * @return
	 */
	public int certenStuBiBased(String username,String bibno)
	{
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		int count=-1;
		try {
			st=conn.createStatement();

			int i=st.executeUpdate("update bibased set center=1 where student='"+username+"' and bibno='"+bibno+"'");
			if(i==1)
			{
				count=st.executeUpdate("delete  from bibased where student!='"+username+"' and bibno='"+bibno+"'");
				count=st.executeUpdate("delete  from bibased where student='"+username+"' and center=null");
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
	 * @param username
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
	 * @param username
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
	public List<Message> getAllMessageByWhere(int pageSize,int pageIndex,String username,String sendid,String acceptid)
	{		
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		ResultSet rs=null;
		List<Message> list=new ArrayList<>();
	    StringBuilder sb=new StringBuilder("select * from message where 1=1 ");
	    if(!"".equals(username)&&null!=username)
	    {
	    	sb.append(" or acceptid='"+username+"' or sendid='"+username+"'" );
	    }
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
	//2018
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
	 * 2018
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
	    	sb.append(" and datetime between "+sdf.format(startTime)+" and "+sdf.format(endTime));
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
	 * 2018
	 * 获取第pageSize*pageIndex行开始的pageSize条BiBased数据的集合
	 * @param pageSize   页面个数
	 * @param pageIndex  当前页面
	 * @param adviser    指导教师
	 * @param bibtitle   毕设题目
	 * @return  返回第pageSize*pageIndex行开始的pageSize条BiBased数据的集合
	 */
	public List<BiBased> getAllChooseBiBasedByWhere(int pageSize,int pageIndex,String adviser,String bibtitle,String username,String selected)
	{
		Connection conn=JdbcUtils.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		List<BiBased> list=new ArrayList<>();
		//2018 where 1=1
		StringBuilder sql=new StringBuilder("select * from bibased where 1=1");
		if(!"".equals(adviser)&&null!=adviser)
		{
			sql.append(" and adviser like "+"'%"+adviser+"%'");
		}
		if(!"".equals(bibtitle)&&null!=bibtitle)
		{
			sql.append(" and bibtitle like "+"'%"+bibtitle+"%'");
		}
		if(!"".equals(username)&&null!=username)
		{
			if(!"".equals(selected)&&null!=selected)
			{
				sql.append(" and center=0");	
			}
			String someStu=username.substring(0, 6);
			sql.append(" and adviser like "+"'"+someStu+"%'");
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
	 * 修改毕设的学生
	 * @param bibno    毕设编号
	 * @param student  学生账号
	 * @return   true:修改成功  false：修改失败
	
	public boolean selectBiBased(String bibno,String student)
	{		
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		PreparedStatement pst=null;
		try {
			String sql="update bibased set student=? where  bibno=?";
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
	 */
	/**
	 * 2018
	 * 取消选择操作，将学生列设为null
	 * @param bibno  毕设编号
	 * @param student  学生学号
	 * @return  true：修改成功  flase:修改失败
	 */
	public boolean unSelectBiBased(String bibno,String student)
	{		
		Connection conn=JdbcUtils.getConn();
		Statement st=null;
		PreparedStatement pst=null;
		try {
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
		StringBuilder sql=new StringBuilder("select * from bibased where student="+student);
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
		//2018 "select * from users where username=? and usercode=? and degree=?";
		String sql="select * from users where username=? and usercode=? and degree=?";
		// 2018 PreparedStatement pst=null;
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
