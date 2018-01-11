<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Услуга</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet" />
    <link href="/static/css/skel.css" rel="stylesheet" />
    <link href="/static/css/style.css" rel="stylesheet" />

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/service.js"></script>
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
            <c:when test="${service == null}">
                <h2>Создание услуги</h2>
            </c:when>
            <c:otherwise>
                <h2>Редактирование услуги</h2>
            </c:otherwise>
        </c:choose>
        </header>
        <c:choose>
            <c:when test="${service == null}">
                <form id="service_form" onsubmit="send_form(event)" method="POST" action="/service/new">
            </c:when>
            <c:otherwise>
                <form id="service_form" onsubmit="send_form(event)" method="POST" action="/service/${service.serviceId}">
            </c:otherwise>
        </c:choose>
            <div class="row">
                <input type="text" name="name" placeholder="Название" value="${service.name}" required />
                <label>Название</label>
            </div>
            <div class="row">
                <textarea name="description" placeholder="Описание" rows="6">${service.description}</textarea>
                <label>Описание</label>
            </div>
            <div class="row">
                <input type="text" name="price" placeholder="Стоимость" pattern="^\d*(\.\d{0,2})?$"  value="${service.price}" required />
                <label>Стоимость</label>
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
