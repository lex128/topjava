<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="MealsUtil" class="ru.javawebinar.topjava.util.MealsUtil"/>
<html lang="ru">
<head>
    <title>Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2><c:out value="${mealId != null ? 'Edit' : 'Add'}" /> meal</h2>
<form method="post" action="meals">
    <table>
        <tr><td>DateTime:</td><td><input type="datetime-local" name="dateTime" value="<c:out value="${meal.dateTime}" />"/></td></tr>
        <tr><td>Description:</td><td><input type="text" name="description"  value="<c:out value="${meal.description}" />"/></td></tr>
        <tr><td>Calories:</td><td><input type="number" name="calories"  value="<c:out value="${meal.calories}" />"/></td></tr>
    </table>
    <input type="submit" value="Save"/><input type="reset" value="Reset"/>
    <input type="hidden" readonly="readonly" name="mealId" value="<c:out value="${mealId}" />"/>
</form>
<a href="meals">Return to meals</a>
</body>
</html>
