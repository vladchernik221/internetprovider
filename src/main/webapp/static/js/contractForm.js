$(document).ready(function() {
    $(".only-for-legal").css({ display: "none" });
    $(".only-for-legal input").attr("disabled", "disabled");
});

function change_client_type(client_type) {
    if(client_type === "individual") {
        $(".only-for-individual").css({ display: "block" });
        $(".only-for-individual input").removeAttr("disabled");
        $(".only-for-legal").css({ display: "none" });
        $(".only-for-legal input").attr("disabled", "disabled");
    } else {
        $(".only-for-individual").css({ display: "none" });
        $(".only-for-individual input").attr("disabled", "disabled");
        $(".only-for-legal").css({ display: "block" });
        $(".only-for-legal input").removeAttr("disabled");
    }
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