<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Тарифы</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/image/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>

    <script src="/static/js/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
</head>
<body>
<!-- Header -->
<header id="header">
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
</header>

<!-- Main part -->
<section class="wrapper style1 align-center">
    <div class="container">
        <header class="major">
            <h2>Тарифные планы</h2>
            <a href="/tariffForm" class="button">Создать тарифный план</a>
        </header>
        <table class="list">
            <tr>
                <th>Название</th>
                <th>Абонентская плата</th>
                <th>Скорость приема</th>
                <th>Скорость передачи</th>
                <th>Включенный трафик</th>
                <th></th>
                <th></th>
            </tr>
            <c:forEach items="${tariffPlans.data}" var="tariffPlan">
                <tr onclick="redirect('tariff')">
                    <td>${tariffPlan.name}</td>
                    <td>${tariffPlan.monthlyFee}</td>
                    <td>${tariffPlan.downSpeed}</td>
                    <td>${tariffPlan.upSpeed}</td>
                    <td>${tariffPlan.includedTraffic}</td>
                    <td><div class="icon small fa-edit"></div></td>
                    <td><div class="icon small fa-archive"></div></td>
                </tr>
            </c:forEach>
        </table>
    </div>
</section>

<!-- Footer -->
<footer id="footer">
    <div class="container">
        &copy; Internet Provider. Разработано Владиславом Черником.
    </div>
</footer>
</body>
</html>
