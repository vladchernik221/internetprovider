<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:bundle basename="pagecontent/contract_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="contract.view.title" /></title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/contract.js"></script>
</head>
<body>
<!-- Header -->
<header id="header">
    <jsp:include page="../template/header.jsp"/>
</header>

<!-- Main part -->
<section class="wrapper document">
    <div class="container">
        <div class="row">
            <div class="align-left col-2">
                <a href="/contract" class="button small"><fmt:message key="contract.view.tolist" /></a>
                <a href="/contract/${contract.contractId}/annex" class="button small"><fmt:message key="annex.list" /></a>
            </div><c:if test="${!contract.dissolved}"><!--
            --><div class="align-right actions col-2">
                <a href="/contract/${contract.contractId}/annex/new" class="button small"><fmt:message key="annex.add" /></a>
                <button class="button small" onclick="dissolve(${contract.contractId})"><fmt:message key="contract.dissolve" /></button>
            </div><!--
        --></c:if></div>
        <c:if test="${contract.dissolved}">
            <h2 class="warn"><fmt:message key="contract.dissolved" /> <fmt:formatDate type="date" dateStyle="long" value="${contract.dissolveDate}"/></h2>
        </c:if>
        <h2 class="uppercase"><fmt:message key="contract" /> № <span class="important">
            <fmt:formatNumber type="number" groupingUsed="false" minIntegerDigits="6" value="${contract.contractId}"/></span><br/><fmt:message key="contract.view.for" /></h2>
        <p><fmt:formatDate type="date" dateStyle="long" value="${contract.concludeDate}"/></p>
        <p><fmt:message key="contract.view.introduction.start" /> <fmt:message key="client" />:
            <c:choose>
                <c:when test="${contract.clientType == 'INDIVIDUAL'}">
                    <fmt:message key="client.name.abbr" /> <span class="important">${contract.individualClientInformation.secondName} ${contract.individualClientInformation.firstName} ${contract.individualClientInformation.lastName}</span>,
                    <fmt:message key="client.passport" /> <span class="important">${contract.individualClientInformation.passportUniqueIdentification}</span>,
                    <fmt:message key="client.register" />: <span class="important">${contract.individualClientInformation.address}</span>,
                </c:when>
                <c:otherwise>
                    <fmt:message key="client.legal" /> <span class="important">"${contract.legalEntityClientInformation.name}"</span>,
                    <fmt:message key="client.register" />: <span class="important">${contract.legalEntityClientInformation.address}</span>,
                </c:otherwise>
            </c:choose>
            <fmt:message key="contract.view.introduction.end" />:</p>

        <ol>
            <li><h3 class="uppercase"><fmt:message key="contract.view.chapter1.title" /></h3>
                <ol>
                    <li><fmt:message key="contract.view.chapter1.point1" /></li>
                    <li><fmt:message key="contract.view.chapter1.point2" /></li>
                    <li><fmt:message key="contract.view.chapter1.point3" /></li>
                </ol>
            </li>
            <li><h3 class="uppercase"><fmt:message key="contract.view.chapter2.title" /></h3>
                <ol>
                    <li><h4><fmt:message key="contract.view.chapter2.point1.title" /></h4>
                        <ol>
                            <li><fmt:message key="contract.view.chapter2.point1.point1" /></li>
                            <li><fmt:message key="contract.view.chapter2.point1.point2" /></li>
                            <li><fmt:message key="contract.view.chapter2.point1.point3" /></li>
                        </ol>
                    </li>
                    <li><h4><fmt:message key="contract.view.chapter2.point2.title" /></h4>
                        <ol>
                            <li><fmt:message key="contract.view.chapter2.point2.point1" /></li>
                            <li><fmt:message key="contract.view.chapter2.point2.point2" /></li>
                            <li><fmt:message key="contract.view.chapter2.point2.point3" /></li>
                            <li><fmt:message key="contract.view.chapter2.point2.point4" /></li>
                        </ol>
                    </li>
                    <li><h4><fmt:message key="contract.view.chapter2.point3.title" /></h4>
                        <ol>
                            <li><fmt:message key="contract.view.chapter2.point3.point1" /></li>
                            <li><fmt:message key="contract.view.chapter2.point3.point2" /></li>
                            <li><fmt:message key="contract.view.chapter2.point3.point3" /></li>
                            <li><fmt:message key="contract.view.chapter2.point3.point4" /></li>
                        </ol>
                    </li>
                    <li><h4><fmt:message key="contract.view.chapter2.point4.title" /></h4>
                        <ol>
                            <li><fmt:message key="contract.view.chapter2.point4.point1" /></li>
                            <li><fmt:message key="contract.view.chapter2.point4.point2" /></li>
                        </ol>
                    </li>
                </ol>
            </li>
            <li><h3 class="uppercase"><fmt:message key="contract.view.chapter3.title" /></h3>
                <ol>
                    <li><fmt:message key="contract.view.chapter3.point1" /></li>
                    <li><fmt:message key="contract.view.chapter3.point2" /></li>
                    <li><fmt:message key="contract.view.chapter3.point3" /></li>
                </ol>
            </li>
            <li><h3 class="uppercase"><fmt:message key="contract.view.chapter4.title" /></h3>
                <ol>
                    <li><fmt:message key="contract.view.chapter4.point1" /></li>
                </ol>
            </li>
            <li><h3 class="uppercase"><fmt:message key="contract.view.chapter5.title" /></h3>
                <ol>
                    <li><fmt:message key="contract.view.chapter5.point1" /></li>
                    <li><fmt:message key="contract.view.chapter5.point2" /></li>
                    <li><fmt:message key="contract.view.chapter5.point3" /></li>
                </ol>
            </li>
            <li><h3 class="uppercase"><fmt:message key="contract.view.chapter6.title" /></h3>
                <ol>
                    <li><fmt:message key="contract.view.chapter6.point1" /></li>
                    <li><fmt:message key="contract.view.chapter6.point2" /></li>
                    <li><fmt:message key="contract.view.chapter6.point3" /></li>
                    <li><fmt:message key="contract.view.chapter6.point4" /></li>
                    <li><fmt:message key="contract.view.chapter6.point5" /></li>
                </ol>
            </li>
            <li><h3 class="uppercase"><fmt:message key="contract.view.chapter7.title" /></h3>
                <ol>
                    <li><fmt:message key="contract.view.chapter7.point1" /></li>
                </ol>
            </li>
            <li><h3 class="uppercase"><fmt:message key="contract.view.chapter8.title" /></h3>
                <ol>
                    <li><fmt:message key="contract.view.chapter8.point1" /></li>
                    <li><fmt:message key="contract.view.chapter8.point2" /></li>
                </ol>
            </li>
            <li><h3 class="uppercase"><fmt:message key="contract.view.chapter9.title" /></h3>
                <ol>
                    <li><fmt:message key="contract.view.chapter9.point1" /></li>
                    <li><fmt:message key="contract.view.chapter9.point2" /></li>
                    <li><fmt:message key="contract.view.chapter9.point3" /></li>
                </ol>
            </li>
            <li><h3 class="uppercase"><fmt:message key="contract.view.chapter10.title" /></h3>
                <table class="description">
                    <tr>
                        <th><fmt:message key="operator" /></th>
                        <th><fmt:message key="client" /></th>
                    </tr>
                    <tr>
                        <td><fmt:message key="operator.name" /></td>
                        <c:choose>
                            <c:when test="${contract.clientType == 'INDIVIDUAL'}">
                                <td><fmt:message key="client.name.abbr" />: <span class="important">${contract.individualClientInformation.secondName} ${contract.individualClientInformation.firstName} ${contract.individualClientInformation.lastName}</span>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td><fmt:message key="client.legal.name" />: <span class="important">${contract.legalEntityClientInformation.name}</span></td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                    <tr>
                        <td><fmt:message key="operator.address.legal" /></td>
                        <td><fmt:message key="client.address" />: <span class="important">
                            <c:choose>
                                <c:when test="${contract.clientType == 'INDIVIDUAL'}">
                                    ${contract.individualClientInformation.address}
                                </c:when>
                                <c:otherwise>
                                    ${contract.legalEntityClientInformation.address}
                                </c:otherwise>
                            </c:choose>
                        </span></td>
                    </tr>
                    <tr>
                        <td><fmt:message key="operator.address.real" /></td>
                        <td><fmt:message key="phone" />: <span class="important">
                            <c:choose>
                                <c:when test="${contract.clientType == 'INDIVIDUAL'}">
                                    ${contract.individualClientInformation.phoneNumber}
                                </c:when>
                                <c:otherwise>
                                    ${contract.legalEntityClientInformation.phoneNumber}
                                </c:otherwise>
                            </c:choose>
                        </span></td>
                    </tr>
                    <tr>
                        <td><fmt:message key="phone" />: (111)111-11-11</td>
                        <td>
                            <c:if test="${contract.clientType == 'LEGAL'}">
                                <fmt:message key="payerAccountNumber" />: <span class="important">${contract.legalEntityClientInformation.payerAccountNumber}</span>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td><fmt:message key="payerAccountNumber" />: 1111111111</td>
                        <td>
                            <c:if test="${contract.clientType == 'LEGAL'}">
                                <fmt:message key="checkingAccount" />: <span class="important">${contract.legalEntityClientInformation.checkingAccount}</span>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td><fmt:message key="operator.bank" /></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td><fmt:message key="checkingAccount" />: 111111111111111111</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td><fmt:message key="operator.director" /></td>
                        <td></td>
                    </tr>
                </table>
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
            </li>
        </ol>
    </div>
</section>

<!-- Footer -->
<jsp:include page="../template/footer.jsp"/>
</body>
</html>
</fmt:bundle>