<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="ru_RU" scope="session"/>
<fmt:bundle basename="pagecontent/annex_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="annex.view.title" /></title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/annex.js"></script>
</head>
<body>
<!-- Header -->
<header id="header">
    <jsp:include page="../template/header.jsp"/>
</header>

<!-- Main part -->
<section class="wrapper document">
    <div class="container">
        <div class="row"><div class="align-left col-2">
            <a href="/contract/${annex.contract.contractId}/annex" class="button small"><fmt:message key="annex.view.toList" /></a>
            <a href="/contract/annex/${annex.contractAnnexId}/account" class="button small"><fmt:message key="account" /></a>
        </div><!--
         --><c:if test="${!annex.canceled}"><div class="align-right actions col-2">
                <button class="button small" onclick="cancel_annex(${annex.contractAnnexId})"><fmt:message key="annex.dissolve" /></button>
            </div></c:if><!--
     --></div>
        <c:if test="${annex.canceled}">
            <h2 class="warn"><fmt:message key="annex.dissolved" /></h2>
        </c:if>
        <h2 class="uppercase"><fmt:message key="annex" /> № 1<br /><fmt:message key="annex.description" /><br/><fmt:message key="contract.number" />:
            <span class="important">
                <fmt:formatNumber type = "number" groupingUsed="false" minIntegerDigits = "6" value = "${annex.contract.contractId}" />
            </span><br/><fmt:message key="annex.service" /></h2>
        <p><fmt:formatDate type = "date" dateStyle = "long" value = "${annex.concludeDate}" /></p>

        <ol>
            <li><h3 class="uppercase"><fmt:message key="annex.view.chapter1.title" /></h3>
                <ol>
                    <li><fmt:message key="annex.view.chapter1.point1" /></li>
                    <li><fmt:message key="annex.view.chapter1.point2" /></li>
                    <li><fmt:message key="annex.view.chapter1.point3" /></li>
                    <li><fmt:message key="annex.view.chapter1.point4" /></li>
                    <li><fmt:message key="annex.view.chapter1.point5" /></li>
                    <li><fmt:message key="annex.view.chapter1.point6" /></li>
                </ol>
            </li>
            <li><h3 class="uppercase"><fmt:message key="annex.view.chapter2.title" /></h3>
                <ol>
                    <li><fmt:message key="annex.view.chapter2.point1" /></li>
                    <li><fmt:message key="annex.view.chapter2.point2" /></li>
                    <li><fmt:message key="annex.view.chapter2.point3" /></li>
                    <li><fmt:message key="annex.view.chapter2.point4" /></li>
                </ol>
            </li>
            <li><h3 class="uppercase"><fmt:message key="annex.view.chapter3.title" /></h3>
                <ol>
                    <li><fmt:message key="annex.view.chapter3.point1" /></li>
                    <li><fmt:message key="annex.view.chapter3.point2" /> <span class="important">${annex.tariffPlan.name}</span></li>
                    <li><fmt:message key="annex.view.chapter3.point3" />: <span class="important">${annex.address}</span></li>
                </ol>
            </li>
        </ol>
        <table class="subscription">
            <tr>
                <td><fmt:message key="subscription.operator" />:</td>
                <td><fmt:message key="subscription.client" />:</td>
            </tr>
            <tr>
                <td>
                    <div></div>
                </td>
                <td>
                    <div></div>
                </td>
            </tr>
        </table>
    </div>
</section>

<!-- Footer -->
<jsp:include page="../template/footer.jsp"/>
</body>
</html>
</fmt:bundle>