<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Приложения к договору</title>

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
<section class="wrapper style1 align-center">
    <div class="container">
        <header class="major">
            <h2>Приложения к договору <fmt:formatNumber type = "number" minIntegerDigits = "6" value = "${contractId}" /></h2>
            <a href="/contract/${contractId}/annex/new" class="button">Создать приложение к договору</a>
        </header>
        <c:choose>
            <c:when test="${contractAnnexesPage.pagesCount == 0}">
                <h2 class="warn">Не создано ни одного приложения</h2>
            </c:when>
            <c:otherwise>
                <table class="list">
                    <tr>
                        <th>Дата заключения</th>
                        <th>Адрес</th>
                        <th>Тарифный план</th>
                        <th></th>
                    </tr>
                    <c:forEach items="${contractAnnexesPage.data}" var="annex">
                        <tr onclick="redirect('/contract/annex/${annex.contractAnnexId}')">
                            <td>${annex.concludeDate}</td>
                            <td>${annex.address}</td>
                            <td>${annex.tariffPlan.name}</td>
                            <td>
                                <div class="icon small
                                    <c:choose>
                                        <c:when test="${annex.canceled}">fa-ban</c:when>
                                        <c:otherwise>fa-file-text-o</c:otherwise>
                                    </c:choose>"></div>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <c:if test="${contractAnnexesPage.pagesCount != 1}">
                    <div class="hidden" id="pagesCount">${contractAnnexesPage.pagesCount}</div>
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
