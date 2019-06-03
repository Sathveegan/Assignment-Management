package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDAO;
import model.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("WEB-INF/login.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		boolean status = true;
		request.removeAttribute("msg");
		if (email.trim().equals("")) {
			status = false;
		}
		if (password.trim().equals("")) {
			status = false;
		}

		if (!status) {
			request.setAttribute("msg", "Email & Password are required.");
			request.getRequestDispatcher("WEB-INF/login.jsp").forward(request, response);
		} else {
			boolean res = UserDAO.login(email, password);

			if (res) {
				try {
					User user = UserDAO.getUserByEmail(email);
					HttpSession session = request.getSession(true);
					session.setAttribute("user", user);
					if(user.getRole().equalsIgnoreCase("lecturer"))
						response.sendRedirect("lhome");
					else if(user.getRole().equalsIgnoreCase("student"))
						response.sendRedirect("shome");
				} catch (SQLException e) {
					e.printStackTrace();
					request.setAttribute("msg", "Login Failed.");
					request.getRequestDispatcher("WEB-INF/login.jsp").forward(request, response);
				}
			} else {
				request.setAttribute("msg", "Login Failed.");
				request.getRequestDispatcher("WEB-INF/login.jsp").forward(request, response);
			}
		}
	}

}
