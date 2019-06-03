<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="model.Assignment"%>
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
			<button type="button" class="btn btn-primary mt-2 mr-2 float-right" data-toggle="modal" data-target="#assignModal">Add Assignment</button>
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
					    <p class="card-text">Post Date: <%= assignment.get(i).getPost_date() %><br/>
					    Due Date: <%= assignment.get(i).getDue_date() %></p>
					    <% 
					    	if(assignment.get(i).getUpload_file() != null){
					    		%>
					    		<a href="downloadFile?filename=assignment/<%= assignment.get(i).getUpload_file() %>" class="card-link"><%= assignment.get(i).getUpload_file() %></a>
					    		<%
					    	}
					    %>
					    <a href="lhome?assign-delete=<%=assignment.get(i).getId()%>" class="btn btn-danger mt-2 mr-2 float-right">Delete</a>
					    <button type="button" onclick='updateAssignment(<%=new Gson().toJson(assignment.get(i))%>)' class="btn btn-warning mt-2 mr-2 float-right">Update</button>
					    <a href="student-submission?assignment=<%=assignment.get(i).getId() %>" class="btn btn-success mt-2 mr-2 float-right">Submissions</a>
					  </div>
					</div>
				</div>
			</div>			
			<%
		}
		
		%>
	
	</div>
	
	<!-- Modal -->
	<div class="modal fade" id="assignModal" tabindex="-1" role="dialog" aria-labelledby="assignModalLabel" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	    <form id="addAssignmentForm" method="post" action="lhome" enctype="multipart/form-data">
	      <div class="modal-header">
	        <h5 class="modal-title" id="assignModalLabel">Add Assignment</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        
			  <div class="form-group">
			    <label for="title">Title</label>
			    <input type="text" class="form-control" id="title" name="title" placeholder="Title" required>
			  </div>
			  <div class="form-group">
			    <label for="subject">Subject</label>
			    <input type="text" class="form-control" id="subject" name="subject" placeholder="Subject" required>
			  </div>
			  <div class="form-group">
			    <label for="year">Year</label>
			    <input type="number" class="form-control" id="year" name="year" placeholder="Year" required>
			  </div>
			  <div class="form-group">
			    <label for="semester">Semester</label>
			    <input type="number" class="form-control" id="semester" name="semester" placeholder="Semester" required>
			  </div>
			  <div class="form-group">
			    <label for="due_date">Due Date</label>
			    <input type="date" class="form-control" id="due_date" name="due_date" placeholder="Due date" required>
			  </div>
			  <div class="form-group">
				  <label for="ass_file">Assignment File</label>
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
	
	<!-- Modal -->
	<div class="modal fade" id="assignUModal" tabindex="-1" role="dialog" aria-labelledby="assignUModalLabel" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	    <form id="updateAssignmentForm" method="post" enctype="multipart/form-data">
	      <div class="modal-header">
	        <h5 class="modal-title" id="assignUModalLabel">Update Assignment</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        
			  <div class="form-group">
			    <label for="title">Title</label>
			    <input type="text" class="form-control" id="title" name="title" placeholder="Title" required>
			  </div>
			  <div class="form-group">
			    <label for="subject">Subject</label>
			    <input type="text" class="form-control" id="subject" name="subject" placeholder="Subject" required>
			  </div>
			  <div class="form-group">
			    <label for="year">Year</label>
			    <input type="number" class="form-control" id="year" name="year" placeholder="Year" required>
			  </div>
			  <div class="form-group">
			    <label for="semester">Semester</label>
			    <input type="number" class="form-control" id="semester" name="semester" placeholder="Semester" required>
			  </div>
			  <div class="form-group">
			    <label for="due_date">Due Date</label>
			    <input type="date" class="form-control" id="due_date" name="due_date" placeholder="Due date" required>
			  </div>
			  <div class="form-group">
				  <label for="ass_file">Assignment File</label>
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
    $('#addAssignmentForm #upload_file').on('change',function(){
        var fileName = $(this).val().split('\\');
        $(this).next('.custom-file-label').html(fileName[fileName.length-1]);
    });
    
    $('#updateAssignmentForm #upload_file').on('change',function(){
        var fileName = $(this).val().split('\\');
        $(this).next('.custom-file-label').html(fileName[fileName.length-1]);
    });
    
    function updateAssignment(assign){
    	$('#updateAssignmentForm #title').val(assign.title);
    	$('#updateAssignmentForm #subject').val(assign.subject);
    	$('#updateAssignmentForm #year').val(assign.year);
    	$('#updateAssignmentForm #semester').val(assign.semester);
    	$('#updateAssignmentForm .custom-file-label').html(assign.upload_file);
    	let due_date = new Date(assign.due_date);
    	$('#updateAssignmentForm #due_date').val(due_date.getFullYear() + "-" + addPrefix(due_date.getMonth() + 1) + "-" + addPrefix(due_date.getDate()));
    	$('#updateAssignmentForm').attr('action', 'lhome?assign-update=' + assign.id);
    	$("#assignUModal").modal();
    }
    
    function addPrefix(str) {
   	  return ('0' + str).slice(-2)
   	}
</script>
</body>
</html>