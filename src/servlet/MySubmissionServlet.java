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

import dao.SubmissionDAO;
import model.Submission;
import model.User;

/**
 * Servlet implementation class MySubmissionServlet
 */
@WebServlet("/MySubmissionServlet")
public class MySubmissionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String UPLOAD_DIRECTORY =  "C:/Users/Sathveegan/Desktop/uploads/" + "submission";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MySubmissionServlet() {
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
		
		if(request.getParameter("submission-delete") != null) {
			String res = null;
			try {
				int id = Integer.parseInt(request.getParameter("submission-delete"));
				File file = new File(UPLOAD_DIRECTORY + File.separator + SubmissionDAO.getSubmissionById(id).getUpload_file());
				file.delete();
				res = SubmissionDAO.deleteSubmission(id);
			} catch (Exception e) {
				e.printStackTrace();
				res = "Submission is not deleted";
			}
			request.setAttribute("msg", res);
		}
		
		ArrayList<Submission> submission = new ArrayList<>();
		try {
			submission = SubmissionDAO.getSubmission(user.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("submission", submission);
		
		request.getRequestDispatcher("WEB-INF/my-submission.jsp").forward(request, response);
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
		if (upload_file == null) {
			status = false;
		}
        
        if(status) {
			try {
				String update_id = request.getParameter("submission-update");
				if (update_id != null && !update_id.trim().equals("")) {
					Submission submission = SubmissionDAO.getSubmissionById(Integer.parseInt(update_id));
					if(upload_file != null) {
						submission.setUpload_file(upload_file);
						submission.setSubmit_date(new Date());
					}
					String res = SubmissionDAO.updateSubmission(submission);
					request.setAttribute("msg", res);
				}
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("msg", "Submission is not updated.");
			}
        } else {
        	request.setAttribute("msg", "All fields are required.");
        }
        
        doGet(request, response);
	}

}
