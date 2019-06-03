package servlet;

import java.io.File;
import java.io.IOException;
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
import dao.SubmissionDAO;
import model.Assignment;
import model.Submission;
import model.User;

/**
 * Servlet implementation class StudentAssignServlet
 */
@WebServlet("/StudentAssignServlet")
public class StudentAssignServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String UPLOAD_DIRECTORY =  "C:/Users/Sathveegan/Desktop/uploads/" + "submission";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentAssignServlet() {
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
		if (!user.getRole().equalsIgnoreCase("student")) {
			request.setAttribute("msg", "Access denied.");
			request.getRequestDispatcher("WEB-INF/login.jsp").forward(request, response);
			return;
		}
		
		ArrayList<Assignment> assignment = new ArrayList<>();
		try {
			assignment = AssignmentDAO.getAssignment();
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("assignment", assignment);
		
		request.getRequestDispatcher("WEB-INF/shome.jsp").forward(request, response);
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
		if (!user.getRole().equalsIgnoreCase("student")) {
			request.setAttribute("msg", "Access denied.");
			request.getRequestDispatcher("WEB-INF/login.jsp").forward(request, response);
			return;
		}
		
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
                    }
                }
            } catch (Exception ex) {
               ex.getStackTrace();
            }          
        }
        
		boolean status = true;
		request.removeAttribute("msg");
		
		if (request.getParameter("add-submission") == null) {
			status = false;
		}
		if (upload_file == null) {
			status = false;
		}
        
        if(status) {
			try {
				Submission submission = new Submission(Integer.parseInt(request.getParameter("add-submission")), user.getId(), upload_file, new Date());
				String res = SubmissionDAO.insertSubmision(submission);
				request.setAttribute("msg", res);
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("msg", "Submission Failure.");
			}
        } else {
        	request.setAttribute("msg", "All fields are required.");
        }
        
        doGet(request, response);
        
	}

}
