<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="model.Submission"%>
<%@ page import="dao.UserDAO"%>
<%@ page import="dao.AssignmentDAO"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Submission</title>

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
	
		<div class="row">
			<div class="col-sm-12">
			<a href="shome" class="btn btn-primary mt-2 ml-2">Back</a>
			</div>
		</div>
	
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
		
		<%
		ArrayList<Submission> submission = (ArrayList<Submission>) request.getAttribute("submission");
		
		if(submission.size() == 0){
			%><h4 style="text-align: center;">No more my Submission.</h4><%
		}
		
		for(int i=0; i<submission.size(); i++){
			%>
			<div class="row">
				<div class="col-sm-12">
					<div class="card m-3">
					  <div class="card-body">
					    <h5 class="card-title"><%= AssignmentDAO.getAssignment(submission.get(i).getAssignment_id()).getTitle() %></h5>
					    <h6 class="card-subtitle mb-2 text-muted"><%= AssignmentDAO.getAssignment(submission.get(i).getAssignment_id()).getSubject() %></h6>
					    <h6 class="card-subtitle mb-2 text-muted">Year: <%= AssignmentDAO.getAssignment(submission.get(i).getAssignment_id()).getYear() %></h6>
					    <h6 class="card-subtitle mb-2 text-muted">Semester: <%= AssignmentDAO.getAssignment(submission.get(i).getAssignment_id()).getSemester() %></h6>
					    <h6 class="card-subtitle mb-2 text-muted"><%= UserDAO.getUserById(AssignmentDAO.getAssignment(submission.get(i).getAssignment_id()).getLecturer_id()).getName() %> - <%= UserDAO.getUserById(AssignmentDAO.getAssignment(submission.get(i).getAssignment_id()).getLecturer_id()).getEmail() %></h6>
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
					    <a href="my-submission?submission-delete=<%=submission.get(i).getId()%>" class="btn btn-danger mt-2 mr-2 float-right">Delete Submission</a>
					    <button type="button" onclick='updateSubmission(<%=new Gson().toJson(submission.get(i))%>)' class="btn btn-success mt-2 mr-2 float-right">Update Submission</button>
					  </div>
					</div>
				</div>
			</div>			
			<%
		}
		
		%>
	
	</div>
	
	<!-- Modal -->
	<div class="modal fade" id="submissionModal" tabindex="-1" role="dialog" aria-labelledby="submissionModalLabel" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	    <form id="updateSubmissionForm" method="post" enctype="multipart/form-data">
	      <div class="modal-header">
	        <h5 class="modal-title" id="submissionModalLabel">Update Submission</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
			  <div class="form-group">
				  <label for="ass_file" class="font-weight-bold">Submission File</label>
			  	  <div class="custom-file">
				    <input type="file" class="custom-file-input" id="upload_file" name="upload_file">
				    <label class="custom-file-label" for="customFile" style="overflow: hidden; text-overflow: ellipsis;">Choose file</label>
				  </div>
			  </div>
	      </div>
	      <div class="modal-footer">
	        <button type="submit" class="btn btn-primary">Submit</button>
	      </div>
	      </form>
	    </div>
	  </div>
	</div>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script>
    $('#updateSubmissionForm #upload_file').on('change',function(){
        var fileName = $(this).val().split('\\');
        $(this).next('.custom-file-label').html(fileName[fileName.length-1]);
    });
    
    function updateSubmission(sub){
    	$('#updateSubmissionForm .custom-file-label').html(sub.upload_file);
    	$('#updateSubmissionForm').attr('action', 'my-submission?submission-update=' + sub.id);
    	$("#submissionModal").modal();
    }
</script>
</body>
</html>