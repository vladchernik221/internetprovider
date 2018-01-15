function send_form(event) {
    event.preventDefault();
    var form = $("#annex_form");
    $.ajax({
        type: form.attr("method"),
        url: form.attr("action"),
        data: form.serialize(),
        success: function (result) {
            redirect(form.attr("action").slice(0, form.attr("action").lastIndexOf("/") + 1) + result);
        },
        error: error_handler
    });
}