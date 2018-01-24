<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:bundle basename="pagecontent/discountForm_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="discount" /></title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet" />
    <link href="/static/css/skel.css" rel="stylesheet" />
    <link href="/static/css/style.css" rel="stylesheet" />

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
        <header class="major">
        <c:choose>
            <c:when test="${discount == null}">
                <h2><fmt:message key="discount.creation" /></h2>
            </c:when>
            <c:otherwise>
                <h2><fmt:message key="discount.edition" /></h2>
            </c:otherwise>
        </c:choose>
        </header>
        <c:choose>
            <c:when test="${discount == null}">
                <form id="discount_form" onsubmit="send_form(event)" method="POST" action="/discount/new">
            </c:when>
            <c:otherwise>
                <form id="discount_form" onsubmit="send_form(event)" method="POST" action="/discount/${discount.discountId}">
            </c:otherwise>
        </c:choose>
            <div class="row">
                <input type="text" name="name" placeholder="<fmt:message key="discount.name" />" value="${discount.name}" required />
                <label><fmt:message key="discount.name" /></label>
            </div>
            <div class="row">
                <textarea name="description" placeholder="<fmt:message key="discount.description" />" rows="6">${discount.description}</textarea>
                <label><fmt:message key="discount.description" /></label>
            </div>
            <div class="row">
                <input type="text" name="amount" placeholder="<fmt:message key="discount.amount" />" pattern="^(\d{1,2}|100)$"  value="${discount.amount}" required />
                <label><fmt:message key="discount.amount" /></label>
            </div>
            <div class="row"><div class="col-2">
                <input type="date" name="startDate" placeholder="<fmt:message key="discount.startDate" />" value="${discount.startDate}" required />
                <label><fmt:message key="discount.startDate" /></label>
            </div><div class="col-2">
                <input type="date" name="endDate" placeholder="<fmt:message key="discount.endDate" />" value="${discount.endDate}" required />
                <label><fmt:message key="discount.endDate" /></label>
            </div></div>
            <div class="row">
                <input type="checkbox" name="onlyForNewClient" value="true" id="onlyForNewClient" <c:if test="${discount.onlyForNewClient}">checked</c:if> />
                <label for="onlyForNewClient"><fmt:message key="discount.onlyForNew" /></label>
            </div>
            <input class="big" type="submit" value="<fmt:message key="submit" />" />
            <input class="big" type="reset" value="<fmt:message key="reset" />" />
        </form>
    </div>
</section>

<!-- Modal window -->
<jsp:include page="../template/modal.jsp" />

<!-- Footer -->
<jsp:include page="../template/footer.jsp" />
</body>
</html>
</fmt:bundle>