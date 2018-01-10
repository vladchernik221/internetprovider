<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ошибка</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/></head>
<body>
<header class="major">
    <h1>Ошибка ${requestScope['javax.servlet.error.status_code']}</h1>
    <c:choose>
        <c:when test="${requestScope['javax.servlet.error.message'] != ''}">
            <p>${requestScope['javax.servlet.error.message']}</p>
        </c:when>
        <c:otherwise>
            <p>На стороне сервера, произошла ошибка. Пожалуйста, свяжитесь с системным админстратором.</p>
        </c:otherwise>
    </c:choose>
</header>
<div class="align-center">
    <img src="/static/images/error.jpg"><br/>
    <a href="/" class="button">На главную</a>
</div>
</body>
</html>
