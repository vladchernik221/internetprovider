<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Кабинет сотрудника</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
</head>
<body>
<!-- Header -->
<header id="header">
    <jsp:include page="template/header.jsp" />
</header>

<!-- Main part -->
<section class="wrapper style1 admin-style align-center">
    <div class="container">
        <header class="major">
            <h2>Кабинет администратора (менеджера по продажам)</h2>
            <p>Здесь Вы можете найти ссылки на ресурсы сервиса.</p>
        </header>
        <div class="row"><section class="box col-3" onclick="redirect('/tariff-plan')">
                <div class="icon medium rounded blue fa-tasks"></div>
                <h3>Тарифные планы</h3>
            </section><section class="box col-3" onclick="redirect('/service')">
                <div class="icon medium rounded green fa-wrench"></div>
                <h3>Услуги</h3>
            </section><section class="box col-3">
                <div class="icon medium rounded red fa-fire"></div>
                <h3>Акции</h3>
            </section><section class="box col-3" onclick="redirect('/contract')">
                <div class="icon medium rounded orange fa-briefcase"></div>
                <h3>Договора</h3>
            </section><section class="box col-3" onclick="redirect('/user')">
                <div class="icon medium rounded violet fa-users"></div>
                <h3>Пользователи</h3>
            </section></div>
    </div>
</section>

<!-- Footer -->
<jsp:include page="template/footer.jsp" />
</body>
</html>