<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="ru_RU" scope="session"/>
<fmt:bundle basename="pagecontent/tariffForm_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="tariff" /></title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet" />
    <link href="/static/css/skel.css" rel="stylesheet" />
    <link href="/static/css/style.css" rel="stylesheet" />

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/tariffForm.js"></script>
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
            <c:when test="${tariffPlan == null}">
                <h2><fmt:message key="tariff.creation" /></h2>
            </c:when>
            <c:otherwise>
                <h2><fmt:message key="tariff.edition" /></h2>
            </c:otherwise>
        </c:choose>
        </header>
        <c:choose>
            <c:when test="${tariffPlan == null}">
                <form id="tariff_plan_form" onsubmit="send(event)" method="POST" action="/tariff-plan/new">
            </c:when>
            <c:otherwise>
                <form id="tariff_plan_form" onsubmit="send(event)" method="POST" action="/tariff-plan/${tariffPlan.tariffPlanId}">
            </c:otherwise>
        </c:choose>
            <div class="row">
                <input type="text" name="name" placeholder="<fmt:message key="tariff.name" />" value="${tariffPlan.name}" required />
                <label><fmt:message key="tariff.name" /></label>
            </div>
            <div class="row">
                <textarea name="description" placeholder="<fmt:message key="tariff.description" />" rows="6">${tariffPlan.description}</textarea>
                <label><fmt:message key="tariff.description" /></label>
            </div>
            <div class="row">
                <input type="text" name="monthlyFee" placeholder="<fmt:message key="tariff.monthlyFee" />" pattern="^\d*(\.\d{0,2})?$"  value="${tariffPlan.monthlyFee}" required />
                <label><fmt:message key="tariff.monthlyFee" /></label>
            </div>
            <div class="row"><div class="col-2">
                <input type="text" name="downSpeed" placeholder="<fmt:message key="tariff.downSpeed" />" pattern="^\d*$" value="${tariffPlan.downSpeed}" required />
                <label><fmt:message key="tariff.downSpeed" /></label>
            </div><div class="col-2">
                <input type="text" name="upSpeed" placeholder="<fmt:message key="tariff.upSpeed" />" pattern="^\d*$" value="${tariffPlan.upSpeed}" required />
                <label><fmt:message key="tariff.upSpeed" /></label>
            </div></div>
            <div class="row"><div class="col-2">
                <input type="radio" name="isLimit" value="true" id="isLimit-true" onchange="change_is_limit(true)" <c:if test="${tariffPlan.includedTraffic != null}">checked</c:if> />
                <label for="isLimit-true"><fmt:message key="tariff.limited" /></label>
            </div><div class="col-2">
                <input type="radio" name="isLimit" value="false" id="isLimit-false" onchange="change_is_limit(false)" <c:if test="${tariffPlan.includedTraffic == null}">checked</c:if> />
                <label for="isLimit-false"><fmt:message key="tariff.unlimited" /></label>
            </div></div>
            <div class="row only-for-limit"><div class="col-2">
                <input type="text" name="includedTraffic" placeholder="<fmt:message key="tariff.includedTraffic" />" pattern="^\d*$" value="${tariffPlan.includedTraffic}" />
                <label><fmt:message key="tariff.includedTraffic" /></label>
            </div><div class="col-2">
                <input type="text" name="priceOverTraffic" placeholder="<fmt:message key="tariff.priceOverTraffic" />" pattern="^\d*(\.\d{0,2})?$" value="${tariffPlan.priceOverTraffic}" />
                <label><fmt:message key="tariff.priceOverTraffic" /></label>
            </div></div>
            <div class="row selected-discounts"></div>
            <div class="row">
                <div class="select-wrapper">
                    <select id="discount" onchange="select_discount(this)">
                        <option value="">-- <fmt:message key="tariff.discounts" /> --</option>
                        <c:forEach items="${discounts}" var="discount">
                            <option value="${discount.discountId}">${discount.name}</option>
                        </c:forEach>
                    </select>
                </div>
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