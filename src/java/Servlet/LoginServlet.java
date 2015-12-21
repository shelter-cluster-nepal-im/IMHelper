package Servlet;

import Util.validate;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class loginServlet
 */
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
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uname = request.getParameter("username");
        String pass = request.getParameter("password");
        if (uname != null) {
            System.out.println(uname);
            validate blayer = new validate();
            boolean check = blayer.isLogIn(uname, pass);
            if (check) {
                response.sendRedirect("home");
                HttpSession session = request.getSession();
                session.setAttribute("userName", uname);
            } else {
                String message = "Invalid Username or Password";
                request.getSession().setAttribute("message", message);
                response.sendRedirect("index.jsp");
                request.setAttribute("message", message);
            }
        } else {
            response.sendRedirect("index.jsp");
        }

    }

}
