<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="ru_RU" scope="session"/>
<fmt:bundle basename="pagecontent/index_content">
<html>
<head>
    <meta charset="UTF-8">
    <title>Internet Provider</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
</head>
<body>
<!-- Header -->
<header id="header" class="transparent top">
    <jsp:include page="template/header.jsp" />
</header>

<!-- Banner -->
<section id="banner">
    <h2>Internet Provider</h2>
    <p><fmt:message key="index.greeting" /></p>
    <a href="/about" class="button big"><fmt:message key="index.about" /></a>
</section>

<!-- Main part -->
<section class="wrapper style1 align-center">
    <div class="container">
        <header class="major">
            <h2><fmt:message key="index.title" /></h2>
            <p><fmt:message key="index.description" /></p>
        </header>
        <div class="row"><section class="box col-3" onclick="redirect('/tariff-plan')">
                <div class="icon big rounded blue fa-tasks"></div>
                <h3><fmt:message key="tariffs" /></h3>
                <p><fmt:message key="tariffs.description" /></p>
            </section><section class="box col-3" onclick="redirect('/service')">
                <div class="icon big rounded green fa-wrench"></div>
                <h3><fmt:message key="services" /></h3>
                <p><fmt:message key="services.description" /></p>
            </section><section class="box col-3" onclick="redirect('/discount')">
                <div class="icon big rounded red fa-fire"></div>
                <h3><fmt:message key="discounts" /></h3>
                <p><fmt:message key="discounts.description" /></p>
            </section></div>
    </div>
</section>

<!-- Footer -->
<jsp:include page="template/footer.jsp" />
</body>
</html>
</fmt:bundle>