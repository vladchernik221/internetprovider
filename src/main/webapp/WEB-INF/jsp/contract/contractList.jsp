<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:bundle basename="pagecontent/contractList_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="contracts" /></title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
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
            <h2><fmt:message key="contracts" /></h2>
            <a href="/contract/new" class="button"><fmt:message key="contract.create" /></a>
        </header>
        <form id="contract_search" method="GET" action="/contract">
            <div class="row">
                <input type="text" name="number" placeholder="<fmt:message key="contract.number" />" value="${param.number}" minlength="6" maxlength="6" required />
                <label><fmt:message key="contract.number" /></label>
            </div>
            <input class="small" type="submit" value="<fmt:message key="search" />" />
        </form>
        <c:choose>
            <c:when test="${contract != null}">
                <table class="list">
                    <tr>
                        <th><fmt:message key="contract.number" /></th>
                        <th><fmt:message key="contract.client" /></th>
                    </tr>
                    <tr onclick="redirect('/contract/${contract.contractId}')">
                        <td><fmt:formatNumber type = "number" groupingUsed="false" minIntegerDigits = "6" value = "${contract.contractId}" /></td>
                        <c:choose>
                            <c:when test="${contract.clientType == 'INDIVIDUAL'}">
                                <td>${contract.individualClientInformation.secondName} ${contract.individualClientInformation.firstName} ${contract.individualClientInformation.lastName}</td>
                            </c:when>
                            <c:otherwise>
                                <td>${contract.legalEntityClientInformation.name}</td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </table>
            </c:when>
            <c:when test="${param.number != '' && param.number != null}">
                <h2 class="warn"><fmt:message key="contract.notFound" /></h2>
            </c:when>
        </c:choose>
    </div>
</section>

<!-- Footer -->
<jsp:include page="../template/footer.jsp" />
</body>
</html>
</fmt:bundle>