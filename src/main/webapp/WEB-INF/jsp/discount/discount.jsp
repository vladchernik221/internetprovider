<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:bundle basename="pagecontent/discount_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="discount.description" /></title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/discount.js"></script>
</head>
<body>
<!-- Header -->
<header id="header">
    <jsp:include page="../template/header.jsp" />
</header>

<!-- Main part -->
<section class="wrapper style2">
    <div class="container">
        <c:if test="${sessionScope.user.userRole == 'ADMIN'}">
            <div class="row"><div class="align-left col-2">
                    <a href="/service" class="button small"><fmt:message key="discount.toList" /></a>
                </div><div class="align-right actions col-2">
                    <a href="/service/${discount.discountId}/edit" class="button small"><fmt:message key="edit" /></a>
                    <button class="button small" onclick="remove(${discount.discountId})"><fmt:message key="remove" /></button>
                </div></div>
        </c:if>
        <h1>${discount.name}</h1>
        <p>${discount.description}</p>
        <table class="description">
            <tr>
                <th><fmt:message key="discount.startDate" /></th>
                <td>${discount.startDate}</td>
            </tr>
            <tr>
                <th><fmt:message key="discount.endDate" /></th>
                <td>${discount.endDate}</td>
            </tr>
            <tr>
                <th><fmt:message key="discount.amount" /></th>
                <td>${discount.amount}%</td>
            </tr>
            <tr>
                <td colspan="2">
                    <c:choose>
                        <c:when test="${discount.onlyForNewClient}"><fmt:message key="discount.forNew" /></c:when>
                        <c:otherwise><fmt:message key="discount.forAll" /></c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
    </div>
</section>

<!-- Footer -->
<jsp:include page="../template/footer.jsp" />
</body>
</html>
</fmt:bundle>