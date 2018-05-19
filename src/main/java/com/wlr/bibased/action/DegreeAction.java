package com.wlr.bibased.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.wlr.bibased.bizlogic.WlrBiBasedService;
import com.wlr.bibased.model.Degree;

/**
 * Servlet implementation class DegreeAction
 */
@WebServlet("/DegreeAction")
public class DegreeAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DegreeAction() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String flag=request.getParameter("flag");
		if(flag.equals("selectStudentDegree"))
		{
			WlrBiBasedService service=new WlrBiBasedService();
			 List<Degree> list=null;		     
	         list=service.getStudentDegree();
		     String jsonResult=JSON.toJSONString(list);
		     response.getWriter().write(jsonResult);
		}
		else if(flag.equals("selectTeacherDegree"))
		{
			WlrBiBasedService service=new WlrBiBasedService();
			 List<Degree> list=null;		     
	        list=service.getTeacherDegree();
		     String jsonResult=JSON.toJSONString(list);
		     response.getWriter().write(jsonResult);
		}
		else if(flag.equals("updateStuDegree"))
		{
			WlrBiBasedService service=new WlrBiBasedService();
			String json2Obj=request.getParameter("data");
			List<Degree> list=JSON.parseArray(json2Obj, Degree.class);
			service.saveStudentDegree(list);
		}
		else if(flag.equals("updateTeaDegree"))
		{
			WlrBiBasedService service=new WlrBiBasedService();
			String json2Obj=request.getParameter("data");
			List<Degree> list=JSON.parseArray(json2Obj, Degree.class);
			service.saveTeacherDegree(list);
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
