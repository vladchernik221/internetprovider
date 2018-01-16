function send_form(event) {
    event.preventDefault();
    var form = $("#annex_form");
    $.ajax({
        type: form.attr("method"),
        url: form.attr("action"),
        data: form.serialize(),
        success: function (result) {
            redirect("/contract/annex/" + result);
        },
        error: error_handler
    });
}

function cancel_annex(annex_id) {
    $.ajax({
        type: "POST",
        url: "/contract/annex/" + annex_id + "/cancel",
        success: function () {
            window.location.reload(true);
        },
        error: error_handler
    });
}