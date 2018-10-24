<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head class="w3-container">
	<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
	<title>List Post</title>
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>
<body>
	<h1 class="w3-text-blue">List Post</h1>
	<br>

	<form class="w3-container" action="post" method="POST">
		<input type="hidden" name="username" value=<%= request.getParameter("username") %>>
		<input type="hidden" name="postid" value="0" %>


		<button class="w3-btn w3-blue" type="submit" name="action" value="open">New Post</button>	
	</form>

	<table class="w3-table w3-bordered w3-striped">
		<thead>
			<tr class="w3-teal">
				<th scope="col">Title</th>
				<th scope="col">Created</th> 
				<th scope="col">Modified</th>
				<th></th>
			</tr>
		</thead>

		<tbody>
			<% List<List<String>> posts = (List<List<String>>)request.getAttribute("posts"); %>
				<% for (int i = 0; posts != null && i < posts.size(); i++) { %>
				<% 	List<String> row = posts.get(i); %>
				<tr>
					<td> <%= row.get(1) %> </td>
					<td> <%= row.get(2) %> </td>
					<td> <%= row.get(3) %> </td>
					<td>
						<form action="post" method="POST">
							<input type="hidden" name="username" value=<%= request.getParameter("username") %>>
							<input type="hidden" name="postid" value=<%= row.get(0) %>>
							<div >
								<button class="w3-btn w3-blue" type="submit" name="action" value="open">Open</button>
								<button class="w3-btn w3-blue" type="submit" name="action" value="delete">Delete</button>
							</div>
						</form>
					</td>
				</tr>
			<% } %>
		</tbody>
	</table>
</body>
</html>
