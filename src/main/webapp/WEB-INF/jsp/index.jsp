<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Internet Provider</title>

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

<!-- Banner -->
<section id="banner">
    <h2>Internet Provider</h2>
    <p>Добро пожаловать в информационный центр компании "Internet Provider"</p>
    <a href="#" class="button big">Информация о компании</a>
</section>

<!-- Main part -->
<section class="wrapper style1 align-center">
    <div class="container">
        <header class="major">
            <h2>Информация для клиентов</h2>
            <p>Полезные сведения о тарфиных планах, предоставляемых услугах и акционных предложениях.</p>
        </header>
        <div class="row"><section class="box col-3" onclick="redirect('/tariff_plan')">
                <div class="icon big rounded blue fa-tasks"></div>
                <h3>Тарифные планы</h3>
                <p>В данном разделе Вы сможете найти список тарифных планов, предоставляемых нашей компанией, с
                    подробным их описанием.</p>
            </section><section class="box col-3">
                <div class="icon big rounded green fa-wrench"></div>
                <h3>Услуги</h3>
                <p>Здесь приведен список услуг по подключению, настройке и т.д., прдоставляемых компанией своим
                    клиентам.</p>
            </section><section class="box col-3">
                <div class="icon big rounded red fa-fire"></div>
                <h3>Акции</h3>
                <p>Наша компания предоставляет скидки своим клиентам. В данном разделе Вы можете ознакомится с условиями
                    предоставляемых скидок.</p>
            </section></div>
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