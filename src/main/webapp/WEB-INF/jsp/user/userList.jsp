<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:bundle basename="pagecontent/userList_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="users" /></title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>
    <link href="/static/css/lib/pagination-plugin.css" rel="stylesheet" />

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/lib/pagination-plugin.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/user.js"></script>
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
            <h2><fmt:message key="user.accounts" /></h2>
            <a href="/user/new" class="button"><fmt:message key="user.create" /></a>
        </header>
        <div class="row">
            <input type="radio" name="role" value="ALL" id="role-all" onchange="show_with_role(this)" <c:if test="${param.role == null}">checked</c:if> />
            <label for="role-all"><fmt:message key="user.all" /></label>
        </div>
        <div class="row"><div class="col-3">
            <input type="radio" name="role" value="ADMIN" id="role-admin" onchange="show_with_role(this)" <c:if test="${param.role == 'ADMIN'}">checked</c:if> />
            <label for="role-admin"><fmt:message key="user.admins" /></label>
        </div><div class="col-3">
            <input type="radio" name="role" value="SELLER" id="role-seller" onchange="show_with_role(this)" <c:if test="${param.role == 'SELLER'}">checked</c:if> />
            <label for="role-seller"><fmt:message key="user.sellers" /></label>
        </div><div class="col-3">
            <input type="radio" name="role" value="CUSTOMER" id="role-customer" onchange="show_with_role(this)" <c:if test="${param.role == 'CUSTOMER'}">checked</c:if> />
            <label for="role-customer"><fmt:message key="user.customers" /></label>
        </div></div>
        <c:choose>
            <c:when test="${usersPage.pagesCount == 0}">
                <h2 class="warn"><fmt:message key="user.notCreated" /></h2>
            </c:when>
            <c:otherwise>
                <table class="list">
                    <tr>
                        <th><fmt:message key="user.login" /></th>
                        <th><fmt:message key="user.role" /></th>
                        <th></th>
                    </tr>
                    <c:forEach items="${usersPage.data}" var="user">
                        <tr onclick="redirect('user/${user.userId}/password')">
                            <td>${user.login}</td>
                            <td>${user.userRole}</td>
                            <td>
                                <div class="icon small
                                    <c:choose>
                                        <c:when test="${user.blocked}">fa-ban</c:when>
                                        <c:otherwise>fa-user</c:otherwise>
                                    </c:choose>
                                " onclick="change_blocked(${user.userId}, event)"></div>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <c:if test="${usersPage.pagesCount != 1}">
                    <div class="hidden" id="pagesCount">${usersPage.pagesCount}</div>
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