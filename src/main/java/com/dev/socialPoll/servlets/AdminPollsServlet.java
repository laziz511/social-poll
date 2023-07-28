package com.dev.socialPoll.servlets;

import com.dev.socialPoll.entity.Poll;
import com.dev.socialPoll.entity.User;
import com.dev.socialPoll.exception.ServiceException;
import com.dev.socialPoll.service.PollService;
import com.dev.socialPoll.service.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin-polls")
public class AdminPollsServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doGet of AdminPollsServlet is working");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Check if the user is logged in
        if (user == null) {
            response.sendRedirect("log-in");
            return;
        }

        PollService pollService = ServiceFactory.getInstance().getPollService();

        try {

            // Retrieve all polls created by the admin (user)
            List<Poll> adminPolls = pollService.getPollsByCreatorId(user.getId());

            // Set the admin polls in the request attribute to be displayed in the JSP
            request.setAttribute("adminPolls", adminPolls);
            request.getRequestDispatcher("html/admin/admin-polls.jsp").forward(request, response);

        } catch (ServiceException e) {
            logger.error("Error occurred while retrieving admin polls!", e);
            response.sendRedirect("html/error.jsp");
        }
    }
}
