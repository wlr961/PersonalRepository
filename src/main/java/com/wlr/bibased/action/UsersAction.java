package com.wlr.bibased.action;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.wlr.bibased.bizlogic.WlrBiBasedService;
import com.wlr.bibased.model.User;

/**
 * Servlet implementation class UsersAction
 */
@WebServlet("/UsersAction")
public class UsersAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UsersAction() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String flag=request.getParameter("flag");
		if(flag.equals("delUsers"))
		{
			WlrBiBasedService service=new WlrBiBasedService();
			String str=request.getParameter("data");
			String[] strs=str.split(",");
			for(int i=0;i<strs.length;i++)
			{
				String st=strs[i];
				//正则表达式，以“+”分割
				String[] scope=st.split("\\+");
				service.delUser(scope[0], scope[1]);
			}
		}
		else if(flag.equals("addUsers"))
		{
			WlrBiBasedService service=new WlrBiBasedService();
			String json2Obj=request.getParameter("submitData");
			//将json数据转换为BiBased
			User user=JSON.parseObject(json2Obj,User.class);
			service.addUser(user);
			
		}
		else if (flag.equals("editUser"))
		{
			String json2obj=request.getParameter("data");
			User user=JSON.parseObject(json2obj, User.class);
			WlrBiBasedService service =new WlrBiBasedService();
			service.updateUser(user);
		}
		else if(flag.equals("selectUser"))
		{
			 WlrBiBasedService service=new WlrBiBasedService();
			 int pageSize=Integer.parseInt(request.getParameter("pageSize"));
		     int pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
		     int total= service.getTotal("Users");
		     String username=request.getParameter("key1");
		     String degree=request.getParameter("key2");
		     List<User> list=service.getAllUsersByWhere(pageSize, pageIndex, username, degree);
		     HashMap<String, Object> map=new HashMap<>();
		     map.put("total", total);
		     map.put("data", list);
		     String jsonResult=JSON.toJSONString(map);
		     response.getWriter().write(jsonResult);
		}
		else if(flag.equals("selectInfoUser"))
		{
			String username=request.getParameter("username");
			String degree=request.getParameter("userdegree");
			WlrBiBasedService service=new WlrBiBasedService();
			User user =service.getUserInfo(username, degree);
			String str=JSON.toJSONString(user);
			response.getWriter().write(str);
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
