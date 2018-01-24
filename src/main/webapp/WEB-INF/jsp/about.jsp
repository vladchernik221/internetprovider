<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:bundle basename="pagecontent/about_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="about" /></title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
</head>
<body>
<!-- Header -->
<header id="header">
    <jsp:include page="template/header.jsp"/>
</header>

<!-- Main part -->
<section class="wrapper style2">
    <div class="container">
        <header class="major">
            <h2><fmt:message key="about" /> Internet Provider</h2>
        </header>
        <p><fmt:message key="about.chapter1" /></p>
        <p><fmt:message key="about.chapter2" /></p>
        <p><fmt:message key="about.chapter3" /></p>
    </div>
</section>

<!-- Footer -->
<jsp:include page="template/footer.jsp"/>
</body>
</html>
</fmt:bundle>