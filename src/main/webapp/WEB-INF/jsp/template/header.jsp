<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="en_US" scope="session"/>
<fmt:bundle basename="pagecontent/template_content">
<div class="home"><a href="/"><fmt:message key="header.home" /></a></div>
<div class="nav">
    <ul>
        <c:choose>
            <c:when test="${sessionScope.user != null}">
                <li>
                    <a href="/client"><fmt:message key="header.client" /></a>
                </li>
                <li>
                    <a href="/employee"><fmt:message key="header.employee" /></a>
                </li>
                <li>
                    <a href="/password"><fmt:message key="header.changePassword" /></a>
                </li>
                <li>
                    <a href="/logout" class="button"><fmt:message key="header.logout" /></a>
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