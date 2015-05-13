package com.YCPCS.Whiteboard.Database;

import java.util.List;

import com.YCPCS.Whiteboard.Model.Permission;
import com.YCPCS.Whiteboard.Model.Lecture;
import com.YCPCS.Whiteboard.Model.Relationship;
import com.YCPCS.Whiteboard.Model.User;
import com.YCPCS.Whiteboard.Model.Assignment;

public interface DatabaseLayer {
	
	//Should get the user ID given the username and password info
	//, If there is no such user return -1
	//Password is preferably stored in a hash
	public int getUserIDByLogin(String Username, String password);
	
	public String getFirstNameFromId(int id);
	
	public String getLastNameFromId(int id);
	
	public User getUserByUsernameAndPassword(String username, String password);
	
	public void addUserToDatabase(User user);

	public Lecture getClassById(int id);
	
	public void addRelationship(Relationship r);
	
	public List<Relationship> getRelationshipsByRootAndTarget(String root, String target);
	
	public void addClass(Lecture lecture);

	public Assignment getAssignmentById(int id);
	
	public List<Permission> getUserPermissionsfromUserId(int id);
	
	public List<Permission> getPermissionsFromPermissionId(int id);
	
	public void addPermission(Permission permission);
	public User getUserById(int id);
	
	public Lecture getLectureById(int id);
	
	public List<Relationship> getTarget(String root, String target, int rootId);
	
	public void addAssignment(Assignment as);
	
	public List<User> getAllUsers();
	
	public List<Lecture> getAllLectures();

	public List<Assignment> getAllAssignments();
}
