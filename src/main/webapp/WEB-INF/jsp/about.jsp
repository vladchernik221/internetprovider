<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>О компании</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
</head>
<body>
<!-- Header -->
<header id="header">
    <jsp:include page="template/header.jsp"/>
</header>

<!-- Main part -->
<section class="wrapper style2">
    <div class="container">
        <header class="major">
            <h2>О компании Internet Provider</h2>
        </header>
        <p>Сегодня наша компания предоставляет широкий спектр услуг электросвязи на базе собственной опорной
            волоконно-оптической сети</p>
        <p>Компания Internet Provider осуществляет свою деятельность в г. Минске и Минском районе и является оператором
            электросвязи полного цикла. Мы предлагаем своим клиентам эффективные решения в области связи и
            телекоммуникаций, начиная от консультаций и проектирования, и заканчивая строительством локальных
            вычислительных сетей. Абонентам предоставляется широкий выбор тарифных планов и технологий, различные
            способы оплаты и пополнения своих лицевых счетов.</p>
        <p>Мы стараемся быть ответственными перед своими клиентами и предлагать им только те решения, которые им
            необходимы здесь и сейчас. Открытость и гибкость - вот наши основные принципы работы с клиентом. Поэтому,
            обратившись к нам однажды, наши клиенты надолго сохраняют взаимовыгодные партнерские отношения.</p>
    </div>
</section>

<!-- Footer -->
<jsp:include page="template/footer.jsp"/>
</body>
</html>