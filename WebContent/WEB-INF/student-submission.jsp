<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="model.Submission"%>
<%@ page import="dao.UserDAO"%>
<%@ page import="dao.AssignmentDAO"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Home</title>

	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>

	<nav class="navbar navbar-light bg-light">
	  <a class="navbar-brand" href="home">Assignment Management</a>
	  <form class="form-inline">
	    <p class="mt-3 mr-2"><%= ((User) session.getAttribute("user")).getName() %></p> | 
	    <p class="mt-3 mr-2 ml-2"><%= ((User) session.getAttribute("user")).getRole() %></p>
	    <a href="logout" class="btn btn-outline-dark my-2 my-sm-0">logout</a>
	  </form>
	</nav>
	
	<div class="contanier">
	
		<%
			if(request.getAttribute("msg") != null){
				%>
				<div class="alert alert-info mt-2 mr-2 ml-2" role="alert">
				  <%=request.getAttribute("msg")%>
				  <button type="button" class="close" data-dismiss="alert" aria-label="Close">
				    <span aria-hidden="true">&times;</span>
				  </button>
				</div>
				<%
			}
		%>
		
		<div class="row">
			<div class="col-sm-12">
			<a href="lhome" class="btn btn-primary mt-2 ml-2">Back</a>
			</div>
		</div>
		
		<%
		ArrayList<Submission> submission = (ArrayList<Submission>) request.getAttribute("submission");
		
		if(submission.size() == 0){
			%><h4 style="text-align: center;">No more Submission.</h4><%
		}
		
		for(int i=0; i<submission.size(); i++){
			%>
			<div class="row">
				<div class="col-sm-12">
					<div class="card m-3">
					  <div class="card-body">
					    <h5 class="card-title">Student Name: <%= UserDAO.getUserById(submission.get(i).getStudent_id()).getName() %></h5>
					    <h6 class="card-subtitle mb-2 text-muted">Email: <%= UserDAO.getUserById(submission.get(i).getStudent_id()).getEmail() %></h6>
					    <p class="card-text">Post Date: <%= AssignmentDAO.getAssignment(submission.get(i).getAssignment_id()).getPost_date() %><br/>
					    Due Date: <%= AssignmentDAO.getAssignment(submission.get(i).getAssignment_id()).getDue_date() %></p>
					    <p class="card-text">Submitted Date: <%= submission.get(i).getSubmit_date() %></p>
					    <% 
					    	if(AssignmentDAO.getAssignment(submission.get(i).getAssignment_id()).getUpload_file() != null){
					    		%>
					    		<a href="downloadFile?filename=assignment/<%= AssignmentDAO.getAssignment(submission.get(i).getAssignment_id()).getUpload_file() %>" class="card-link"><%= AssignmentDAO.getAssignment(submission.get(i).getAssignment_id()).getUpload_file() %></a>
					    		<%
					    	}
					    %>
					    <% 
					    	if(submission.get(i).getUpload_file() != null){
					    		%>
					    		<p class="font-weight-bold mt-3">Submitted File</p>
					    		<a href="downloadFile?filename=submission/<%= submission.get(i).getUpload_file() %>" class="card-link"><%= submission.get(i).getUpload_file() %></a>
					    		<%
					    	}
					    %>
					  </div>
					</div>
				</div>
			</div>			
			<%
		}
		
		%>
	
	</div>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>