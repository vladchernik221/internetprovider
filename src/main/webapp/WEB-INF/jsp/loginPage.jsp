<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:choose>
    <c:when test="${sessionScope.user == null}">
        <form method="post" action="/signin">
            login: <input type="text" name="login"/>
            <br/>
            password: <input type="password" name="password"/>
            <br/>
            <input type="submit" value="Login" name="auth"/>
        </form>
    </c:when>
    <c:otherwise>
        Hello ${sessionScope.user.login}!
        <br>
        <form method="post" action="/logout">
            <input type="submit" value="Logout" name="logout">
        </form>
    </c:otherwise>
</c:choose>
</body>
</html>
