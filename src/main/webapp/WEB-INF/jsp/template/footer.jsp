<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:bundle basename="pagecontent/template_content">
<footer id="footer">
    <div class="container">
        <div class="row"><div class="col-2">
            &copy; Internet Provider. <fmt:message key="footer.designed" />.
        </div><div class="col-2 align-right">
            <ul class="lang-bar">
                <c:choose>
                <c:when test="${sessionScope.locale == 'ru_RU'}">
                    <li class="active" onclick="change_lang('ru', 'RU')">RU</li><li onclick="change_lang('en', 'US')">EN</li>
                </c:when>
                <c:otherwise>
                    <li onclick="change_lang('ru', 'RU')">RU</li><li class="active" onclick="change_lang('en', 'US')">EN</li>
                </c:otherwise>
                </c:choose>
            </ul>
        </div></div>
    </div>
</footer>
</fmt:bundle>
