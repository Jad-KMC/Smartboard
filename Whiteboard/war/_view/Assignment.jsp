<html>
	<head>
	<link rel="stylesheet" type="text/css" href="_view/Assignment.css">
	<div id="Title">
		<title>SmartBoard</title>
	</div>
	</head>
<body>
	<h1>Smartboard: Classes</h1>
	<div id="Options">
		<p>Account | Help | Logout</p>
	</div>
	<div id="OptionsBar">
		<ul><form action="${pageContext.request.contextPath}/hub"><button type="submit">Home</button></form></ul>
		<ul><form action="${pageContext.request.contextPath}/lecture"><button type="submit">Classes</button></form></ul>
		<ul><form action="${pageContext.request.contextPath}/schedule"><button type="submit">Schedule</button></form></ul>
		<ul><form action="${pageContext.request.contextPath}/calendar"><button type="submit">Calendar</button></form></ul>
		<ul><form action="${pageContext.request.contextPath}/grades"><button type="submit">Grades</button></form></ul>
		<ul><form action="${pageContext.request.contextPath}/assignments"><button type="submit">Assignments</button></form></ul>
		<ul><form action="${pageContext.request.contextPath}/forums"><button type="submit">Forums</button></form></ul>
	</div>
	${classHTML}
	<form class="lecture" action="${pageContext.request.contextPath}/ClassCreate" method="post">
	<ul><button type="submit" style="margin-left: -40px;">Submit New Class</button></ul>
	</form>
</body>			
</html>