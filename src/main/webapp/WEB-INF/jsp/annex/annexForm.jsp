<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="ru_RU" scope="session"/>
<fmt:bundle basename="pagecontent/annexForm_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="annex" /></title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet" />
    <link href="/static/css/skel.css" rel="stylesheet" />
    <link href="/static/css/style.css" rel="stylesheet" />

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/annex.js"></script>
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
            <h2><fmt:message key="annex.creation" /> № <fmt:formatNumber type = "number" groupingUsed="false" minIntegerDigits = "6" value = "${contractId}" /></h2>
        </header>
            <form id="annex_form" onsubmit="send_form(event)" method="POST" action="/contract/${contractId}/annex/new">
            <div class="row">
                <input type="text" name="address" placeholder="<fmt:message key="annex.address" />" required />
                <label><fmt:message key="annex.address" /></label>
            </div>
            <div class="row">
                <div class="select-wrapper">
                    <select name="tariffPlanId" required>
                        <option value="">- <fmt:message key="annex.tariffPlan" /> -</option>
                            <c:forEach items="${tariffPlans}" var="tariffPlan">
                                <option value="${tariffPlan.tariffPlanId}">${tariffPlan.name}</option>
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