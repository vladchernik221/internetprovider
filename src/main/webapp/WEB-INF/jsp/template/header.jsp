<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:bundle basename="pagecontent/template_content">
<div class="home"><a href="/"><fmt:message key="header.home" /></a></div>
<div class="nav">
    <ul>
        <c:choose>
            <c:when test="${sessionScope.user != null}">
                <c:if test="${sessionScope.user.userRole == 'CUSTOMER'}">
                    <li>
                        <a href="/contract/${user.contract.contractId}"><fmt:message key="header.contract" /></a>
                    </li>
                </c:if>
                <c:if test="${sessionScope.user.userRole == 'ADMIN' || sessionScope.user.userRole == 'SELLER'}">
                    <li>
                        <a href="/employee"><fmt:message key="header.employee" /></a>
                    </li>
                </c:if>
                <li>
                    <a href="/user/${sessionScope.user.userId}/password"><fmt:message key="header.changePassword" /></a>
                </li>
                <li>
                    <a onclick="logout()" class="button"><fmt:message key="header.logout" /></a>
                </li>
            </c:when>
            <c:otherwise>
                <li>
                    <a href="/login" class="button"><fmt:message key="header.login" /></a>
                </li>
            </c:otherwise>
        </c:choose>
    </ul>
</div>
</fmt:bundle>