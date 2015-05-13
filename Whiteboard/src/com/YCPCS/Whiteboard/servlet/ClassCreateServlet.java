package com.YCPCS.Whiteboard.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.YCPCS.Whiteboard.Database.DatabaseProvider;
import com.YCPCS.Whiteboard.Model.Assignment;
import com.YCPCS.Whiteboard.Model.Lecture;
import com.YCPCS.Whiteboard.Model.User;

public class ClassCreateServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute("user");
		
		if (user == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}else{
			req.setAttribute("username", user.getFirstname());
		}
		req.getRequestDispatcher("/_view/createClass.jsp").forward(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doRequest(req, resp);
	}
	
	private void doRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("Adding Class");
		Lecture lec = new Lecture();
		lec.setClassName((String) req.getParameter("className"));
		lec.setTeacher(req.getParameter("teacher"));
		lec.setClassDescription(req.getParameter("description"));
		lec.setClassSize(Integer.parseInt(req.getParameter("size")));
		DatabaseProvider.getInstance().addClass(lec);
		resp.sendRedirect(req.getContextPath() + "/lecture");
	}
}
