<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:bundle basename="pagecontent/annexList_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="annex.list" /></title>

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
            <h2><fmt:message key="annex.list" /> <fmt:formatNumber type = "number" groupingUsed="false" minIntegerDigits = "6" value = "${contractId}" /></h2>
            <c:if test="${sessionScope.user.userRole == 'SELLER'}">
                <a href="/contract/${contractId}/annex/new" class="button"><fmt:message key="annex.create" /></a>
            </c:if>
        </header>
        <c:choose>
            <c:when test="${contractAnnexesPage.pagesCount == 0}">
                <h2 class="warn"><fmt:message key="annex.notCreated" /></h2>
            </c:when>
            <c:otherwise>
                <table class="list">
                    <tr>
                        <th><fmt:message key="annex.date" /></th>
                        <th><fmt:message key="annex.address" /></th>
                        <th><fmt:message key="annex.tariffPlan" /></th>
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
</fmt:bundle>