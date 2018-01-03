<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script type="text/javascript" src="/static/js_test/test.js"></script>
    <link type="image/x-icon" rel="shortcut icon" href="/static/image/meow.ico"/>
</head>
<body>
test
<form id="form1" method="post" action="/test">
    <label>login:
        <input type="text" name="login">
    </label>
    <br/>
    <label>password:
        <input type="password" name="password">
    </label>
    <br/>
    <input type="submit" value="Login" name="auth"/>
    <input type="text" value="" id="res">
</form>
</body>
</html>
