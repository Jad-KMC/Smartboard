package com.YCPCS.Whiteboard.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.YCPCS.Whiteboard.Controller.LectureController;
import com.YCPCS.Whiteboard.Database.DatabaseProvider;
import com.YCPCS.Whiteboard.Model.Lecture;
import com.YCPCS.Whiteboard.Model.User;

public class LectureServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doRequest(req, resp);
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doRequest(req, resp);
	}
	
	private void doRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String classCode = "";
		String doLogout = (String) req.getParameter("logout");
		String doHelp = (String) req.getParameter("help");
		String doAccount = (String) req.getParameter("account");
		
		User user = (User) req.getSession().getAttribute("user");
		
		if(user == null){
			resp.encodeRedirectURL("/login");
			return;
		}
		if (doHelp != null && doHelp.equals("true")) {
			//System.out.println("Help is true: " + doHelp);
			resp.sendRedirect(req.getContextPath() + "/Help");
			return;
		} else if (doAccount != null && doAccount.equals("true")) {
			//System.out.println("Account Page is true: " + doAccount);
			resp.sendRedirect(req.getContextPath() + "/UserAccount");
			return;
		} else if (doLogout != null && doLogout.equals("true")) {
			//System.out.println("DOING LOGOUT");
			req.setAttribute("username", null);
			resp.sendRedirect(req.getContextPath() + "/login");
		}
		else{
			
			int userId = user.getId();
			//TODO: Make more secure
			LectureController cont = new LectureController();
			ArrayList<Lecture> lectures = (ArrayList<Lecture>) cont.getAllUserLectures(userId);
			for(Lecture lecture : lectures){
				User teacher = DatabaseProvider.getInstance().getUserById(lecture.getTeacherId());
				String temp = 
					"<div id=\"Class\">"+
						"<h2>"+lecture.getClassName()+"</h2>"+
						"<h3>Professor: "+teacher.getFirstname() + " " + teacher.getLastname()+"</h3>"+
						"<h2>"+lecture.getClassDescription() +"</h2>"+
					"</div>";
				
				classCode += temp;
			}
			if(lectures.size() == 0){
				classCode = "<h2 style=\"color:red;\">You are not enrolled in any classes</h2>";
			}
			
			req.setAttribute("classHTML", classCode);
			req.getRequestDispatcher("/_view/Lecture.jsp").forward(req, resp);
		}
	}
}
