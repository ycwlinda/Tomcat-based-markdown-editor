<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head class="w3-container">
    <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
    <title>Preview Post</title>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>
<body>
    <form action="post" method="POST">
        <input type="hidden" name="username" value='<%= request.getParameter("username") %>'/>
        <input type="hidden" name="postid" value='<%= request.getParameter("postid") %>'/>
        <input type="hidden" name="title" value='<%= request.getAttribute("title") %>'>
        <input type="hidden" name="body" value='<%= request.getAttribute("body") %>'>
    
    <h1 class="w3-text-blue">Preview Post</h1>
    <br>
    <div class="w3-container">
        <button class="w3-btn w3-blue" type="submit" name="action" value="open">Close Preview</button>
    </div>
    </form>
    <br>
    <div class="w3-container">
    	<h2><%= request.getAttribute("titleHtml") %></h2>
    	<%= request.getAttribute("bodyHtml") %>
    </div>
</body>
</html>