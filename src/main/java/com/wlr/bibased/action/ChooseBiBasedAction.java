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

/**
 * Servlet implementation class ChooseBiBasedAction
 */
@WebServlet("/ChooseBiBasedAction")
public class ChooseBiBasedAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChooseBiBasedAction() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String flag=request.getParameter("flag");
		//学生的选课题列表
		if(flag.equals("selectBiBased"))
		{
			 WlrBiBasedService service=new WlrBiBasedService();
			 List<BiBased> list=null;
			 int pageSize=Integer.parseInt(request.getParameter("pageSize"));
		     int pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
		     //获取bibased表的记录总数
		     String username=request.getParameter("username");
		     String selected=request.getParameter("selected");
		     String adviser=request.getParameter("adviser");
		     String bibtitle=request.getParameter("bibtitle");	
		     //获取页面要显示的bibased数据
	         list=service.getAllChooseBiBasedByWhere(pageSize, pageIndex, adviser, bibtitle,username,selected);
	         int total= list.size();
		     HashMap<String, Object> map=new HashMap<>();
		     map.put("total", total);
		     map.put("data", list);
		     //map转换为json再到String
		     String jsonResult=JSON.toJSONString(map);
		     response.getWriter().write(jsonResult);
		}
		//教师的选课题列表
		else if(flag.equals("selectTeaBiBased"))
		{
			 WlrBiBasedService service=new WlrBiBasedService();
			 List<BiBased> list=null;
			 int pageSize=Integer.parseInt(request.getParameter("pageSize"));
		     int pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
		     String username=request.getParameter("username");
		     String bibtitle=request.getParameter("bibtitle");
		     //获取页面要显示的bibased数据
	         list=service.getTeaBiBasedByWhere(pageSize, pageIndex,bibtitle,username);
	         int total= list.size();
		     HashMap<String, Object> map=new HashMap<>();
		     map.put("total", total);
		     map.put("data", list);
		     //map转换为json再到String
		     String jsonResult=JSON.toJSONString(map);
		     response.getWriter().write(jsonResult);
		}
		//管理员的选课题列表
		else if(flag.equals("selectManBiBased"))
		{
			 WlrBiBasedService service=new WlrBiBasedService();
			 List<BiBased> list=null;
			 int pageSize=Integer.parseInt(request.getParameter("pageSize"));
		     int pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
		     //获取bibased表的记录总数
		     String username=request.getParameter("username");
		     String adviser=request.getParameter("adviser");
		     String bibtitle=request.getParameter("bibtitle");	
		     //获取页面要显示的bibased数据
	         list=service.getManBiBasedByWhere(pageSize,pageIndex,adviser,bibtitle,username);
	         int total= list.size();
		     HashMap<String, Object> map=new HashMap<>();
		     map.put("total", total);
		     map.put("data", list);
		     //map转换为json再到String
		     String jsonResult=JSON.toJSONString(map);
		     response.getWriter().write(jsonResult);
		}
		//学生选择课题
		else if(flag.equals("addStuBiBased"))
		{
			String json2Obj=request.getParameter("data");
			//将json数据转换为BiBased
			BiBased bibased=JSON.parseObject(json2Obj,BiBased.class);
			bibased.setStudent(request.getParameter("selecter"));
			WlrBiBasedService service=new WlrBiBasedService();
			service.addSelectStuBiased(bibased);
		}
		//学生取消选择课题
		else if(flag.equals("cancelStuBiBased"))
		{
			String bibno=request.getParameter("data");
			String student=request.getParameter("selecter");
			WlrBiBasedService service=new WlrBiBasedService();
			service.unSelectBiBased(bibno, student);
		}
		//学生选择课题的数目
		else if(flag.equals("selectCount"))
		{
			String username=request.getParameter("username");
			WlrBiBasedService service=new WlrBiBasedService();
			int count=service.getStudentCount(username);
			String jsonResult=JSON.toJSONString(count);
			response.getWriter().write(jsonResult);
		}
		//学生是否已经选中课题
		else if(flag.equals("selectedAlready"))
		{
			String username=request.getParameter("username");
			WlrBiBasedService service=new WlrBiBasedService();
			int count=service.selectedAlready(username);
			String jsonResult=JSON.toJSONString(count);
			response.getWriter().write(jsonResult);
		}
		//教师确认学生选中课题
		else if(flag.equals("certenStuBiBased"))
		{
			String bibno=request.getParameter("data");
			String username=request.getParameter("selecter");
			WlrBiBasedService service=new WlrBiBasedService();
			int count=service.certenStuBiBased(username,bibno);
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
