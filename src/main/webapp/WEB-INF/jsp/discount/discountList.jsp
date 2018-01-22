<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="ru_RU" scope="session"/>
<fmt:bundle basename="pagecontent/discountList_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="discounts" /></title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>
    <link href="/static/css/lib/pagination-plugin.css" rel="stylesheet" />

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/lib/pagination-plugin.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/discount.js"></script>
    <script src="/static/js/pagination.js"></script>
</head>
<body>
<!-- Header -->
<header id="header">
    <jsp:include page="../template/header.jsp" />
</header>

<!-- Main part -->
<section class="wrapper style1 align-center">
    <div class="container">
        <header class="major">
            <h2><fmt:message key="discounts" /></h2>
            <a href="/discount/new" class="button"><fmt:message key="discount.create" /></a>
        </header>
        <c:choose>
            <c:when test="${discountsPage.pagesCount == 0}">
                <h2 class="warn"><fmt:message key="discount.notCreated" /></h2>
            </c:when>
            <c:otherwise>
                <table class="list">
                    <tr>
                        <th><fmt:message key="discount.name" /></th>
                        <th><fmt:message key="discount.amount" /></th>
                        <th><fmt:message key="discount.startDate" /></th>
                        <th><fmt:message key="discount.endDate" /></th>
                        <th><fmt:message key="discount.onlyForNew" /></th>
                        <th></th>
                        <th></th>
                    </tr>
                    <c:forEach items="${discountsPage.data}" var="discount">
                        <tr onclick="redirect('/discount/${discount.discountId}')">
                            <td>${discount.name}</td>
                            <td>${discount.amount}</td>
                            <td>${discount.startDate}</td>
                            <td>${discount.endDate}</td>
                            <td><c:choose>
                                <c:when test="${discount.onlyForNewClient}"><fmt:message key="yes" /></c:when>
                                <c:otherwise><fmt:message key="no" /></c:otherwise>
                            </c:choose></td>
                            <td>
                                <div class="icon small fa-edit" onclick="redirect('/discount/${discount.discountId}/edit', event)"></div>
                            </td>
                            <td>
                                <div class="icon small fa-remove" onclick="remove(${discount.discountId}, event)"></div>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <c:if test="${discountsPage.pagesCount != 1}">
                    <div class="hidden" id="pagesCount">${discountsPage.pagesCount}</div>
                    <div id="pagination-holder"></div>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<!-- Footer -->
<jsp:include page="../template/footer.jsp" />
</body>
</html>
</fmt:bundle>