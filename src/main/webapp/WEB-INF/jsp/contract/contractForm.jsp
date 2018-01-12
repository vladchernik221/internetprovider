<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Контракт</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet" />
    <link href="/static/css/skel.css" rel="stylesheet" />
    <link href="/static/css/style.css" rel="stylesheet" />

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/contractForm.js"></script>
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
            <h2>Создание контракта</h2>
        </header>
        <form id="contract_form" onsubmit="send_form(event)" method="POST" action="/contract/new">
            <div class="row"><div class="col-2">
                <input type="radio" name="clientType" value="INDIVIDUAL" id="clientType-individual" onchange="change_client_type('individual')" checked />
                <label for="clientType-individual">Физическое лицо</label>
            </div><div class="col-2">
                <input type="radio" name="clientType" value="LEGAL" id="clientType-legal" onchange="change_client_type('legal')" />
                <label for="clientType-legal">Юридическое лицо</label>
            </div></div>
            <div class="row only-for-individual">
                <input type="text" name="individualClientInformation.secondName" placeholder="Фамилия" required />
                <label>Фамилия</label>
            </div>
            <div class="row only-for-individual">
                <input type="text" name="individualClientInformation.firstName" placeholder="Имя" required />
                <label>Имя</label>
            </div>
            <div class="row only-for-individual">
                <input type="text" name="individualClientInformation.lastName" placeholder="Отчество" required />
                <label>Отчество</label>
            </div>
            <div class="row only-for-individual">
                <input type="text" name="individualClientInformation.passportUniqueIdentification" placeholder="Личный (идентификационный) номер" required />
                <label>Личный (идентификационный) номер</label>
            </div>
            <div class="row only-for-individual">
                <input type="text" name="individualClientInformation.address" placeholder="Адрес" required />
                <label>Адрес</label>
            </div>
            <div class="row only-for-individual">
                <input type="text" name="individualClientInformation.phoneNumber" placeholder="Номер телефона" required />
                <label>Номер телефона</label>
            </div>
            <div class="row only-for-legal">
                <input type="text" name="legalEntityClientInformation.name" placeholder="Название" required />
                <label>Название</label>
            </div>
            <div class="row only-for-legal">
                <input type="text" name="legalEntityClientInformation.payerAccountNumber" placeholder="Номер расчётного счета" required />
                <label>Номер расчётного счёта</label>
            </div>
            <div class="row only-for-legal">
                <input type="text" name="legalEntityClientInformation.checkingAccount" placeholder="Checking account" required />
                <label>Checking account</label>
            </div>
            <div class="row only-for-legal">
                <input type="text" name="legalEntityClientInformation.address" placeholder="Адрес" required />
                <label>Адрес</label>
            </div>
            <div class="row only-for-legal">
                <input type="text" name="legalEntityClientInformation.phoneNumber" placeholder="Номер телефона" required />
                <label>Номер телефона</label>
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
