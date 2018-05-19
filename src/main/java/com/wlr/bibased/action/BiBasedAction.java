package com.wlr.bibased.action;

import java.io.IOException;
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
import com.wlr.bibased.model.BiBased;
import com.wlr.bibased.model.News;

/**
 * Servlet implementation class BiBasedAction
 */
@WebServlet("/BiBasedAction")
public class BiBasedAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BiBasedAction() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String flag=request.getParameter("flag");
		if(flag.equals("addBibased"))
		{
			WlrBiBasedService service=new WlrBiBasedService();
			String json2Obj=request.getParameter("submitData");
			//将json数据转换为BiBased
			BiBased bibased=JSON.parseObject(json2Obj,BiBased.class);
			bibased.setAdviser(request.getParameter("username"));
			service.addBiased(bibased);
		}
		else if(flag.equals("editBiBased"))
		{
			String json2obj=request.getParameter("data");
			BiBased biBased=JSON.parseObject(json2obj, BiBased.class);
			WlrBiBasedService service =new WlrBiBasedService();
			service.updateBiBased(biBased);
		}
		else if(flag.equals("selectInfoBiBased"))
		{
			String bibno=request.getParameter("bibno");
			WlrBiBasedService service=new WlrBiBasedService();
			BiBased biBased =service.getBiBased(bibno);
			String str=JSON.toJSONString(biBased);
			response.getWriter().write(str);
		}
		else if(flag.equals("selectListBiBased"))
		{
			WlrBiBasedService service=new WlrBiBasedService();
			 List<BiBased> list=null;
			 int pageSize=Integer.parseInt(request.getParameter("pageSize"));
		     int pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
		     int total= service.getTotal("bibased");
		     String adviser=request.getParameter("adviser");
		     String student=request.getParameter("student");
		  
		     //根据用户名获取bibased的记录的集合
		     list=service.getAllBiBasedByWhere(pageSize,pageIndex,adviser,student);
		     HashMap<String, Object> map=new HashMap<>();
		     map.put("total", total);
		     map.put("data", list);
		     String jsonResult=JSON.toJSONString(map);
		     response.getWriter().write(jsonResult);
		     
		}
		else if (flag.equals("delBiBased"))
		{
			String delScope	= request.getParameter("data");
			WlrBiBasedService service=new WlrBiBasedService();
			service.delRecord("bibased", "bibno", delScope);
		}
		else if(flag.equals("selectStuBiBased"))
		{
			WlrBiBasedService service=new WlrBiBasedService();
			String student=request.getParameter("student");
			BiBased biBased=service.getMyBiBased(student);
			String jsonResult=JSON.toJSONString(biBased);
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
