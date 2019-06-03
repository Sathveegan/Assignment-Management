<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="model.Assignment"%>
<%@ page import="dao.UserDAO"%>
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
			<a href="my-submission" class="btn btn-primary mt-2 mr-2 float-right">My Submission</a>
			</div>
		</div>
		
		<%
		ArrayList<Assignment> assignment = (ArrayList<Assignment>) request.getAttribute("assignment");
		
		if(assignment.size() == 0){
			%><h4 style="text-align: center;">No more Assignment.</h4><%
		}
		
		for(int i=0; i<assignment.size(); i++){
			%>
			<div class="row">
				<div class="col-sm-12">
					<div class="card m-3">
					  <div class="card-body">
					    <h5 class="card-title"><%= assignment.get(i).getTitle() %></h5>
					    <h6 class="card-subtitle mb-2 text-muted"><%= assignment.get(i).getSubject() %></h6>
					    <h6 class="card-subtitle mb-2 text-muted">Year: <%= assignment.get(i).getYear() %></h6>
					    <h6 class="card-subtitle mb-2 text-muted">Semester: <%= assignment.get(i).getSemester() %></h6>
					    <h6 class="card-subtitle mb-2 text-muted"><%= UserDAO.getUserById(assignment.get(i).getLecturer_id()).getName() %> - <%= UserDAO.getUserById(assignment.get(i).getLecturer_id()).getEmail() %></h6>
					    <p class="card-text">Post Date: <%= assignment.get(i).getPost_date() %><br/>
					    Due Date: <%= assignment.get(i).getDue_date() %></p>
					    <% 
					    	if(assignment.get(i).getUpload_file() != null){
					    		%>
					    		<a href="downloadFile?filename=assignment/<%= assignment.get(i).getUpload_file() %>" class="card-link"><%= assignment.get(i).getUpload_file() %></a>
					    		<%
					    	}
					    %>
					    <button type="button" onclick='addSubmission(<%=new Gson().toJson(assignment.get(i))%>)' class="btn btn-success mt-2 mr-2 float-right">Add Submission</button>
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
	    <form id="addSubmissionForm" method="post" action="lhome" enctype="multipart/form-data">
	      <div class="modal-header">
	        <h5 class="modal-title" id="submissionModalLabel">Add Submission</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        
			  <div class="form-group">
			    <p class="font-weight-bold">Title</p>
			    <p id="title"></p>
			  </div>
			  <div class="form-group">
			    <p class="font-weight-bold">Subject</p>
			    <p id="subject"></p>
			  </div>
			  <div class="form-group">
			    <p class="font-weight-bold">Year</p>
			    <p id="year"></p>
			  </div>
			  <div class="form-group">
			    <p class="font-weight-bold">Semester</p>
			    <p id="semester"></p>
			  </div>
			  <div class="form-group">
			    <p class="font-weight-bold">Post Date</p>
			    <p id="post_date"></p>
			  </div>
			  <div class="form-group">
			    <p class="font-weight-bold">Due Date</p>
			    <p id="due_date"></p>
			  </div>
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
    $('#addSubmissionForm #upload_file').on('change',function(){
        var fileName = $(this).val().split('\\');
        $(this).next('.custom-file-label').html(fileName[fileName.length-1]);
    });
    
    function addSubmission(sub){
    	$('#addSubmissionForm #title').text(sub.title);
    	$('#addSubmissionForm #subject').text(sub.subject);
    	$('#addSubmissionForm #year').text(sub.year);
    	$('#addSubmissionForm #semester').text(sub.semester);
    	
    	let due_date = new Date(sub.due_date);
    	$('#addSubmissionForm #due_date').text(due_date.getFullYear() + "-" + addPrefix(due_date.getMonth() + 1) + "-" + addPrefix(due_date.getDate()));
    	let post_date = new Date(sub.post_date);
    	$('#addSubmissionForm #post_date').text(post_date.getFullYear() + "-" + addPrefix(post_date.getMonth() + 1) + "-" + addPrefix(post_date.getDate()));
    	$('#addSubmissionForm').attr('action', 'shome?add-submission=' + sub.id);
    	$("#submissionModal").modal();
    }
    
    function addPrefix(str) {
   	  return ('0' + str).slice(-2)
   	}
</script>
</body>
</html>