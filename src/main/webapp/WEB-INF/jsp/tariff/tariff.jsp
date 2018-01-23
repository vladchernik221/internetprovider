<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="ru_RU" scope="session"/>
<fmt:bundle basename="pagecontent/tariff_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="tariff.description" /></title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/tariff.js"></script>
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
                <a href="/tariff-plan" class="button small"><fmt:message key="tariff.toList" /></a>
            </div><div class="align-right actions col-2">
                <a href="/tariff-plan/${tariffPlan.tariffPlanId}/edit" class="button small"><fmt:message key="edit" /></a>
                <button class="button small" onclick="change_archived(${tariffPlan.tariffPlanId})">
                    <c:choose>
                        <c:when test="${!tariffPlan.archived}"><fmt:message key="tariff.toArchive" /></c:when>
                        <c:otherwise><fmt:message key="tariff.fromArchive" /></c:otherwise>
                    </c:choose>
                </button>
            </div></div>
        <c:if test="${tariffPlan.archived}">
            <h2 class="warn"><fmt:message key="tariff.inArchive" /></h2>
        </c:if>
        <h1>${tariffPlan.name}</h1>
        <p>${tariffPlan.description}</p>
        <table class="description">
            <tr>
                <th><fmt:message key="tariff.monthlyFee" /></th>
                <td>${tariffPlan.monthlyFee}</td>
            </tr>
            <tr>
                <th><fmt:message key="tariff.downSpeed" /></th>
                <td>${tariffPlan.downSpeed}</td>
            </tr>
            <tr>
                <th><fmt:message key="tariff.upSpeed" /></th>
                <td>${tariffPlan.upSpeed}</td>
            </tr>
            <tr>
                <th><fmt:message key="tariff.includedTraffic" /></th>
                <td>${tariffPlan.includedTraffic}</td>
            </tr>
            <tr>
                <th><fmt:message key="tariff.priceOverTraffic" /></th>
                <td>${tariffPlan.priceOverTraffic}</td>
            </tr>
        </table>
        <c:if test="${fn:length(tariffPlan.discounts) != 0}">
            <div class="discount slider">
                <ul>
                    <c:forEach items="${tariffPlan.discounts}" var="discount">
                        <li onclick="redirect('/discount/${discount.discountId}')">
                            <h2>${discount.name}</h2>
                            <p>${discount.description}</p>
                            <p class="important uppercase"><fmt:message key="discount" /> ${discount.amount}%</p>
                            <p><fmt:message key="discount.from" /> <fmt:formatDate type="date" dateStyle="short" value="${discount.startDate}"/> <fmt:message key="discount.to" /> <fmt:formatDate type="date" dateStyle="short" value="${discount.endDate}"/></p>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
    </div>
</section>

<!-- Footer -->
<jsp:include page="../template/footer.jsp" />
</body>
</html>
</fmt:bundle>