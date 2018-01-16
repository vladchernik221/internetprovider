<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Описание приложения</title>

    <link type="image/x-icon" rel="shortcut icon" href="/static/images/meow.ico"/>

    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/skel.css" rel="stylesheet"/>
    <link href="/static/css/style.css" rel="stylesheet"/>

    <script src="/static/js/lib/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
    <script src="/static/js/annex.js"></script>
</head>
<body>
<!-- Header -->
<header id="header">
    <jsp:include page="../template/header.jsp"/>
</header>

<!-- Main part -->
<section class="wrapper document">
    <div class="container">
        <div class="row"><div class="align-left col-2">
            <a href="/contract/${annex.contract.contractId}/annex" class="button small">К списку приложений</a>
            <a href="#" class="button small">Счёт</a>
        </div><!--
         --><c:if test="${!annex.canceled}"><div class="align-right actions col-2">
                <button class="button small" onclick="cancel_annex(${annex.contractAnnexId})">Расторгнуть</button>
            </div></c:if><!--
     --></div>
        <c:if test="${annex.canceled}">
            <h2 class="warn">Расторгнуто</h2>
        </c:if>
        <h2 class="uppercase">Приложение № 1<br />к договору на оказание услуг передачи данных<br/>номер договора:
            <span class="important">
                <fmt:formatNumber type = "number" minIntegerDigits = "6" value = "${annex.contract.contractId}" />
            </span><br/>Услуга "Доступ в Интернет"</h2>
        <p><fmt:formatDate type = "date" dateStyle = "long" value = "${annex.concludeDate}" /></p>

        <ol>
            <li><h3 class="uppercase">Описание услуги</h3>
                <ol>
                    <li>Оператор связи предоставляет Абоненту Услугу «Доступ в Интернет» - предоставление постоянного
                        доступа посредством технологии Ethernet.
                    </li>
                    <li>Услуга предоставляется с использованием технических средств телематических служб, находящихся в
                        зоне ответственности Оператора связи. Каналы связи между оборудованием Оператора связи и
                        абонентским оборудованием организуются Оператором связи по технологии Ethernet с использованием
                        волоконно-оптической сети передачи данных Оператора.
                    </li>
                    <li>Услуга предусматривает предоставление локального (внутреннего) IP-адреса.</li>
                    <li>Скорость передачи данных на организуемом канале ограничивается Оператором связи на станционном
                        оборудовании в соответствии с условиями тарифного плана. Оператор связи не гарантирует
                        неизменность скорости обмена данными на организуемом канале в течение всего периода
                        предоставления Услуги Абоненту.
                    </li>
                    <li>Услуга может использоваться для доступа исключительно посредством сетей Оператора связи к
                        информационным ресурсам Оператора и к сети Интернет, в соответствии с условиями выбранного
                        тарифного плана. Услуга доступа в Интернет не предназначена для обмена речевой и визуальной
                        информацией в режиме реального времени.
                    </li>
                    <li>Выделенные Оператором связи Абоненту для получения Услуги ресурсы (в том числе, но не
                        ограничиваясь, учетные записи на оборудовании Оператора связи, IP-адреса) могут быть
                        использованы исключительно способом, предусмотренным настоящим Договором и Приложениями к нему и
                        не могут быть использованы в отрыве от Услуги.
                    </li>
                </ol>
            </li>
            <li><h3 class="uppercase">Порядок предоставления Услуги "Доступ в Интернет"</h3>
                <ol>
                    <li>По заявке Абонента, Оператор связи проводит работы по проверке возможности подключения Абонента
                        к Услуге и извещает Абонента о положительном или отрицательном результатах проверки.
                    </li>
                    <li>Оплата Услуги производится по условиям выбранного тарифного плана.</li>
                    <li>Подключение Абонента к Услуге с использованием оборудования Абонента возможно только в том
                        случае, если его оборудование включено в список совместимого оборудования, размещенного по
                        адресу, указанному в «Инструкции для Абонента». Настройка и поддержка оборудования, не
                        включенного в список, Оператором связи не производится, претензии по качеству оказания Услуги не
                        принимаются.
                    </li>
                    <li>Оператор связи оставляет за собой право ввести фильтрацию трафика по определенным адресам и
                        портам протоколов TCP и UDP для защиты абонентского оборудования.
                    </li>
                </ol>
            </li>
            <li><h3 class="uppercase">Индивидуальные условия</h3>
                <ol>
                    <li>При подключении Абонент выбирает авансовые условия оплаты услуг.
                    </li>
                    <li>При подключении Абонент выбирает тарифный план <span class="important">${annex.tariffPlan.name}</span></li>
                    <li>Адрес установки оконечного оборудования: <span class="important">${annex.address}</span>
                    </li>
                </ol>
            </li>
        </ol>
        <table class="subscription">
            <tr>
                <td>От Оператора связи:</td>
                <td>От Абонента:</td>
            </tr>
            <tr>
                <td>
                    <div></div>
                </td>
                <td>
                    <div></div>
                </td>
            </tr>
        </table>
    </div>
</section>

<!-- Footer -->
<jsp:include page="../template/footer.jsp"/>
</body>
</html>