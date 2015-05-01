<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
	<link rel="stylesheet" type="text/css" href="_view/Assignments.css">
	<div id="Title">
		<title>SmartBoard</title>
	</div>
</head>
<body>
	<h1>SmartBoard: Assignments</h1>
	<div id="Options">
	<h3><form action="${pageContext.request.contextPath}/hub" method="post"><button type="submit">Home</button> | <button type="submit" name="account" value="true">My Account</button> | <button type="submit" name="help" value="true">Help</button> | <button type="submit" name="logout" value="true">Log Out</button></form></h3>
	</div>
	<div id="OptionsBar">
		<ul><form action="${pageContext.request.contextPath}/lecture"><button type="submit">Classes</button></form></ul>
		<ul><form action="${pageContext.request.contextPath}/Schedule"><button type="submit">Schedule</button></form></ul>
		<ul><form action="${pageContext.request.contextPath}/Calendar"><button type="submit">Calendar</button></form></ul>
		<ul><form action="${pageContext.request.contextPath}/Grades"><button type="submit">Grades</button></form></ul>
		<ul><form action="${pageContext.request.contextPath}/Assignments"><button type="submit">Assignments</button></form></ul>
		<ul><form action="${pageContext.request.contextPath}/Forums"><button type="submit">Forums</button></form></ul>
	</div>
	<div id="AssignmentCreator">
		<ul><button type="submit">Create Assignment</button></ul>
		<form action="${pageContext.request.contextPath}/Assignment" method="get">
		<br>
		<p>Title:</p>
		<input type="text" name="title">
		
		<p>Question:</p>
		<input type="text" name="question">
		
		<p>Due Date: </p>
		<input type="date" name="dueDate">
		</form>
	</div>
	<div id="Assignments">
		
	</div>	
</body>
</html>