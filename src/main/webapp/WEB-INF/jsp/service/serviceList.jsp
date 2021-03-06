<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:bundle basename="pagecontent/serviceList_content">
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
            <c:if test="${sessionScope.user.userRole == 'ADMIN'}">
                <a href="/service/new" class="button"><fmt:message key="service.create" /></a>
            </c:if>
        </header>
        <c:choose>
            <c:when test="${servicesPage.pagesCount == 0}">
                <h2 class="warn"><fmt:message key="service.notCreated" /></h2>
            </c:when>
            <c:otherwise>
                <c:if test="${supportArchived}">
                    <input type="checkbox" id="archived" onchange="show_archived(this)" <c:if test="${param.archived == true}">checked</c:if> />
                    <label for="archived"><fmt:message key="service.showArchived" /></label>
                </c:if>
                <table class="list">
                    <tr>
                        <th><fmt:message key="service.name" /></th>
                        <th><fmt:message key="service.cost" /></th>
                        <c:if test="${sessionScope.user.userRole == 'ADMIN'}">
                            <th></th>
                            <th></th>
                        </c:if>
                    </tr>
                    <c:forEach items="${servicesPage.data}" var="service">
                        <tr onclick="redirect('/service/${service.serviceId}')">
                            <td>${service.name}</td>
                            <td>${service.price}</td>
                            <c:if test="${sessionScope.user.userRole == 'ADMIN'}">
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
                            </c:if>
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