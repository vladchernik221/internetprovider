<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Акции</title>

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
            <h2>Акции</h2>
            <a href="/discount/new" class="button">Создать акцию</a>
        </header>
        <c:choose>
            <c:when test="${discountsPage.pagesCount == 0}">
                <h2 class="warn">Не создано ни одной акции</h2>
            </c:when>
            <c:otherwise>
                <table class="list">
                    <tr>
                        <th>Название</th>
                        <th>Размер скидки</th>
                        <th>Дата начала</th>
                        <th>Дата окончания</th>
                        <th>Только для новых клиентов</th>
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
                                <c:when test="${discount.onlyForNewClient}">Да</c:when>
                                <c:otherwise>Нет</c:otherwise>
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