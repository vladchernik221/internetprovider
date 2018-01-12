<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="home"><a href="/">На главную</a></div>
<div class="nav">
    <ul>
        <c:choose>
            <c:when test="${sessionScope.user != null}">
                <li>
                    <a href="/client">Личный кабинет</a>
                </li>
                <li>
                    <a href="/employee">Кабинет сотрудника</a>
                </li>
                <li>
                    <a href="/password">Сменить пароль</a>
                </li>
                <li>
                    <a href="/logout" class="button">Выйти</a>
                </li>
            </c:when>
            <c:otherwise>
                <li>
                    <a href="/login" class="button">Войти</a>
                </li>
            </c:otherwise>
        </c:choose>
    </ul>
</div>
