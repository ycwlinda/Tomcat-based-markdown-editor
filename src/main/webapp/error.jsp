<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head class="w3-container">
    <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
    <title>Error Page</title>
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">   
</head>
<body>
     <div><h1 class="w3-text-blue">Error Page</h1></div>
    <br>
    <div class="w3-container">
		<h3>Error Message</h3>
		<%= request.getAttribute("msg") %>
    </div>
    <div class="w3-container">
		<h3>Response Status</h3>
		<%= response.getStatus() %>
    </div>
    
    <br>
    <div class="w3-container">
		<h3>Request Info</h3>
		<b>action:</b>
		<%= request.getParameter("action") %>
		<br>
		<b>username:</b>
		<%= request.getParameter("username") %>
		<br>
		<b>postid:</b>
		<%= request.getParameter("postid") %>
		<br>
		<b>title:</b>
		<%= request.getParameter("title") %>
		<br>
		<b>body:</b>
		<%= request.getParameter("body") %>
    </div>
</body>
</html>
