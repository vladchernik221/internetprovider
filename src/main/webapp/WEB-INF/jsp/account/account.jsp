<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="ru_RU" scope="session"/>
<fmt:bundle basename="pagecontent/account_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="account" /></title>

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
            <a href="/contract/annex/${account.contractAnnex.contractAnnexId}" class="button small">К приложению</a>
        </div>
        <h1><fmt:message key="account.number" /> <fmt:formatNumber type = "number" groupingUsed="false" minIntegerDigits = "6" value = "${account.contractAnnex.contractAnnexId}" /></h1>
        <table class="description">
            <tr>
                <th><fmt:message key="account.balance" /></th>
                <td>${account.balance}</td>
            </tr>
            <tr>
                <th><fmt:message key="account.usedTraffic" /></th>
                <td>${account.usedTraffic}</td>
            </tr>
        </table>
        <c:choose>
        <c:when test="${account.transactions.pagesCount == 0}">
            <h2 class="warn"><fmt:message key="transaction.notCreated" /></h2>
        </c:when>
        <c:otherwise>
            <h2><fmt:message key="transactions" /></h2>
            <table class="list">
                <tr>
                    <th><fmt:message key="transaction.date" /></th>
                    <th><fmt:message key="transaction.amount" /></th>
                    <th><fmt:message key="transaction.type" /></th>
                </tr>
                <c:forEach items="${account.transactions.data}" var="transaction">
                    <tr>
                        <td>${transaction.date}</td>
                        <td>${transaction.amount}</td>
                        <td>
                            <c:choose>
                                <c:when test="${transaction.type} == 'WRITE_OFF'"><fmt:message key="transaction.writeOff" /></c:when>
                                <c:otherwise><fmt:message key="transaction.refill" /></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <c:if test="${account.transactions.pagesCount != 1}">
                <div class="hidden" id="pagesCount">${account.transactions.pagesCount}</div>
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