<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Описание тарифа</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/image/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>

    <script src="/static/js/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
</head>
<body>
<!-- Header -->
<header id="header" class="transparent top">
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
<section class="wrapper style2">
    <div class="container">
        <div class="row"><div class="align-left col-2">
                <a href="/tariff_plan" class="button small">К списку</a>
            </div><div class="align-right actions col-2">
                <a href="#" class="button small">Редактировать</a>
                <c:choose>
                    <c:when test="${!tariffPlan.archived}">
                        <button class="button small">Поместить в архив</button>
                    </c:when>
                    <c:otherwise>
                        <button class="button small">Извлечь из архива</button>
                    </c:otherwise>
                </c:choose>
            </div></div>
        <c:if test="${tariffPlan.archived}">
            <h2 class="warn">Находится в архиве</h2>
        </c:if>
        <h1>${tariffPlan.name}</h1>
        <p>${tariffPlan.description}</p>
        <table class="description">
            <tr>
                <th>Абонентская плата</th>
                <td>${tariffPlan.monthlyFee}</td>
            </tr>
            <tr>
                <th>Скорость передачи</th>
                <td>${tariffPlan.downSpeed}</td>
            </tr>
            <tr>
                <th>Скорость отдачи</th>
                <td>${tariffPlan.upSpeed}</td>
            </tr>
            <tr>
                <th>Включенный трафик, Мб</th>
                <td>${tariffPlan.includedTraffic}</td>
            </tr>
            <tr>
                <th>Цена за Мб после превышения трафика</th>
                <td>${tariffPlan.priceOverTraffic}</td>
            </tr>
        </table>
        <div class="discount slider">
            <ul>
                <li>
                    <h2>Название акции 1</h2>
                    <p>Описание акции, описание</p>
                </li>
                <li>
                    <h2>Название акции 2</h2>
                    <p>Описание акции, описание</p>
                </li>
                <li>
                    <h2>Название акции 3</h2>
                    <p>Описание акции, описание</p>
                </li>
                <li>
                    <h2>Название акции 4</h2>
                    <p>Описание акции, описание</p>
                </li>
                <li>
                    <h2>Название акции 5</h2>
                    <p>Описание акции, описание</p>
                </li>
            </ul>
        </div>
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
