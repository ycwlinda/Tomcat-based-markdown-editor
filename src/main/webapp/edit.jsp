<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head class="w3-container">
    <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
    <title>Edit Post</title>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>
<body>
	<div><h1 class="w3-text-blue">Edit Post</h1></div>
	<form action="post" method="POST" >
	    <div>
        	<input type="hidden" name="username" value='<%= request.getParameter("username") %>' />
        	<input type="hidden" name="postid" value='<%= request.getParameter("postid") %>' />
    	</div>
        <div class="w3-container">
            <button class="w3-btn w3-blue" type="submit" name="action" value="save">Save</button>
            <button class="w3-btn w3-blue" type="submit" name="action" value="close">Close</button>
            <button class="w3-btn w3-blue" type="submit" name="action" value="preview">Preview</button>
            <button class="w3-btn w3-blue" type="submit" name="action" value="delete">Delete</button>
        </div>
        <br>
        <div class="w3-container">
            <label class="w3-text-blue" for="title">Title</label>
            <input type="text" name="title" value='<%= request.getAttribute("title") %>'>
        </div>
        <br>
        <div class="w3-container">
            <label class="w3-text-blue" for="body">Body</label>
            <textarea style="height: 20rem;" id="body" name="body"><%= request.getAttribute("body")%></textarea>
        </div>
    </form>
</body>
</html>
