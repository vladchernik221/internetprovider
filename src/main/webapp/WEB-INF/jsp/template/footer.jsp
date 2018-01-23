<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:bundle basename="pagecontent/template_content">
<footer id="footer">
    <div class="container">
        <div class="row"><div class="col-2">
            &copy; Internet Provider. <fmt:message key="footer.designed" />.
        </div><div class="col-2 align-right">
            <ul class="lang-bar">
                <li class="active">RU</li><li>EN</li>
            </ul>
        </div></div>
    </div>
</footer>
</fmt:bundle>
