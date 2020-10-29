package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int MEAL_ID = START_SEQ + 2;

    public static final Meal meal = new Meal(MEAL_ID, USER_ID, LocalDateTime.parse("2020-01-30T10:00"), "Завтрак", 500);
    public static final List<Meal> meals = Arrays.asList(
            new Meal(MEAL_ID + 6, USER_ID, LocalDateTime.parse("2020-01-31T20:00"), "Ужин", 510),
            new Meal(MEAL_ID + 5, USER_ID, LocalDateTime.parse("2020-01-31T13:00"), "Обед", 1000),
            new Meal(MEAL_ID + 4, USER_ID, LocalDateTime.parse("2020-01-31T10:00"), "Завтрак", 500),
            new Meal(MEAL_ID + 3, USER_ID, LocalDateTime.parse("2020-01-31T00:00"), "Еда на граничное значение", 100),
            new Meal(MEAL_ID + 2, USER_ID, LocalDateTime.parse("2020-01-30T20:00"), "Ужин", 500),
            new Meal(MEAL_ID + 1, USER_ID, LocalDateTime.parse("2020-01-30T13:00"), "Обед", 1000),
            meal);

    public static Meal getNew() {
        return new Meal(null, USER_ID, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Новая еда", 250);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meal);
        updated.setDescription("UpdatedDescription");
        updated.setCalories(330);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
