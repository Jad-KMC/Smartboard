package com.YCPCS.Whiteboard.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.YCPCS.Whiteboard.Model.Assignment;
import com.YCPCS.Whiteboard.Model.Lecture;
import com.YCPCS.Whiteboard.Model.Permission;
import com.YCPCS.Whiteboard.Model.Relationship;
import com.YCPCS.Whiteboard.Model.User;

import dbUtils.DBUtil;
import dbUtils.PersistenceException;


public class DerbyDatabase implements DatabaseLayer{
	static {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception e) {
			throw new IllegalStateException("Could not load Derby JDBC driver");
		}
	}
	
	private interface Transaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException;
	}
	
	private static final int MAX_ATTEMPTS = 10;
	
	@Override
	public int getUserIDByLogin(String username, String password) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement("select id from users " +
							"where users.username = ? AND users.password = ?"
					);
					stmt.setString(1, username);		
					stmt.setString(2, password);
					
					int result = 0;
					
					resultSet = stmt.executeQuery();
					
					while (resultSet.next()){
						return resultSet.getInt(1);
					}
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
		//return 0;
	}

	public void updateGrades(int id, Assignment assign){
		Connection conn = null; 
		try{
			conn = connect();
			PreparedStatement statement = conn.prepareStatement("INSERT INTO assignments (grade)" + " VALUES (?)");
			
			statement.setFloat(1, assign.getAssignmentGrade());
			
//			assignment.setId(resultSet.getInt(index++));
//			assignment.setPointValue(resultSet.getInt(index++));
//			assignment.setName(resultSet.getString(index++));
//			assignment.setDescription(resultSet.getString(index++));
//			assignment.setAssignmentGrade(resultSet.getLong(index++));
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeQuietly(conn);
		}
	}
	
	@Override
	public String getFirstNameFromId(int id) {
		return executeTransaction(new Transaction<String>() {
			@Override
			public String execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
//				"select id from users " +
//				"where users.username = ? AND users.password = ?"
				try {
					stmt = conn.prepareStatement("select firstname from users " +
							"where id = ?"
					);
					stmt.setInt(1, id);
					
					String result = "";
					
					resultSet = stmt.executeQuery();
					
					while (resultSet.next()){
						return resultSet.getString(1);
					}
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
		
	}

	@Override
	public String getLastNameFromId(int id) {
		
		return executeTransaction(new Transaction<String>() {
			@Override
			public String execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement("select lastname " +
							"from users " +
							"where id = ? "
					);
					stmt.setInt(1, id);		
					
					String lastname = "";
					
					resultSet = stmt.executeQuery();
					
					while (resultSet.next()){
						return resultSet.getString(1);
						
					}
					return lastname;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public User getUserByUsernameAndPassword(String username, String password) {
		
		return executeTransaction(new Transaction<User>() {
			@Override
			public User execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement("select users.* " +
							"from users where username = ? and password = ?"
					);
					stmt.setString(1, username);		
					stmt.setString(2, password);
					
					resultSet = stmt.executeQuery();
					
					if (resultSet.next()){
						// User was found
						User user = new User();
						loadUser(user, resultSet, 1);
						return user;
					} else {
						// No such username/password combination
						return null;
					}
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
		
	}

	@Override
	public void addUserToDatabase(User user) {
		Connection conn = null;
		try{
			conn = connect();
			PreparedStatement statement = conn.prepareStatement("INSERT INTO users (username, password, firstname, lastname, email)" + "VALUES (?, ?, ?, ?, ?)");
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getFirstname());
			statement.setString(4, user.getLastname());
			statement.setString(5, user.getEmail());
			statement.execute();
			conn.commit();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeQuietly(conn);
		}
	}
	@Override
	public void addPermission(Permission permission) {
		Connection conn = null;
		try{
			conn = connect();
			PreparedStatement statement = conn.prepareStatement("INSERT INTO permissions (name, userId, fruitcake)" + "VALUES (?, ?, ?)");
			//statement.setInt(1, permission.getId());
			statement.setString(1, permission.getName());
			statement.setInt(2, permission.getUserId());
			statement.setBoolean(3, permission.isFruitcake());//Name teacher desc size
			statement.execute();
			conn.commit();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeQuietly(conn);
		}
		
	}
	
	@Override
	public void addRelationship(Relationship r) {
		Connection conn = null;
		try{
			conn = connect();
			PreparedStatement statement = conn.prepareStatement("INSERT INTO relationships (root, target, root_id, target_id)" + " VALUES (?, ?, ?, ?)");
			statement.setString(1, r.getRoot());
			statement.setString(2, r.getTarget());
			statement.setInt(3, r.getRootId());
			statement.setInt(4, r.getTargetId());
			statement.execute();
			conn.commit();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public Lecture getClassById(int id) {
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = connect();
			String sql = "select lectures.* from lectures where lectures.id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			rs = statement.executeQuery();
			
			while(rs.next()){
				Lecture a = new Lecture();
				loadLecture(a, rs, 1);
				return a;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeQuietly(conn);
			DBUtil.closeQuietly(rs);
			
		}
		
		return null;
	}
	
	public User getUserById(int id){
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = connect();
			String sql = "select users.* from users where users.id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			rs = statement.executeQuery();
			
			while(rs.next()){
				User a = new User();
				loadUser(a, rs, 1);
				return a;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeQuietly(conn);
			DBUtil.closeQuietly(rs);
			
		}
		
		return null;
	}
	
	public List<User> getAllUsers(){
		Connection conn = null;
		ResultSet rs = null;
		List<User> temp = new ArrayList<User>();
		try {
			conn = connect();
			String sql = "select users.* from users";
			PreparedStatement statement = conn.prepareStatement(sql);
			rs = statement.executeQuery();
			
			while(rs.next()){
				User a = new User();
				loadUser(a, rs, 1);
				temp.add(a);
			}
			
			return temp;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeQuietly(conn);
			DBUtil.closeQuietly(rs);
			
		}
		
		return null;
	}
	
	
	public List<Relationship> getRelationshipsByRootAndTarget(String root, String target) {	
		List<Relationship> list = new ArrayList<Relationship>();
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = connect();
			String sql = "select relationships.* from relationships where relationships.root = ? AND relationships.target = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, root);
			statement.setString(2, target);
			rs = statement.executeQuery();
			
			while(rs.next()){
				Relationship r = new Relationship();
				loadRelationship(r, rs, 1);
				list.add(r);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeQuietly(conn);
			DBUtil.closeQuietly(rs);
			
		}
		return list;
	}
	
	public Assignment getAssignmentById(int id) {
		return executeTransaction(new Transaction<Assignment>() {
			public Assignment execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement("select assignments.* " +
							"from assignments where id = ?"
					);
					stmt.setInt(1, id);		
					
					resultSet = stmt.executeQuery();
					
					if (resultSet.next()){
						Assignment assign = new Assignment();
						loadAssignment(assign, resultSet, 1);
						return assign;
					} else {
						// No grade from this id
						return null;
					}
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	@Override
	public List<Permission> getPermissionsFromPermissionId(int id){
		List<Permission> list = new ArrayList<Permission>();
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = connect();
			String sql = "select permissions.* from permissions where permissions.id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			
			rs = statement.executeQuery();
			
			while(rs.next()){
				Permission p = new Permission();
				loadPermissions(p, rs, 1);
				list.add(p);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeQuietly(conn);
			DBUtil.closeQuietly(rs);
			
		}
		return list;
	}
	@Override
	public List<Permission> getUserPermissionsfromUserId(int id) {
		List<Permission> list = new ArrayList<Permission>();
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = connect();
			String sql = "select permissions.* from permissions where permissions.userId = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			
			rs = statement.executeQuery();
			
			while(rs.next()){
				Permission p = new Permission();
				loadPermissions(p, rs, 1);
				list.add(p);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeQuietly(conn);
			DBUtil.closeQuietly(rs);
			
		}
		return list;
	}
	
	

	public<ResultType> ResultType executeTransaction(Transaction<ResultType> txn) {
		try {
			return doExecuteTransaction(txn);
		} catch (SQLException e) {
			throw new PersistenceException("Transaction failed", e);
		}
	}
	
	public<ResultType> ResultType doExecuteTransaction(Transaction<ResultType> txn) throws SQLException {
		Connection conn = connect();
		
		try {
			int numAttempts = 0;
			boolean success = false;
			ResultType result = null;
			
			while (!success && numAttempts < MAX_ATTEMPTS) {
				try {
					result = txn.execute(conn);
					conn.commit();
					success = true;
				} catch (SQLException e) {
					if (e.getSQLState() != null && e.getSQLState().equals("41000")) {
						// Deadlock: retry (unless max retry count has been reached)
						numAttempts++;
					} else {
						// Some other kind of SQLException
						throw e;
					}
				}
			}
			
			if (!success) {
				throw new SQLException("Transaction failed (too many retries)");
			}
			
			// Success!
			return result;
		} finally {
			DBUtil.closeQuietly(conn);
		}
	}

	private Connection connect() throws SQLException {
		String homeDir = System.getProperty("user.home");
		Connection conn = DriverManager.getConnection("jdbc:derby:" + homeDir + "/whiteboard.db;create=true");
		
		// Set autocommit to false to allow multiple the execution of
		// multiple queries/statements as part of the same transaction.
		conn.setAutoCommit(false);
		
		return conn;
	}
	
	private void loadUser(User user, ResultSet resultSet, int index) throws SQLException {
		user.setId(resultSet.getInt(index++));
		user.setUsername(resultSet.getString(index++));
		user.setPassword(resultSet.getString(index++));
		user.setLastname(resultSet.getString(index++));
		user.setFirstname(resultSet.getString(index++));
		user.setEmail(resultSet.getString(index++));
	}
	
	private void loadRelationship(Relationship relationship, ResultSet resultSet, int index) throws SQLException {
		relationship.setId(resultSet.getInt(index++));
		relationship.setRoot(resultSet.getString(index++));
		relationship.setTarget(resultSet.getString(index++));
		relationship.setRootId(resultSet.getInt(index++));
		relationship.setTargetId(resultSet.getInt(index++));
	}
	
	private void loadLecture(Lecture lecture, ResultSet resultSet, int index) throws SQLException {
		lecture.setClassId(resultSet.getInt(index++));
		lecture.setClassName(resultSet.getString(index++));
		lecture.setTeacherId(resultSet.getInt(index++));
		lecture.setClassDescription(resultSet.getString(index++));
		lecture.setClassSize(resultSet.getInt(index++));
	}
	
	private void loadAssignment(Assignment assignment, ResultSet resultSet, int index) throws SQLException {
		assignment.setId(resultSet.getInt(index++));
		assignment.setPointValue(resultSet.getInt(index++));
		assignment.setName(resultSet.getString(index++));
		assignment.setDescription(resultSet.getString(index++));
		assignment.setAssignmentGrade(resultSet.getLong(index++));
	}
	
	private void loadPermissions(Permission p, ResultSet rs, int i) throws SQLException {
		p.setId(rs.getInt(i++));
		p.setName(rs.getString(i++));
		p.setUserId(rs.getInt(i++));
		p.setFruitcake(rs.getBoolean(i++));
	}
	
	public void createTables() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement userStmt = null;
				PreparedStatement lectureStmt = null;
				PreparedStatement relationshipStmt = null;
				PreparedStatement assignmentStmt = null;
				PreparedStatement permissionStmt = null;
				
				try {
					userStmt = conn.prepareStatement(
							"create table users (" +
							"    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY," +
							"    username varchar(20)," +
							"    password varchar(30)," +
							"    lastname varchar(40)," +
							"    firstname varchar(40)," +
							"    email varchar(40)" +
							")");
					userStmt.executeUpdate();
					
					permissionStmt = conn.prepareStatement(
							"create table permissions (" +
							"    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY," +
							"    name varchar(30)," +
							"    userId integer," +
							"    fruitcake smallint " +
							")");
					permissionStmt.executeUpdate();
					
					lectureStmt = conn.prepareStatement(
							"create table lectures (" +
							"    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY," +
							"    name varchar(40)," +
							"    teacher_id integer," +
							"    description varchar(500)," +
							"    size integer" +
							")");
					lectureStmt.executeUpdate();
					
					relationshipStmt = conn.prepareStatement(
							"create table relationships (" +
							"    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY," +
							"    root varchar(30)," +
							"    target varchar(30)," +
							"    root_id integer," +
							"    target_id integer"+
							")");
					relationshipStmt.executeUpdate();
					
					assignmentStmt = conn.prepareStatement(
							"create table assignments (" +
							"    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY," +
							"    point_value integer," +
							"    name varchar(50)," +
							"    description varchar(500)," +
							"    grade integer" +
							")");
					assignmentStmt.executeUpdate();
					return true;
				} finally {
					DBUtil.closeQuietly(userStmt);
					DBUtil.closeQuietly(lectureStmt);
					DBUtil.closeQuietly(relationshipStmt);
					DBUtil.closeQuietly(assignmentStmt);
					DBUtil.closeQuietly(permissionStmt);
				}
			}
		});
	}
	
	public void loadInitialData() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				List<Lecture> lectureList;
				List<Relationship> relationshipList;
				List<User> userList;
				List<Assignment> assignmentList;
				List<Permission> permissionList;
				
				try {
					lectureList = InitialData.getClasses();
					relationshipList = InitialData.getRelationships();
					userList = InitialData.getUsers();
					assignmentList = InitialData.getAssignments();
					permissionList = InitialData.getPermission();
				} catch (IOException e) {
					throw new SQLException("Couldn't read initial data", e);
				}

				PreparedStatement insertLecture = null;
				PreparedStatement insertRelationship = null;
				PreparedStatement insertUser = null;
				PreparedStatement insertAssignment = null;
				PreparedStatement insertPermission = null;

				try {
					insertLecture = conn.prepareStatement("insert into lectures (name, teacher_id, description, size) values (?, ?, ?, ?)");
					for (Lecture lecture : lectureList) {
						insertLecture.setString(1, lecture.getClassName());
						insertLecture.setInt(2, lecture.getTeacherId());
						insertLecture.setString(3, lecture.getClassDescription());
						insertLecture.setInt(4, lecture.getClassSize());
						insertLecture.addBatch();

					}
					insertLecture.executeBatch();
					
					insertRelationship = conn.prepareStatement("insert into relationships (root, target, root_id, target_id) values (?, ?, ?, ?)");
					for (Relationship relationship : relationshipList) {
						insertRelationship.setString(1, relationship.getRoot());
						insertRelationship.setString(2, relationship.getTarget());
						insertRelationship.setInt(3, relationship.getRootId());
						insertRelationship.setInt(4, relationship.getTargetId());
						insertRelationship.addBatch();
					}
					insertRelationship.executeBatch();
					
					insertUser = conn.prepareStatement("insert into users (username, password, lastname, firstname, email) values (?, ?, ?, ?, ?)");
					for (User user : userList) {
						insertUser.setString(1, user.getUsername());
						insertUser.setString(2, user.getPassword());
						insertUser.setString(3, user.getLastname());
						insertUser.setString(4, user.getFirstname());
						insertUser.setString(5, user.getEmail());
						insertUser.addBatch();

					}
					insertUser.executeBatch();
					
					insertAssignment = conn.prepareStatement("insert into assignments (point_value, name, description, grade) values (?, ?, ?, ?)");
					for (Assignment assignment : assignmentList) {
						insertAssignment.setInt(1, assignment.getPointValue());
						insertAssignment.setString(2, assignment.getName());
						insertAssignment.setString(3, assignment.getDescription());
						insertAssignment.setInt(4,(int) assignment.getAssignmentGrade());
						insertAssignment.addBatch();

					}
					insertAssignment.executeBatch();
					
					insertPermission = conn.prepareStatement("insert into permissions (name, userId, fruitcake)values (?, ?, ?)");
					for (Permission permission : permissionList) {
						insertPermission.setString(1, permission.getName());
						insertPermission.setInt(2, permission.getUserId());
						insertPermission.setBoolean(3, permission.isFruitcake());
						insertPermission.addBatch();

					}
					insertPermission.executeBatch();
					
					return true;
				} finally {
					DBUtil.closeQuietly(insertRelationship);
					DBUtil.closeQuietly(insertLecture);
					DBUtil.closeQuietly(insertUser);
					DBUtil.closeQuietly(insertAssignment);
					DBUtil.closeQuietly(insertPermission);
				}
			}
		});
	}
	
	public static void main(String[] args) throws IOException {
		
		DerbyDatabase db = new DerbyDatabase();
		System.out.println("Creating tables...");
		db.createTables();
		System.out.println("Loading initial data...");
		db.loadInitialData();
		System.out.println("Please run again for tests!");
		for(Assignment a : db.getAllAssignments()){
			System.out.println("Assignment: "+a.getName());
		}
		for(Lecture a : db.getAllLectures()){
			System.out.println("Lecture: "+a.getClassName());
		}
	}

	public void addClass(Lecture lecture) {
		Connection conn = null;
		try{
			conn = connect();
			PreparedStatement statement = conn.prepareStatement("INSERT INTO lectures (name, teacher_id, description, size)" + "VALUES (?, ?, ?, ?)");
			statement.setString(1, lecture.getClassName());
			statement.setInt(2, lecture.getTeacherId());
			statement.setString(3, lecture.getClassDescription());
			statement.setInt(4, lecture.getClassSize());//Name teacher desc size
			statement.execute();
			conn.commit();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeQuietly(conn);
		}
	}

	public Lecture getLectureById(int id) {
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = connect();
			String sql = "select lectures.* from lectures where lectures.id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			rs = statement.executeQuery();
			
			while(rs.next()){
				Lecture a = new Lecture();
				loadLecture(a, rs, 1);
				return a;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeQuietly(conn);
			DBUtil.closeQuietly(rs);
			
		}
		return null;
	}

	@Override
	public List<Relationship> getTarget(String root, String target, int rootId) {
		Connection conn = null;
		ResultSet rs = null;
		List<Relationship> temp = new ArrayList<Relationship>();
		try {
			conn = connect();
			String sql = "select relationships.* from relationships where relationships.root = ? AND relationships.target = ? AND relationships.root_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, root);
			statement.setString(2, target);
			statement.setInt(3, rootId);
			rs = statement.executeQuery();
			
			while(rs.next()){
				Relationship rel = new Relationship();
				temp.add(rel);
				loadRelationship(rel, rs, 1);
			}
			return temp;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeQuietly(conn);
			DBUtil.closeQuietly(rs);
			
		}
		
		return null;
	}
	
	public List<Assignment> getAllAssignments(){
		Connection conn = null;
		ResultSet rs = null;
		List<Assignment> temp = new ArrayList<Assignment>();
		try {
			conn = connect();
			String sql = "select assignments.* from assignments";
			PreparedStatement statement = conn.prepareStatement(sql);
			rs = statement.executeQuery();
			
			while(rs.next()){
				Assignment ass = new Assignment();
				temp.add(ass);
				loadAssignment(ass, rs, 1);
			}
			return temp;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeQuietly(conn);
			DBUtil.closeQuietly(rs);
			
		}
		return null;
	}

	public void addAssignment(Assignment as) {
		Connection conn = null;
		try{
			conn = connect();
			PreparedStatement statement = conn.prepareStatement("INSERT INTO assignments (name, description, grade, point_value)" + "VALUES (?, ?, ?, ?)");
			statement.setString(1, as.getName());
			statement.setString(2, as.getDescription());
			statement.setInt(3, (int)as.getAssignmentGrade());
			statement.setInt(4, as.getPointValue());//Name teacher desc size
			statement.execute();
			conn.commit();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeQuietly(conn);
		}
	}
	
	public List<Lecture> getAllLectures(){
		Connection conn = null;
		ResultSet rs = null;
		List<Lecture> temp = new ArrayList<Lecture>();
		try {
			conn = connect();
			String sql = "select lectures.* from lectures";
			PreparedStatement statement = conn.prepareStatement(sql);
			rs = statement.executeQuery();
			
			while(rs.next()){
				Lecture lec = new Lecture();
				temp.add(lec);
				loadLecture(lec, rs, 1);
			}
			return temp;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.closeQuietly(conn);
			DBUtil.closeQuietly(rs);
			
		}
		return null;
	}
}
