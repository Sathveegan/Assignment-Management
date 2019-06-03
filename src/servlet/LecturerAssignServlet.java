package servlet;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import dao.AssignmentDAO;
import model.Assignment;
import model.User;

/**
 * Servlet implementation class LecturerAssignServlet
 */
@WebServlet("/LecturerAssignServlet")
public class LecturerAssignServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String UPLOAD_DIRECTORY =  "C:/Users/Sathveegan/Desktop/uploads/" + "assignment";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LecturerAssignServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect("login");
			return;
		}
		User user = (User) session.getAttribute("user");
		if (!user.getRole().equalsIgnoreCase("lecturer")) {
			request.setAttribute("msg", "Access denied.");
			request.getRequestDispatcher("WEB-INF/login.jsp").forward(request, response);
			return;
		}
		
		if(request.getParameter("assign-delete") != null) {
			String res = null;
			try {
				int id = Integer.parseInt(request.getParameter("assign-delete"));
				File file = new File(UPLOAD_DIRECTORY + File.separator + AssignmentDAO.getAssignment(id).getUpload_file());
				file.delete();
				res = AssignmentDAO.deleteAssignment(id);
			} catch (Exception e) {
				e.printStackTrace();
				res = "Assignment is not deleted";
			}
			request.setAttribute("msg", res);
		}
		
		ArrayList<Assignment> assignment = new ArrayList<>();
		try {
			assignment = AssignmentDAO.getLecturerAssignment(user.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("assignment", assignment);
		
		request.getRequestDispatcher("WEB-INF/lhome.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect("login");
			return;
		}
		request.removeAttribute("msg");
		User user = (User) session.getAttribute("user");
		if (!user.getRole().equalsIgnoreCase("lecturer")) {
			request.setAttribute("msg", "Access denied.");
			request.getRequestDispatcher("WEB-INF/login.jsp").forward(request, response);
			return;
		}
		
		String title = null;
		String subject = null;
		int year = -99;
		int semester = -99;
		String due_date = null;
		String upload_file = null;
		
        if(ServletFileUpload.isMultipartContent(request)){
            try {
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
               
                for(FileItem item : multiparts){
                    if(!item.isFormField()){
                    	if(item.getName() != null && !item.getName().trim().equals("")) {
	                    	upload_file = new File(new Date().getTime()+ "_" +item.getName()).getName();
	                        item.write(new File(UPLOAD_DIRECTORY + File.separator + upload_file));
                    	}
                    } else {
                    	if(item.getFieldName().equals("title"))
                    		title = item.getString();
                    	else if(item.getFieldName().equals("subject"))
                    		subject = item.getString();
                    	else if(item.getFieldName().equals("year"))
                    		year = Integer.parseInt(item.getString());
                    	else if(item.getFieldName().equals("semester"))
                    		semester = Integer.parseInt(item.getString());
                    	else if(item.getFieldName().equals("due_date"))
                    		due_date = item.getString();
                    }
                }
            } catch (Exception ex) {
               ex.getStackTrace();
            }          
        }
        
		boolean status = true;
		if (title == null || title.trim().equals("")) {
			status = false;
		}
		if (subject == null || subject.trim().equals("")) {
			status = false;
		}
		if (year == -99) {
			status = false;
		}
		if (semester == -99) {
			status = false;
		}
		if (due_date == null) {
			status = false;
		}
        
        if(status) {
			try {
				Assignment assignment = new Assignment(title, subject, year, semester, new Date(), new SimpleDateFormat("yyyy-mm-dd").parse(due_date), upload_file, user.getId());

				String res = null;
				String update_id = request.getParameter("assign-update");
				if (update_id != null && !update_id.trim().equals("")) {
					assignment.setId(Integer.parseInt(update_id));
					if(upload_file == null) {
						upload_file = AssignmentDAO.getAssignment(Integer.parseInt(update_id)).getUpload_file();
						assignment.setUpload_file(upload_file);
					}
					res = AssignmentDAO.updateAssignment(assignment);
				} else {
					res = AssignmentDAO.insertAssignment(assignment);
				}
				request.setAttribute("msg", res);
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("msg", "Assignment is not inserted");
			}
        } else {
        	request.setAttribute("msg", "All fields are required.");
        }
        
        doGet(request, response);
        
	}

}
