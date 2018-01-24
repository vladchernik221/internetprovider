<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:bundle basename="pagecontent/service_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="service.description" /></title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/service.js"></script>
</head>
<body>
<!-- Header -->
<header id="header">
    <jsp:include page="../template/header.jsp" />
</header>

<!-- Main part -->
<section class="wrapper style2">
    <div class="container">
        <div class="row"><div class="align-left col-2">
                <a href="/service" class="button small"><fmt:message key="service.toList" /></a>
            </div><!--
            --><c:if test="${sessionScope.user.userRole == 'ADMIN'}"><div class="align-right actions col-2">
                <a href="/service/${service.serviceId}/edit" class="button small"><fmt:message key="edit" /></a>
                <button class="button small" onclick="change_archived(${service.serviceId})">
                    <c:choose>
                        <c:when test="${!service.archived}"><fmt:message key="service.toArchive" /></c:when>
                        <c:otherwise><fmt:message key="service.fromArchive" /></c:otherwise>
                    </c:choose>
                </button>
            </div></c:if><!--
        --></div>
        <c:if test="${service.archived}">
            <h2 class="warn"><fmt:message key="service.inArchive" /></h2>
        </c:if>
        <h1>${service.name}</h1>
        <p>${service.description}</p>
        <table class="description">
            <tr>
                <th><fmt:message key="service.cost" /></th>
                <td>${service.price}</td>
            </tr>
        </table>
    </div>
</section>

<!-- Footer -->
<jsp:include page="../template/footer.jsp" />
</body>
</html>
</fmt:bundle>