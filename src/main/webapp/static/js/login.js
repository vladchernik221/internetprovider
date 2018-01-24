function send(event) {
    event.preventDefault();
    var form = $("#login_form");
    $.ajax({
        type: form.attr("method"),
        url: form.attr("action"),
        data: form.serialize(),
        success: function () {
            redirect("/");
        },
        error: error_handler
    });
}