package com.YCPCS.Whiteboard.Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.YCPCS.Whiteboard.Database.InitialData;
import com.YCPCS.Whiteboard.Model.*;

public class FakeDatabase implements DatabaseLayer{
	private List<User> usersList;
	private List<Lecture> classList;
	private List<Assignment> assignmentList;
	private List<Relationship> relationshipList;
	private List<Permission> permissionList;
	private List<Lecture> lectureList;
	
	//Below is covered in the user class
	//private ArrayList<String> usernames;
	//private ArrayList<String> passwords;
	//private ArrayList<String> names = new ArrayList<String>();
	//private ArrayList<Integer> userIds = new ArrayList<Integer>();
	
	
	public FakeDatabase() {
		usersList = new ArrayList<User>();
		classList = new ArrayList<Lecture>();
		assignmentList = new ArrayList<Assignment>();
		relationshipList = new ArrayList<Relationship>();
		permissionList = new ArrayList<Permission>();
		lectureList = new ArrayList<Lecture>();
		// get initial data
		readInitialData();
	}
	
	public void addUserToDatabase(User user){
		usersList.add(user);
	}

	public void readInitialData() {
		try {
			usersList.addAll(InitialData.getUsers());
			classList.addAll(InitialData.getClasses());
			assignmentList.addAll(InitialData.getAssignments());
			relationshipList.addAll(InitialData.getRelationships());
			permissionList.addAll(InitialData.getPermission());
		} catch (IOException e) {
			throw new IllegalStateException("Couldn't read initial data", e);
		}
	}
	
	// user 
	public User getUserByUsernameAndPassword(String username, String password){
		if(username!=null && password!=null){
			for(User user : usersList){
				if(user.getUsername().equals(username) && user.getPassword().equals(password)){
					return user;
				}
			}
		}
		return null;
	}
	
	public int getUserIDByLogin(String Username, String password) {
		for(User user : usersList){
			if(user.getUsername().equals(Username) && user.getPassword().equals(password)){
				return user.getId();
			}
		}
		return -1;
	}

	public String getFirstNameFromId(int id) {
		for(User user : usersList){
			if(user.getId()==id){
				return user.getFirstname();
			}
		}
		return null;
	}

	public String getLastNameFromId(int id) {
		for(User user : usersList){
			if(user.getId()==id){
				return user.getLastname();
			}
		}
		return null;
	}
	
	// class
	public String getClassNameFromId(int id){
		for (Lecture class1 : classList){
			if(class1.getClassId()==id){
				return class1.getClassName();
			}
		}
		return null;
	}
	
	public String getClassDescriptionFromId(int id){
		for (Lecture class1 : classList){
			if(class1.getClassId()==id){
				return class1.getClassDescription();
			}
		}
		return null;
	}
	
	public int getClassSizeFromId(int id){
		for (Lecture class1 : classList){
			if(class1.getClassId()==id){
				return class1.getClassSize();
			}
		}
		return 0;
	}

	public Lecture getClassById(int id) {
		for(Lecture lecture : classList){
			if(lecture.getClassId()==id){
				return lecture;
			}
		}
		return null;
	}
	
	public List<Lecture> getAllLectures(){		
		return classList;
	}
	
	public List<Relationship> getRelationshipsByRootAndTarget(String root, String target) {
		List<Relationship> relList = new ArrayList<Relationship>();
		for(Relationship rel : relationshipList){
			if(rel.getRoot().equals(root) && rel.getTarget().equals(target)){
				relList.add(rel);
			}
		}
		return relList;
	}

	public Grade getGradeById(int id) {
		for(Lecture lecture: classList){
			if(lecture.getClassId()==id)
				return lecture.getGrade();
		}
		return null;
	}

	@Override
	public void addRelationship(Relationship r) {
		relationshipList.add(r);
	}

	@Override
	public void addClass(Lecture lecture) {
		classList.add(lecture);
	}
	public Assignment getAssignmentById(int id) {
		for (Assignment assign: assignmentList){
			if(assign.getId()==id){
				return assign;
			}
		}
		return null;
	}

	@Override
	public List<Permission> getUserPermissionsfromUserId(int id) {
		List<Permission> permissions = new ArrayList<Permission>();
		for (Permission permission : permissionList) {
			if (permission.getId()==id){
				permissions.add(permission);
				return permissions;
			}
		}
		return null;
	}

	@Override
	public List<Permission> getPermissionsFromPermissionId(int id) {
		List<Permission> permissions = new ArrayList<Permission>();
		for (Permission permission : permissionList) {
			if (permission.getId()==id){
				permissions.add(permission);
				return permissions;
			}
		}
		return null;
	}

	@Override
	public void addPermission(Permission permission) {
		permissionList.add(permission);
	}
	public User getUserById(int id) {
		for (User user : usersList) {
			if (user.getId()==id){
				return user;
			}
		}
		return null;
	}

	@Override
	public Lecture getLectureById(int id) {
		for (Lecture lecture : lectureList) {
			if (lecture.getClassId()==id){
				return lecture;
			}
		}
		return null;
	}

	@Override
	public List<Relationship> getTarget(String root, String target, int rootId) {
		
		return null;
	}

	@Override
	public void addAssignment(Assignment as) {
		assignmentList.add(as);
		
	}

	@Override
	public List<User> getAllUsers() {
		return usersList;
	}

	@Override
	public List<Assignment> getAllAssignments() {
		return assignmentList;
	}
}
