<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Услуги</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>
    <link href="/static/css/lib/pagination-plugin.css" rel="stylesheet" />

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/lib/pagination-plugin.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/service.js"></script>
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
            <h2>Услуги</h2>
            <a href="/service/new" class="button">Создать услугу</a>
        </header>
        <c:choose>
            <c:when test="${servicesPage.pagesCount == 0}">
                <h2 class="warn">Не создано ни одной услуги</h2>
            </c:when>
            <c:otherwise>
                <input type="checkbox" id="archived" onchange="show_archived(this)" <c:if test="${param.archived == true}">checked</c:if> />
                <label for="archived">Показывать архивные</label>
                <table class="list">
                    <tr>
                        <th>Название</th>
                        <th>Стоимость</th>
                        <th></th>
                        <th></th>
                    </tr>
                    <c:forEach items="${servicesPage.data}" var="service">
                        <tr onclick="redirect('/service/${service.serviceId}')">
                            <td>${service.name}</td>
                            <td>${service.price}</td>
                            <td>
                                <div class="icon small fa-edit" onclick="redirect('/service/${service.serviceId}/edit', event)"></div>
                            </td>
                            <td>
                                <div class="icon small
                                    <c:choose>
                                        <c:when test="${!service.archived}">fa-archive</c:when>
                                        <c:otherwise>fa-share</c:otherwise>
                                    </c:choose>
                                " onclick="change_archived(${service.serviceId}, event)"></div>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <c:if test="${servicesPage.pagesCount != 1}">
                    <div class="hidden" id="pagesCount">${servicesPage.pagesCount}</div>
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
