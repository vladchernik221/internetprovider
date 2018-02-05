<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:bundle basename="pagecontent/serviceAnnexList_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="services" /></title>

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
            <h2><fmt:message key="services" /></h2>
        </header>
        <c:choose>
            <c:when test="${servicesPage.pagesCount == 0}">
                <h2 class="warn"><fmt:message key="service.notCreated" /></h2>
            </c:when>
            <c:otherwise>
                <table class="list">
                    <tr>
                        <th><fmt:message key="service.name" /></th>
                        <th><fmt:message key="service.cost" /></th>
                        <th></th>
                    </tr>
                    <c:forEach items="${servicesPage.data}" var="service">
                        <tr onclick="redirect('/service/${service.serviceId}')">
                            <td>${service.name}</td>
                            <td>${service.price}</td>
                            <td>
                                <button class="button small" onclick="order_service(${annexId}, ${service.serviceId}, event)"><fmt:message key="service.order" /></button>
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
</fmt:bundle>