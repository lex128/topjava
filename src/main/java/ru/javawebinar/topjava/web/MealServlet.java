package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forwards to meals");
        String forward = "";
        String action = request.getParameter("action");

        if (action != null && action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            MealsUtil.delete(mealId);
            forward = LIST_MEAL;
            request.setAttribute("meals", MealsUtil.getAll());
        } else if (action != null && action.equalsIgnoreCase("edit")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            forward = INSERT_OR_EDIT;
            request.setAttribute("meal", MealsUtil.getById(mealId));
            request.setAttribute("mealId", mealId);
        } else if (action != null && action.equalsIgnoreCase("add")) {
            forward = INSERT_OR_EDIT;
        } else {
            forward = LIST_MEAL;
            request.setAttribute("meals", MealsUtil.getAll());
        }
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal();

        try {
            meal.setDateTime(LocalDateTime.parse(request.getParameter("dateTime")));
        } catch (DateTimeParseException e) {
            meal.setDateTime(LocalDateTime.now());
        }

        String description = request.getParameter("description");
        if (description == null || description.isEmpty())
            meal.setDescription("Пожрал как самый умный");
        else
            meal.setDescription(description);

        try {
            int calories = Integer.parseInt(request.getParameter("calories"));
            meal.setCalories(Integer.max(calories, 0));
        } catch (NumberFormatException e) {
            meal.setCalories(0);
        }

        try {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            MealsUtil.update(mealId, meal);
        } catch (NumberFormatException e) {
            MealsUtil.add(meal);
        }

        request.setAttribute("meals", MealsUtil.getAll());
        request.getRequestDispatcher(LIST_MEAL).forward(request, response);
    }
}
