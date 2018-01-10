<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Тарифы</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>
    <link href="/static/css/lib/pagination-plugin.css" rel="stylesheet" />

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/lib/pagination-plugin.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/tariff.js"></script>
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
            <h2>Тарифные планы</h2>
            <a href="/tariff-plan/new" class="button">Создать тарифный план</a>
        </header>
        <input type="checkbox" id="archived" onchange="show_archived(this)" <c:if test="${param.archived == true}">checked</c:if> />
        <label for="archived">Показывать архивные</label>
        <table class="list">
            <tr>
                <th>Название</th>
                <th>Абонентская плата</th>
                <th>Скорость приема</th>
                <th>Скорость передачи</th>
                <th>Включенный трафик</th>
                <th></th>
                <th></th>
            </tr>
            <c:forEach items="${tariffPlansPage.data}" var="tariffPlan">
                <tr onclick="redirect('/tariff-plan/${tariffPlan.tariffPlanId}')">
                    <td>${tariffPlan.name}</td>
                    <td>${tariffPlan.monthlyFee}</td>
                    <td>${tariffPlan.downSpeed}</td>
                    <td>${tariffPlan.upSpeed}</td>
                    <td>${tariffPlan.includedTraffic}</td>
                    <td>
                        <div class="icon small fa-edit" onclick="redirect('/tariff-plan/${tariffPlan.tariffPlanId}/edit', event)"></div>
                    </td>
                    <td>
                        <div class="icon small
                            <c:choose>
                                <c:when test="${!tariffPlan.archived}">fa-archive</c:when>
                                <c:otherwise>fa-share</c:otherwise>
                            </c:choose>
                        " onclick="change_archived(${tariffPlan.tariffPlanId}, event)"></div>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <c:if test="${tariffPlansPage.pagesCount != 1}">
            <div class="hidden" id="pagesCount">${tariffPlansPage.pagesCount}</div>
            <div id="pagination-holder"></div>
        </c:if>
    </div>
</section>

<!-- Footer -->
<jsp:include page="../template/footer.jsp" />
</body>
</html>
