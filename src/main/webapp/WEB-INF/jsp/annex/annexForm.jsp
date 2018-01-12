<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Приложение</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet" />
    <link href="/static/css/skel.css" rel="stylesheet" />
    <link href="/static/css/style.css" rel="stylesheet" />

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
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
            <h2>Создание приложения к контракту № 300500</h2>
        </header>
            <form id="service_form" onsubmit="send_form(event)" method="POST" action="/contract/300/annex/new">
            <div class="row">
                <input type="text" name="address" placeholder="Адрес подключения" required />
                <label>Адрес подключения</label>
            </div>
            <div class="row">
                <div class="select-wrapper">
                    <select name="tariffPlan.tariffPlanId" required>
                        <option value="">- Тарифный план -</option>
                        <option value="1">Тарифный план 1</option>
                        <option value="1">Тарифный план 2</option>
                        <option value="1">Тарифный план 3</option>
                        <option value="1">Тарифный план 4</option>
                    </select>
                </div>
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
