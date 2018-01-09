<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Авторизация</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>
    <link href="/static/css/login.css" rel="stylesheet"/>

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
</head>
<body>
<!-- Header -->
<header id="header" class="transparent top">
    <div class="home"><a href="/">На главную</a></div>
</header>

<!-- Main part -->
<section class="login">
    <form action="/signin" method="post">
        <div class="row">
            <input type="text" name="login" placeholder="Логин"/>
        </div>
        <div class="row">
            <input type="password" name="password" placeholder="Пароль"/>
        </div>
        <input type="submit" value="Войти"/>
        <input type="reset" value="Очистить"/>
    </form>
</section>
</body>
</html>
