<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="en_US" scope="session"/>
<fmt:bundle basename="pagecontent/contractForm_content">
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="contract" /></title>

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
            <h2><fmt:message key="contract.creation" /></h2>
        </header>
        <form id="contract_form" onsubmit="send_form(event)" method="POST" action="/contract/new">
            <div class="row"><div class="col-2">
                <input type="radio" name="clientType" value="INDIVIDUAL" id="clientType-individual" onchange="change_client_type('individual')" checked />
                <label for="clientType-individual"><fmt:message key="contract.individual" /></label>
            </div><div class="col-2">
                <input type="radio" name="clientType" value="LEGAL" id="clientType-legal" onchange="change_client_type('legal')" />
                <label for="clientType-legal"><fmt:message key="contract.legal" /></label>
            </div></div>
            <div class="row only-for-individual">
                <input type="text" name="individual.passportUniqueIdentification" placeholder="<fmt:message key="contract.password" />" required />
                <label><fmt:message key="contract.password" /></label>
            </div>
            <div class="row only-for-legal">
                <input type="text" name="legal.payerAccountNumber" placeholder="<fmt:message key="contract.payerAccountNumber" />" required />
                <label><fmt:message key="contract.payerAccountNumber" /></label>
            </div>
            <div class="row">
                <button class="button small" onclick="fill_form(event)"><fmt:message key="autocomplete" /></button>
            </div>
            <div class="row only-for-individual">
                <input type="text" name="individual.secondName" placeholder="<fmt:message key="contract.secondName" />" required />
                <label><fmt:message key="contract.secondName" /></label>
            </div>
            <div class="row only-for-individual">
                <input type="text" name="individual.firstName" placeholder="<fmt:message key="contract.firstName" />" required />
                <label><fmt:message key="contract.firstName" /></label>
            </div>
            <div class="row only-for-individual">
                <input type="text" name="individual.lastName" placeholder="<fmt:message key="contract.lastName" />" required />
                <label><fmt:message key="contract.lastName" /></label>
            </div>
            <div class="row only-for-individual">
                <input type="text" name="individual.address" placeholder="<fmt:message key="contract.address" />" required />
                <label><fmt:message key="contract.address" /></label>
            </div>
            <div class="row only-for-individual">
                <input type="text" name="individual.phoneNumber" placeholder="<fmt:message key="contract.phoneNumber" />: (00)000-00-00" pattern="^\(\d{2}\)\d{3}-\d{2}-\d{2}$" required />
                <label><fmt:message key="contract.phoneNumber" />: (00)000-00-00</label>
            </div>
            <div class="row only-for-legal">
                <input type="text" name="legal.name" placeholder="<fmt:message key="contract.legalName" />" required />
                <label><fmt:message key="contract.legalName" /></label>
            </div>
            <div class="row only-for-legal">
                <input type="text" name="legal.checkingAccount" placeholder="<fmt:message key="contract.checkingAccount" />" required />
                <label><fmt:message key="contract.checkingAccount" /></label>
            </div>
            <div class="row only-for-legal">
                <input type="text" name="legal.address" placeholder="<fmt:message key="contract.address" />" required />
                <label><fmt:message key="contract.address" /></label>
            </div>
            <div class="row only-for-legal">
                <input type="text" name="legal.phoneNumber" placeholder="<fmt:message key="contract.phoneNumber" />: (00)000-00-00" pattern="^\(\d{2}\)\d{3}-\d{2}-\d{2}$" required />
                <label><fmt:message key="contract.phoneNumber" />: (00)000-00-00</label>
            </div>
            <input class="big" type="submit" value="<fmt:message key="submit" />" />
            <input class="big" type="reset" value="<fmt:message key="reset" />" />
        </form>
    </div>
</section>

<!-- Modal window -->
<jsp:include page="../template/modal.jsp" />

<!-- Footer -->
<jsp:include page="../template/footer.jsp" />
</body>
</html>
</fmt:bundle>
