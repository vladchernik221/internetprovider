<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:bundle basename="pagecontent/error_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="error" /></title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/></head>
<body>
<header class="major">
    <h1><fmt:message key="error" /> ${requestScope['javax.servlet.error.status_code']}</h1>
    <c:choose>
        <c:when test="${requestScope['javax.servlet.error.message'] != ''}">
            <p>${requestScope['javax.servlet.error.message']}</p>
        </c:when>
        <c:otherwise>
            <p><fmt:message key="error.commonDescription" /></p>
        </c:otherwise>
    </c:choose>
</header>
<div class="align-center">
    <img src="/static/images/error.jpg"><br/>
    <a href="/" class="button"><fmt:message key="error.home" /></a>
</div>
</body>
</html>
</fmt:bundle>