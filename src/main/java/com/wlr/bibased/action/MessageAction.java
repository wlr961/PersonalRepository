package com.wlr.bibased.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.wlr.bibased.bizlogic.WlrBiBasedService;
import com.wlr.bibased.model.BiBased;
import com.wlr.bibased.model.Message;

/**
 * Servlet implementation class MessageAction
 */
@WebServlet("/MessageAction")
public class MessageAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MessageAction() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String flag=request.getParameter("flag");
		if(flag.equals("selectMessage"))
		{
			WlrBiBasedService service=new WlrBiBasedService();
			 List<Message> list=null;
			 int pageSize=Integer.parseInt(request.getParameter("pageSize"));
		     int pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
		     //String username=request.getParameter("username");
		     int total= service.getTotal("message");
		     String username=request.getParameter("username");
		     String sendid=request.getParameter("key1");
		     String acceptid=request.getParameter("key2");
		     //根据用户名获取bibased的记录的集合
		     list=service.getAllMessageByWhere(pageSize,pageIndex,username,sendid,acceptid);
		     HashMap<String, Object> map=new HashMap<>();
		     map.put("total", total);
		     map.put("data", list);
		     String jsonResult=JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd HH:mm:ss");
		     response.getWriter().write(jsonResult);
		}
		else if(flag.equals("selectSomeMessage"))
		{
			WlrBiBasedService service=new WlrBiBasedService();
			 List<Message> list=null;
			 int pageSize=Integer.parseInt(request.getParameter("pageSize"));
		     int pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
		     int total= service.getTotal("message");
		     String sendid=request.getParameter("key1");
		     String acceptid=request.getParameter("key2");
		     //根据用户名获取bibased的记录的集合
		     list=service.getSomeMessageByWhere(pageSize,pageIndex,sendid,acceptid);
		     HashMap<String, Object> map=new HashMap<>();
		     map.put("total", total);
		     map.put("data", list);
		     String jsonResult=JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd-hh-mm-ss");
		     response.getWriter().write(jsonResult);
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
