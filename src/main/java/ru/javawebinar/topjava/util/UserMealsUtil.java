package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesOfDay = new HashMap<>();
        Map<LocalDate, AtomicBoolean> excessOfDay = new HashMap<>();
        List<UserMealWithExcess> filteredMeals = new ArrayList<>();
        meals.forEach(meal -> {
            LocalDate mealDate = meal.getDateTime().toLocalDate();
            caloriesOfDay.merge(mealDate, meal.getCalories(), Integer::sum);
            if (excessOfDay.containsKey(mealDate)) {
                excessOfDay.get(mealDate).set(caloriesOfDay.get(mealDate) > caloriesPerDay);
            } else {
                excessOfDay.put(mealDate, new AtomicBoolean(caloriesOfDay.get(mealDate) > caloriesPerDay));
            }
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                filteredMeals.add(new UserMealWithExcess(meal, excessOfDay.get(mealDate)));
            }
        });
        return filteredMeals;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(
                        Collectors.groupingBy(
                                meal -> meal.getDateTime().toLocalDate(),
                                Collector.of(
                                        () -> new TimeUtil(0, new AtomicBoolean(), new ArrayList<>()),
                                        (a, t) -> {
                                            a.setCount(a.getCount() + t.getCalories());
                                            a.setExcess(a.getCount() > caloriesPerDay);
                                            if (TimeUtil.isBetweenHalfOpen(t.getDateTime().toLocalTime(), startTime, endTime)) {
                                                a.getMeals().add(new UserMealWithExcess(t, a.getExcess()));
                                            }
                                        },
                                        (a, b) -> a
                                )
                        )
                )
                .values().stream()
                .map(TimeUtil::getMeals)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
