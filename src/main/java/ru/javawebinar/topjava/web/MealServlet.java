package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Objects;

import static ru.javawebinar.topjava.web.SecurityUtil.*;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController rest;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        rest = new MealRestController(new MealService(new InMemoryMealRepository()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                authUserId(),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (meal.isNew()) {
            rest.create(meal);
        } else {
            rest.update(meal, meal.getId());
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String sD = request.getParameter("startDate");
        String eD = request.getParameter("endDate");
        String sT = request.getParameter("startTime");
        String eT = request.getParameter("endTime");
        LocalDate startDate = LocalDate.MIN, endDate = LocalDate.MAX;
        LocalTime startTime = LocalTime.MIN, endTime = LocalTime.MAX;

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                rest.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(authUserId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        rest.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                String msg = "";
                if (sD != null && !sD.isEmpty()) {
                    startDate = LocalDate.parse(sD);
                    msg = msg.concat(" startDate=" + startDate);
                }
                if (eD != null && !eD.isEmpty()) {
                    endDate = LocalDate.parse(eD);
                    msg = msg.concat(" endDate=" + endDate);
                }
                if (sT != null && !sT.isEmpty()) {
                    startTime = LocalTime.parse(sT);
                    msg = msg.concat(" startTime=" + startTime);
                }
                if (eT != null && !eT.isEmpty()) {
                    endTime = LocalTime.parse(eT);
                    msg = msg.concat(" endTime=" + endTime);
                }
                log.info("getAll{}", msg);
                Collection<Meal> meals = rest.getAll(startDate, endDate);
                if (startTime == LocalTime.MIN && endTime == LocalTime.MAX) {
                    request.setAttribute("meals", MealsUtil.getTos(meals, authUserCaloriesPerDay()));
                } else {
                    request.setAttribute("meals", MealsUtil.getFilteredTos(meals, authUserCaloriesPerDay(), startTime, endTime));
                }
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
