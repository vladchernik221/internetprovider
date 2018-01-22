<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="ru_RU" scope="session"/>
<fmt:bundle basename="pagecontent/clientIndex_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="clientPage" /></title>

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
    <jsp:include page="template/header.jsp" />
</header>

<!-- Main part -->
<section class="wrapper style1 admin-style align-center">
    <div class="container">
        <header class="major">
            <h2><fmt:message key="clientPage" /></h2>
            <p><fmt:message key="clientPage.description" /></p>
        </header>
        <div class="row"><section class="box col-3">
                <div class="icon medium rounded orange fa-briefcase"></div>
                <h3><fmt:message key="clientPage.contract" /></h3>
            </section><section class="box col-3">
                <div class="icon medium rounded violet fa-money"></div>
                <h3><fmt:message key="clientPage.accounts" /></h3>
            </section></div>
    </div>
</section>

<!-- Footer -->
<jsp:include page="template/footer.jsp" />
</body>
</html>
</fmt:bundle>