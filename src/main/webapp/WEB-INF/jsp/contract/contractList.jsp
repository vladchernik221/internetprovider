<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Договора</title>

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
            <h2>Договора</h2>
            <a href="/contract/new" class="button">Создать договор</a>
        </header>
        <form id="contract_search" method="GET" action="/contract">
            <div class="row">
                <input type="text" name="number" placeholder="Номер договора" value="${param.number}" minlength="6" maxlength="6" required />
                <label>Номер договора</label>
            </div>
            <input class="small" type="submit" value="Найти" />
        </form>
        <c:choose>
            <c:when test="${contract != null}">
                <table class="list">
                    <tr>
                        <th>Номер договора</th>
                        <th>Абонент</th>
                    </tr>
                    <tr onclick="redirect('/contract/${contract.contractId}')">
                        <td><fmt:formatNumber type = "number" minIntegerDigits = "6" value = "${contract.contractId}" /></td>
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
                <h2 class="warn">Договор с таким номером не найден</h2>
            </c:when>
        </c:choose>
    </div>
</section>

<!-- Footer -->
<jsp:include page="../template/footer.jsp" />
</body>
</html>
