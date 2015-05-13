package com.YCPCS.Whiteboard.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.YCPCS.Whiteboard.Controller.AssignmentController;
import com.YCPCS.Whiteboard.Controller.LectureController;
import com.YCPCS.Whiteboard.Database.DatabaseProvider;
import com.YCPCS.Whiteboard.Model.Assignment;
import com.YCPCS.Whiteboard.Model.Lecture;
import com.YCPCS.Whiteboard.Model.User;

public class AssignmentsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doRequest(req, resp);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doRequest(req, resp);
	}

	protected void doRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String doLogout = (String) req.getParameter("logout");
		String doHelp = (String) req.getParameter("help");
		String doAccount = (String) req.getParameter("account");

		User user = (User) req.getSession().getAttribute("user");
		if (user == null) {
			// User is not logged in, redirect to login page
			resp.sendRedirect(req.getContextPath() + "/login");
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
			req.setAttribute("username", null);
			resp.sendRedirect(req.getContextPath() + "/login");
		}
		else {
			req.setAttribute("username", user.getFirstname());
		}
		
		AssignmentController cont = new AssignmentController();
		ArrayList<Assignment> assignments = cont.getAllUserAssignments(user.getId());
		System.out.println(assignments.size());
		String classCode = "";
		if(assignments.size() == 0){
			classCode = "<h2 style=\"color:red;\">You are not enrolled in any classes</h2>";
		}else{
			for(Assignment assignment : assignments){
				System.out.println(assignment);
				String temp = 
					"<div id=\"Assignment\">"+
							"<h2>"+assignment.getName()+"</h2>"+
							"<h3>Grade: "+assignment.getAssignmentGrade()+"</h3>"+
						"<div id=\"ClassOptions\">"+
						"<p>"+assignment.getDescription()+"</p>"+
						"</div>"+
					"</div>";
				classCode += temp;
			}
		}
		
		req.setAttribute("classHTML", classCode);
		
		req.getRequestDispatcher("/_view/Assignments.jsp").forward(req, resp);
	}
		
}
