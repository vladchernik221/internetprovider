<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Тариф</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet" />
    <link href="/static/css/skel.css" rel="stylesheet" />
    <link href="/static/css/style.css" rel="stylesheet" />

    <script src="/static/js/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
</head>
<body>
<!-- Header -->
<header id="header">
    <jsp:include page="header.jsp" />
</header>

<!-- Main part -->
<section class="wrapper style2">
    <div class="container">
        <header class="major">
            <h2>Создание тарифного плана</h2>
        </header>
        <form action="/tariff_plan/new" method="post">
            <div class="row">
                <input type="text" name="name" placeholder="Название" required />
                <label>Название</label>
            </div>
            <div class="row">
                <textarea name="description" placeholder="Описание" rows="6"></textarea>
                <label>Описание</label>
            </div>
            <div class="row">
                <input type="text" name="monthlyFee" placeholder="Абонентская плата" pattern="^\d*(\.\d{0,2})?$"  required />
                <label>Абонентская плата</label>
            </div>
            <div class="row"><div class="col-2">
                <input type="text" name="downSpeed" placeholder="Скорость передачи" pattern="^\d*(\.\d{0,2})?$" required />
                <label>Скорость передачи</label>
            </div><div class="col-2">
                <input type="text" name="upSpeed" placeholder="Скорость отдачи" pattern="^\d*(\.\d{0,2})?$" required />
                <label>Скорость отдачи</label>
            </div></div>
            <div class="row"><div class="col-2">
                <input type="radio" name="isLimit" value="true" id="isLimit-true" />
                <label for="isLimit-true">Лимитный</label>
            </div><div class="col-2">
                <input type="radio" name="isLimit" value="false" id="isLimit-false" checked />
                <label for="isLimit-false">Безлимитный</label>
            </div></div>
            <div class="row"><div class="col-2">
                <input type="text" name="includedTraffic" placeholder="Включенный трафик, Мб" pattern="^\d*$" />
                <label>Включенный трафик, Мб</label>
            </div><div class="col-2">
                <input type="text" name="priceOverTraffic" placeholder="Цена за Мб после превышения трафика" pattern="^\d*(\.\d{0,2})?$" />
                <label>Цена за Мб после превышения трафика</label>
            </div></div>
            <div class="row">
                <div class="selected-item">Акция 11<div class="icon small fa-remove"></div></div>
                <div class="selected-item">Акция 12<div class="icon small fa-remove"></div></div>
                <div class="selected-item">Акция 13<div class="icon small fa-remove"></div></div>
            </div>
            <div class="row">
                <div class="select-wrapper">
                    <select name="category">
                        <option value="">- Акции -</option>
                        <option value="1">Акция 1</option>
                        <option value="1">Акция 2</option>
                        <option value="1">Акция 3</option>
                        <option value="1">Акция 4</option>
                    </select>
                </div>
            </div>
            <input class="big" type="submit" value="Создать" />
            <input class="big" type="reset" value="Очистить" />
        </form>
    </div>
</section>

<!-- Footer -->
<jsp:include page="footer.jsp" />
</body>
</html>
