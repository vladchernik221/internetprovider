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

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/tariffForm.js"></script>
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
        <c:choose>
            <c:when test="${tariffPlan == null}">
                <h2>Создание тарифного плана</h2>
            </c:when>
            <c:otherwise>
                <h2>Редактирование тарифного плана</h2>
            </c:otherwise>
        </c:choose>
        </header>
        <c:choose>
            <c:when test="${tariffPlan == null}">
                <form id="tariff_plan_form" onsubmit="send(event)" method="POST" action="/tariff_plan/new">
            </c:when>
            <c:otherwise>
                <form id="tariff_plan_form" onsubmit="send(event)" method="PUT" action="/tariff_plan/${tariffPlan.tariffPlanId}">
            </c:otherwise>
        </c:choose>
            <div class="row">
                <input type="text" name="name" placeholder="Название" value="${tariffPlan.name}" required />
                <label>Название</label>
            </div>
            <div class="row">
                <textarea name="description" placeholder="Описание" value="${tariffPlan.description}" rows="6"></textarea>
                <label>Описание</label>
            </div>
            <div class="row">
                <input type="text" name="monthlyFee" placeholder="Абонентская плата" pattern="^\d*(\.\d{0,2})?$"  value="${tariffPlan.monthlyFee}" required />
                <label>Абонентская плата</label>
            </div>
            <div class="row"><div class="col-2">
                <input type="text" name="downSpeed" placeholder="Скорость передачи" pattern="^\d*$" value="${tariffPlan.downSpeed}" required />
                <label>Скорость передачи</label>
            </div><div class="col-2">
                <input type="text" name="upSpeed" placeholder="Скорость отдачи" pattern="^\d*$" value="${tariffPlan.upSpeed}" required />
                <label>Скорость отдачи</label>
            </div></div>
            <div class="row"><div class="col-2">
                <input type="radio" name="isLimit" value="true" id="isLimit-true" onchange="change_is_limit(true)" <c:if test="${tariffPlan.includedTraffic != null}">checked</c:if> />
                <label for="isLimit-true">Лимитный</label>
            </div><div class="col-2">
                <input type="radio" name="isLimit" value="false" id="isLimit-false" onchange="change_is_limit(false)" <c:if test="${tariffPlan.includedTraffic == null}">checked</c:if> />
                <label for="isLimit-false">Безлимитный</label>
            </div></div>
            <div class="row only-for-limit"><div class="col-2">
                <input type="text" name="includedTraffic" placeholder="Включенный трафик, Мб" pattern="^\d*$" value="${tariffPlan.includedTraffic}" />
                <label>Включенный трафик, Мб</label>
            </div><div class="col-2">
                <input type="text" name="priceOverTraffic" placeholder="Цена за Мб после превышения трафика" pattern="^\d*(\.\d{0,2})?$" value="${tariffPlan.priceOverTraffic}" />
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
            <input class="big" type="submit" value="Сохранить" />
            <input class="big" type="reset" value="Очистить" />
        </form>
    </div>
</section>

<!-- Modal window -->
<jsp:include page="modal.jsp" />

<!-- Footer -->
<jsp:include page="footer.jsp" />
</body>
</html>
