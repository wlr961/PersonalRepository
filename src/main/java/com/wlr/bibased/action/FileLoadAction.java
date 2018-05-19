package com.wlr.bibased.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wlr.bibased.utils.JdbcUtils;

/**
 * Servlet implementation class FileAction
 */
@WebServlet("/FileLoadAction")
public class FileLoadAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileLoadAction() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String flag=request.getParameter("flag");
		if(flag.equals("downPaper"))
		{
			String bibno=request.getParameter("bibno");
			Blob paperBlob=null;
			String paperName=null;
			
			OutputStream out = response.getOutputStream();	
			Connection conn=JdbcUtils.getConn();
			Statement st=null;
			ResultSet rs=null;
			try {
				st = conn.createStatement();
				rs =st.executeQuery("select paper,papername from bibased where bibno="+"'"+bibno+"'");
				while(rs.next())
				{
					paperBlob=rs.getBlob("paper");
					paperName=rs.getString("papername");
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
			
			InputStream in=null;
			try {
				//将longBlob类型转换为二进制流
				in = paperBlob.getBinaryStream();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
			Date date = new Date();
			long time = date.getTime();
			// 设置响应头的MIME类型
			response.setContentType("application/force-download");
			response.setHeader("Content-Length",String.valueOf(in.available()));
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(paperName.getBytes("utf-8"),"gb2312"));		 
			int i = 0;
			byte[] b = new byte[1024];
			while ((i = in.read(b)) != -1)
			{

			     out.write(b, 0, i);
			}
			in.close();

			out.close();
		}
		else if(flag.equals("downWork"))
		{
			String bibno=request.getParameter("bibno");
			Blob workBlob=null;
			String workName=null;
			OutputStream out = response.getOutputStream();	
			Connection conn=JdbcUtils.getConn();
			Statement st=null;
			ResultSet rs=null;
			try {
				st = conn.createStatement();
				rs =st.executeQuery("select work,workname from bibased where bibno="+"'"+bibno+"'");
				while(rs.next())
				{
					workBlob=rs.getBlob("work");
					workName=rs.getString("workname");
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
			
			InputStream in=null;
			try {
				in = workBlob.getBinaryStream();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
			Date date = new Date();
			long time = date.getTime();
			// 设置响应头的MIME类型
			response.setContentType("application/force-download");
			response.setHeader("Content-Length",String.valueOf(in.available()));
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(workName.getBytes("utf-8"),"gb2312"));		 
			int i = 0;
			byte[] b = new byte[1024];
			while ((i = in.read(b)) != -1)
			{

			     out.write(b, 0, i);
			}
			in.close();

			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
