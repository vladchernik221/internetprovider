<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Описание акции</title>

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
        <div class="row"><div class="align-left col-2">
                <a href="/service" class="button small">К списку</a>
            </div><div class="align-right actions col-2">
                <a href="/service/${discount.discountId}/edit" class="button small">Редактировать</a>
                <button class="button small" onclick="remove(${discount.discountId})">Удалить</button>
            </div></div>
        <h1>${discount.name}</h1>
        <p>${discount.description}</p>
        <table class="description">
            <tr>
                <th>Дата начала</th>
                <td>${discount.startDate}</td>
            </tr>
            <tr>
                <th>Дата окончания</th>
                <td>${discount.endDate}</td>
            </tr>
            <tr>
                <th>Величина скидки</th>
                <td>${discount.amount}%</td>
            </tr>
            <tr>
                <td colspan="2">
                    <c:choose>
                        <c:when test="${discount.onlyForNewClient}">Действует только для новых клиентов</c:when>
                        <c:otherwise>Действует для всех клиентов</c:otherwise>
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
