<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Пользователь</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet" />
    <link href="/static/css/skel.css" rel="stylesheet" />
    <link href="/static/css/style.css" rel="stylesheet" />

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/user.js"></script>
</head>
<body>
<!-- Header -->
<header id="header">
    <jsp:include page="../template/header.jsp" />
</header>

<!-- Main part -->
<section class="wrapper style2">
    <div class="container">
        <header class="major">
            <h2>Создание учётной записи пользователя</h2>
        </header>
        <form id="user_form" onsubmit="send_form(event)" method="POST" action="/user/new">
            <div class="row">
                <input type="text" name="login" placeholder="Логин" required />
                <label>Логин</label>
            </div>
            <div class="row">
                <input type="password" name="password" placeholder="Пароль" pattern="^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)\w{6,}$" required />
                <label>Пароль</label>
            </div>
            <div class="row"><div class="col-2">
                <input type="radio" name="userRole" value="ADMIN" id="userRole-admin" checked />
                <label for="userRole-admin">Администратор</label>
            </div><div class="col-2">
                <input type="radio" name="userRole" value="SELLER" id="userRole-seller" />
                <label for="userRole-seller">Менеджер по продажам</label>
            </div></div>
            <input class="big" type="submit" value="Сохранить" />
            <input class="big" type="reset" value="Очистить" />
        </form>
    </div>
</section>

<!-- Modal window -->
<jsp:include page="../template/modal.jsp" />

<!-- Footer -->
<jsp:include page="../template/footer.jsp" />
</body>
</html>
