package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public Meal get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public void update(Meal meal) {
        checkNotFoundWithId(repository.save(meal), meal.getId());
    }

    public Collection<Meal> getAll() {
        return repository.getAll();
    }

    public Collection<Meal> getByFilter(int userId, LocalDate startDate, LocalDate endDate) {
        Stream<Meal> str = getAll().stream().filter(meal -> meal.getUserId() == userId);
        if (startDate != LocalDate.MIN) str = str.filter(meal -> meal.getDate().isAfter(startDate));
        if (endDate != LocalDate.MAX) str = str.filter(meal -> meal.getDate().isBefore(endDate));
        return str.collect(Collectors.toList());
    }

}