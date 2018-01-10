<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Описание услуги</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>

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
        <div class="row"><div class="align-left col-2">
                <a href="/service" class="button small">К списку</a>
            </div><div class="align-right actions col-2">
                <a href="/service/${service.serviceId}/edit" class="button small">Редактировать</a>
                <button class="button small" onclick="change_archived(${service.serviceId})">
                    <c:choose>
                        <c:when test="${!service.archived}">Поместить в архив</c:when>
                        <c:otherwise>Извлечь из архива</c:otherwise>
                    </c:choose>
                </button>
            </div></div>
        <c:if test="${service.archived}">
            <h2 class="warn">Находится в архиве</h2>
        </c:if>
        <h1>${service.name}</h1>
        <p>${service.description}</p>
        <table class="description">
            <tr>
                <th>Стоимость</th>
                <td>${service.price}</td>
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
<jsp:include page="../template/footer.jsp" />
</body>
</html>
