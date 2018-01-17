<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Акция</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet" />
    <link href="/static/css/skel.css" rel="stylesheet" />
    <link href="/static/css/style.css" rel="stylesheet" />

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/discount.js"></script>
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
        <c:choose>
            <c:when test="${discount == null}">
                <h2>Создание акции</h2>
            </c:when>
            <c:otherwise>
                <h2>Редактирование акции</h2>
            </c:otherwise>
        </c:choose>
        </header>
        <c:choose>
            <c:when test="${discount == null}">
                <form id="discount_form" onsubmit="send_form(event)" method="POST" action="/discount/new">
            </c:when>
            <c:otherwise>
                <form id="discount_form" onsubmit="send_form(event)" method="POST" action="/discount/${discount.discountId}">
            </c:otherwise>
        </c:choose>
            <div class="row">
                <input type="text" name="name" placeholder="Название" value="${discount.name}" required />
                <label>Название</label>
            </div>
            <div class="row">
                <textarea name="description" placeholder="Описание" rows="6">${discount.description}</textarea>
                <label>Описание</label>
            </div>
            <div class="row">
                <input type="text" name="amount" placeholder="Рамер скидки" pattern="^(\d{1,2}|100)$"  value="${discount.amount}" required />
                <label>Рамер скидки</label>
            </div>
            <div class="row"><div class="col-2">
                <input type="date" name="startDate" placeholder="Начало акции" value="${discount.startDate}" required />
                <label>Начало акции</label>
            </div><div class="col-2">
                <input type="date" name="endDate" placeholder="Конец акции" value="${discount.endDate}" required />
                <label>Конец акции</label>
            </div></div>
            <div class="row">
                <input type="checkbox" name="onlyForNewClient" value="true" id="onlyForNewClient" <c:if test="${discount.onlyForNewClient}">checked</c:if> />
                <label for="onlyForNewClient">Только для новых клиентов</label>
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
