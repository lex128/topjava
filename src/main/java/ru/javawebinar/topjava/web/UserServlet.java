package ru.javawebinar.topjava.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String authId = request.getParameter("authId");
        if (authId != null && !authId.isEmpty())
            SecurityUtil.setAuthUserId(Integer.parseInt(authId));

        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}
