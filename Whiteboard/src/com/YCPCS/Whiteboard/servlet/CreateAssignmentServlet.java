package com.YCPCS.Whiteboard.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.YCPCS.Whiteboard.Database.DatabaseProvider;
import com.YCPCS.Whiteboard.Model.Assignment;
import com.YCPCS.Whiteboard.Model.Relationship;
import com.YCPCS.Whiteboard.Model.User;

public class CreateAssignmentServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doRequest(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doRequest(req, resp);
	}
	
	private void doRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute("user");
		if (user == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		} else {
			req.setAttribute("username", user.getFirstname());
			
			String name = (String) req.getParameter("assignmentName");
			String desc = (String) req.getParameter("description");
			
			if(name != null && desc != null){
				Assignment as = new Assignment();
				as.setDescription(desc);
				as.setName(name);
				as.setTeacherName(user.getFirstname() + " "+ user.getLastname());
				
				DatabaseProvider.getInstance().addAssignment(as);
				int assId = DatabaseProvider.getInstance().getAllAssignments().size();
				Relationship rel = new Relationship();
				rel.setRoot("user");
				rel.setTarget("assignment");
				rel.setRootId(user.getId());
				rel.setTargetId(assId);
				DatabaseProvider.getInstance().addRelationship(rel);
				
				resp.sendRedirect(req.getContextPath() + "/Assignments");
			}
		}
		
		req.getRequestDispatcher("/_view/AssignmentEditor.jsp").forward(req, resp);
		return;
	}
}
