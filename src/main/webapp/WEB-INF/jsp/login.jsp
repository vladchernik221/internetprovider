<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:bundle basename="pagecontent/login_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="login" /></title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>
    <link href="/static/css/login.css" rel="stylesheet"/>

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/login.js"></script>
</head>
<body>
<!-- Header -->
<header id="header" class="transparent top">
    <div class="home"><a href="/"><fmt:message key="login.home" /></a></div>
</header>

<!-- Main part -->
<section class="login">
    <form id="login_form" action="/signin" method="POST" onsubmit="send(event)">
        <div class="row">
            <input type="text" name="login" placeholder="<fmt:message key="login.login" />"/>
        </div>
        <div class="row">
            <input type="password" name="password" placeholder="<fmt:message key="login.password" />"/>
        </div>
        <input type="submit" value="<fmt:message key="submit" />"/>
        <input type="reset" value="<fmt:message key="reset" />"/>
    </form>
</section>

<!-- Modal window -->
<jsp:include page="template/modal.jsp" />

</body>
</html>
</fmt:bundle>