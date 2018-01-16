<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Изменение пароля</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet" />
    <link href="/static/css/skel.css" rel="stylesheet" />
    <link href="/static/css/style.css" rel="stylesheet" />

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/changePassword.js"></script>
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
            <h2>Изменение пароля</h2>
        </header>
        <form id="password_form" onsubmit="validate_form(event)" method="POST" action="/password">
        <div class="row">
            <input type="text" name="oldPassword" placeholder="Старый пароль" required />
            <label>Старый пароль</label>
        </div>
        <div class="row">
            <input type="text" name="newPassword" placeholder="Новый пароль" pattern="^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)\w{6,}$" required />
            <label>Новый пароль</label>
        </div>
        <div class="row">
            <input type="text" name="confirmNewPassword" placeholder="Подтверждение нового пароля" pattern="^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)\w{6,}$" required />
            <label>Подтверждение нового пароля</label>
        </div>
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