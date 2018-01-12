<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Счёт</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>
    <link href="/static/css/lib/pagination-plugin.css" rel="stylesheet" />

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/lib/pagination-plugin.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/pagination.js"></script>
</head>
<body>
<!-- Header -->
<header id="header">
    <jsp:include page="../template/header.jsp" />
</header>

<!-- Main part -->
<section class="wrapper style2">
    <div class="container">
        <div class="row">
            <a href="/contract/123/annex/123" class="button small">К приложению</a>
        </div>
        <h1>Счёт номер 123</h1>
        <table class="description">
            <tr>
                <th>Баланс</th>
                <td>${account.balance}</td>
            </tr>
            <tr>
                <th>Остаток включённого трафика</th>
                <td>${account.traffickedTraffic}</td>
            </tr>
            <h2>Транзакции</h2>
            <table class="list">
                <tr>
                    <th>Дата</th>
                    <th>Количество</th>
                    <th>Тип</th>
                </tr>
                <c:forEach items="${account.transactions.data}" var="transaction">
                    <tr>
                        <td>${transaction.data}</td>
                        <td>${transaction.amount}</td>
                        <td>${transaction.type}</td>
                    </tr>
                </c:forEach>
            </table>
            <c:if test="${account.transactions.pagesCount != 1}">
                <div class="hidden" id="pagesCount">${account.transactions.pagesCount}</div>
                <div id="pagination-holder"></div>
            </c:if>
        </table>
    </div>
</section>

<!-- Footer -->
<jsp:include page="../template/footer.jsp" />
</body>
</html>
