<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Приложения</title>

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
    <jsp:include page="../template/header.jsp" />
</header>

<!-- Main part -->
<section class="wrapper style1 align-center">
    <div class="container">
        <header class="major">
            <h2>Приложения к контракту № 300500</h2>
            <a href="/contract/123/annex/new" class="button">Создать приожение</a>
        </header>
        <c:choose>
            <c:when test="${annexes.length == 0}">
                <h2 class="warn">Не создано ни одного приложения</h2>
            </c:when>
            <c:otherwise>
                <input type="checkbox" id="canceled" <%--onchange="show_canceled(this)"--%> <c:if test="${param.canceled == true}">checked</c:if> />
                <label for="canceled">Показывать расторгнутые</label>
                <table class="list">
                    <tr>
                        <th>Дата заключения</th>
                        <th>Адрес</th>
                        <th>Тарифный план</th>
                        <th></th>
                    </tr>
                    <c:forEach items="${annexes}" var="annex">
                        <tr onclick="redirect('/contract/123/annex/123')">
                            <td>${annex.concludeDate}</td>
                            <td>${annex.address}</td>
                            <td>${annex.tariffPlan.name}</td>
                            <td>
                                <div class="icon small
                                    <c:choose>
                                        <c:when test="${service.canceled}">fa-ban</c:when>
                                        <c:otherwise>fa-file-text-o</c:otherwise>
                                    </c:choose>"></div>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<!-- Footer -->
<jsp:include page="../template/footer.jsp" />
</body>
</html>
