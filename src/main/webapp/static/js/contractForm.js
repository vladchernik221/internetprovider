$(document).ready(function () {
    $(".only-for-legal").css({display: "none"});
    $(".only-for-legal input").attr("disabled", "disabled");
});

function change_client_type(client_type) {
    if (client_type === "individual") {
        $(".only-for-individual").css({display: "block"});
        $(".only-for-individual input").removeAttr("disabled");
        $(".only-for-legal").css({display: "none"});
        $(".only-for-legal input").attr("disabled", "disabled");
    } else {
        $(".only-for-individual").css({display: "none"});
        $(".only-for-individual input").attr("disabled", "disabled");
        $(".only-for-legal").css({display: "block"});
        $(".only-for-legal input").removeAttr("disabled");
    }
}

function fill_form(event) {
    event.preventDefault();
    event.stopPropagation();
    var form = $('#contract_form');
    var client_type = form.find("input[name='clientType']:checked").val();
    var identifier = client_type === "INDIVIDUAL" ?
        form.find("input[name='individual.passportUniqueIdentification']").val() :
        form.find("input[name='legal.payerAccountNumber']").val();
    $.ajax({
        type: "POST",
        url: "/client/" + client_type.toLowerCase(),
        data: {
            identifier: identifier
        },
        success: function (result) {
            if (result !== "null") {
                var resultJSON = JSON.parse(result);
                if (client_type === "INDIVIDUAL") {
                    form.find("input[name='individual.secondName']").val(resultJSON.secondName);
                    form.find("input[name='individual.firstName']").val(resultJSON.firstName);
                    form.find("input[name='individual.lastName']").val(resultJSON.lastName);
                    form.find("input[name='individual.address']").val(resultJSON.address);
                    form.find("input[name='individual.phoneNumber']").val(resultJSON.phoneNumber);
                } else {
                    form.find("input[name='legal.name']").val(resultJSON.name);
                    form.find("input[name='legal.checkingAccount']").val(resultJSON.checkingAccount);
                    form.find("input[name='legal.address']").val(resultJSON.address);
                    form.find("input[name='legal.phoneNumber']").val(resultJSON.phoneNumber);
                }
            } else {
                $("#modal_message").html("Клиент с типом " + client_type + " и идентификатором " + identifier + " не найден.");
                show_modal();
            }
        },
        error: error_handler
    });
}

function send_form(event) {
    event.preventDefault();
    var form = $("#contract_form");
    $.ajax({
        type: form.attr("method"),
        url: form.attr("action"),
        data: form.serialize(),
        success: function (result) {
            redirect("/contract/" + result);
        },
        error: error_handler
    });
}