<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="home"><a href="/">На главную</a></div>
<div class="nav">
    <ul>
        <c:choose>
            <c:when test="${sessionScope.user != null}">
                <li>
                    <a href="#">Личный кабинет</a>
                </li>
                <li>
                    <a href="#">Кабинет сотрудника</a>
                </li>
                <li>
                    <a href="#">Сменить пароль</a>
                </li>
                <li>
                    <a href="/logout" class="button">Выйти</a>
                </li>
            </c:when>
            <c:otherwise>
                <li>
                    <a href="/loginPage" class="button">Войти</a>
                </li>
            </c:otherwise>
        </c:choose>
    </ul>
</div>
