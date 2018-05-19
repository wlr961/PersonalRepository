package com.wlr.bibased.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alibaba.fastjson.JSON;
import com.wlr.bibased.bizlogic.WlrBiBasedService;
import com.wlr.bibased.model.News;
import com.wlr.bibased.utils.JdbcUtils;

/**
 * Servlet implementation class NewsAction
 */
@WebServlet("/NewsAction")
public class NewsAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 // 保存文件的目录
	private static String PATH_FOLDER = "/";
	// 存放临时文件的目录
	private static String TEMP_FOLDER = "/";   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewsAction() {
        super();
        // TODO Auto-generated constructor stub
    }
    @Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext servletCtx = config.getServletContext();			
		// 初始化路径
		// 保存文件的目录		
		PATH_FOLDER = servletCtx.getRealPath("/upload");
		// 存放临时文件的目录,存放xxx.tmp文件的目录
		TEMP_FOLDER = servletCtx.getRealPath("/uploadTemp");
		File file1=new File(PATH_FOLDER);
		if(!file1.exists())
		{
			file1.mkdirs();
		}
		File file2=new File(TEMP_FOLDER);
		if(!file2.exists())
		{
			file2.mkdirs();
		}
		
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String flag=request.getParameter("flag");

		
		if(flag.equals("addNews"))
		{
			String pulisher=request.getParameter("username");
	        DiskFileItemFactory factory = new DiskFileItemFactory();	
			// 如果没以下两行设置的话，上传大的 文件 会占用 很多内存，
			// 设置暂时存放的 存储室 , 这个存储室，可以和 最终存储文件 的目录不同
			/**
			 * 原理 它是先存到 暂时存储室，然后在真正写到 对应目录的硬盘上， 按理来说 当上传一个文件时，其实是上传了两份，第一个是以 .tem
			 * 格式的 然后再将其真正写到 对应目录的硬盘上
			 */
			factory.setRepository(new File(TEMP_FOLDER));
			// 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
			factory.setSizeThreshold(1024 * 1024);
			// 高水平的API文件上传处理
			ServletFileUpload upload = new ServletFileUpload(factory);		
			try {
				// 提交上来的信息都在这个list里面
				// 这意味着可以上传多个文件
				// 请自行组织代码
				List<FileItem> list = upload.parseRequest(request);
				// 获取上传的文件
				FileItem item = getUploadFileItem(list);
				// 获取文件名
				String filename = getUploadFileName(item);
				// 保存到缓存的暂时存储室的文件名
				String saveName = new Date().getTime() + filename.substring(filename.lastIndexOf("."));
				// 保存后的浏览器访问路径  协议，主机名，端口号，项目名
				String picUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/upload/"+saveName;
				
				System.out.println("存放目录:" + PATH_FOLDER);
				System.out.println("文件名:" + filename);
				System.out.println("浏览器访问路径:" + picUrl);
				File file=new File(PATH_FOLDER, saveName);	
				// 真正写到磁盘上
				item.write(new File(PATH_FOLDER, saveName)); 
				Connection conn=JdbcUtils.getConn();
				String sql="insert into news(title,content,publishdate,publisher)values(?,?,?,?)";
				java.sql.PreparedStatement pst =conn.prepareStatement(sql);
				FileInputStream in =new FileInputStream(file);				
				pst.setString(1, filename);
				pst.setBlob(2, in);
				pst.setDate(3, new java.sql.Date(new Date().getTime()));
				pst.setString(4, pulisher);
				pst.executeUpdate();			
				PrintWriter writer = response.getWriter();	
				writer.print("{");
				writer.print("msg:\"文件大小:"+item.getSize()+",文件名:"+filename+"\"");
				writer.print(",picUrl:\"" + picUrl + "\"");
				writer.print("}");	
				writer.close();		
			} catch (FileUploadException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(flag.equals("delNews"))
		{
			String delScope	= request.getParameter("data");
			WlrBiBasedService service=new WlrBiBasedService();
			service.delRecord("news", "newid", delScope);
		}
		else if(flag.equals("selectNews"))
		{
			 WlrBiBasedService service=new WlrBiBasedService();
			 List<News> list=null;
			 int pageSize=Integer.parseInt(request.getParameter("pageSize"));
		     int pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
		     int total= service.getTotal("News");
		     String title=request.getParameter("title");
		     String startTime=request.getParameter("startTime");
		     String endTime=request.getParameter("endTime");
		     if(!"".equals(startTime)&&null!=startTime&&!"".equals(endTime)&&null!=endTime)
			    {
		    	 //获取符合开始时间，结束时间和标题字符的公告集合
		    	list=service.getAllNewsByWhere(pageSize, pageIndex, title, new Date(startTime), new Date(endTime));
			    }
		     else
		     {
		    	 //获取符合标题字符的公告集合
		    	 list=service.getAllNewsByWhere(pageSize, pageIndex, title,null, null);
		     }
		     HashMap<String, Object> map=new HashMap<>();
		     map.put("total", total);
		     map.put("data", list);
		     String jsonResult=JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd");
		     response.getWriter().write(jsonResult);
		}
		else if(flag.equals("downNews"))
		{
			String newid=request.getParameter("newid");
			Blob content=null;
			String title=null;
			
			OutputStream out = response.getOutputStream();	
			Connection conn=JdbcUtils.getConn();
			Statement st=null;
			ResultSet rs=null;
			try {
				st = conn.createStatement();
				rs =st.executeQuery("select title,content from news where newid="+"'"+newid+"'");
				while(rs.next())
				{
					content=rs.getBlob("content");
					title=rs.getString("title");
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
				in = content.getBinaryStream();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
			Date date = new Date();
			long time = date.getTime();
			// 设置响应头的MIME类型
			response.setContentType("application/force-download");
			response.setHeader("Content-Length",String.valueOf(in.available()));
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(title.getBytes("utf-8"),"iso-8859-1"));		 
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
	private String getUploadFileName(FileItem item) {
		// 获取路径名
		String value = item.getName();
		// 索引到最后一个反斜杠
		int start = value.lastIndexOf("/");
		// 截取 上传文件的 字符串名字，加1是 去掉反斜杠，
		String filename = value.substring(start + 1);		
		return filename;
	}
	//获取文件
	private FileItem getUploadFileItem(List<FileItem> list) {
		for (FileItem fileItem : list) {
			if(!fileItem.isFormField()) {
				return fileItem;
			}
		}
		return null;
	}
}
