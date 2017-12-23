<%--
  Created by IntelliJ IDEA.
  User: Владислав
  Date: 22.12.2017
  Time: 16:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/authentication" method="POST">
    login: <input type="text" name="login"/>
    <br/>
    password: <input type="password" name="password"/>
    <br/>
    <input type="submit" name="auth"/>
</form>
</body>
</html>
