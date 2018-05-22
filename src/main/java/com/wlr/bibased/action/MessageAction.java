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
import com.wlr.bibased.model.User;

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
		    
		     String username=request.getParameter("username");
		     String sendid=request.getParameter("sendid");
		     list=service.getAllMessageByWhere(pageSize,pageIndex,username,sendid);
		     int total= list.size();
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
		else if(flag.equals("selectedAlready"))
		{
			WlrBiBasedService service=new WlrBiBasedService();
			 String username=request.getParameter("username");
			 int count=service.selectedAlready(username);
			 String adviser=service.selectedAdvserAlready(username);
		     HashMap<String, Object> map=new HashMap<>();
		     map.put("count", count);
		     map.put("adviser", adviser);
		     String jsonResult=JSON.toJSONString(map);
		     response.getWriter().write(jsonResult);
		}
		else if(flag.equals("addMessage"))
		{
			WlrBiBasedService service=new WlrBiBasedService();
			 String username=request.getParameter("username");
			 String json2Obj=request.getParameter("submitData");
			 Message mess=JSON.parseObject(json2Obj,Message.class);
			 String adviser=request.getParameter("adviser");
			 String student=request.getParameter("acceptid");
			 int messid=service.addMessage(username, adviser, mess.getContent(),student);
			 String jsonResult=JSON.toJSONString(messid);
		     response.getWriter().write(jsonResult);
		}

		else if(flag.equals("delMessage"))
		{
			WlrBiBasedService service=new WlrBiBasedService();
			String str=request.getParameter("data");
			String[] strs=str.split(",");
			for(int i=0;i<strs.length;i++)
			{
				service.delMessage(Integer.parseInt(strs[i]));
			}
			
		}
		else if(flag.equals("lookMess"))
		{
			WlrBiBasedService service=new WlrBiBasedService();
			String messid=request.getParameter("id");
			String username=request.getParameter("username");
			Message mess =service.getMessInfo(Integer.parseInt(messid),username);
			String jsonResult=JSON.toJSONStringWithDateFormat(mess, "yyyy-MM-dd HH:mm:ss");
		    response.getWriter().write(jsonResult);
		}
		else if(flag.equals("selectedAdviserAlready"))
		{
			WlrBiBasedService service=new WlrBiBasedService();
			 String username=request.getParameter("username");
			 List<BiBased> list=service.getMyAllStudent(username);
			 //int count=student.size();
		     //HashMap<String, string> map=new HashMap<>();
		    // map.put("count", count);
		     //map.put("data", student);
		     String jsonResult=JSON.toJSONString(list);
		     response.getWriter().write(jsonResult);
		}
		else if(flag.equals("selectTeaAlready"))
		{
			WlrBiBasedService service=new WlrBiBasedService();
			 String username=request.getParameter("username");
			 List<BiBased> list=service.getMyAllStudent(username);
			 int count=list.size();
		     String jsonResult=JSON.toJSONString(count);
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
