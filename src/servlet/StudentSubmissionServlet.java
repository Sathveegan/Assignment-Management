package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.SubmissionDAO;
import model.Submission;
import model.User;

/**
 * Servlet implementation class StudentSubmissionServlet
 */
@WebServlet("/StudentSubmissionServlet")
public class StudentSubmissionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentSubmissionServlet() {
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
		
		ArrayList<Submission> submission = new ArrayList<>();
		try {
			submission = SubmissionDAO.getSubmissionByAssignment(Integer.parseInt(request.getParameter("assignment")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("submission", submission);
		
		request.getRequestDispatcher("WEB-INF/student-submission.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
