<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table cellspacing="0" border="1" cellpadding="8">
    <tr>
        <th>DateTime</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan="2">Actions</th>
    </tr>
    <c:forEach items="${meals}" var="meal" varStatus="loop">
        <tr style="color:<c:out value="${meal.excess ? 'red' : 'green'}"/>">
            <td><c:out value="${meal.dateTime}"/></td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
            <td><a href="<c:url value="meals?action=edit&mealId=${loop.index}"/>">Edit</a></td>
            <td><a href="<c:url value="meals?action=delete&mealId=${loop.index}"/>"
                   onclick="return confirm('Вы уверены?');">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<p><a href="meals?action=add">Add meal</a></p>
</body>
</html>
