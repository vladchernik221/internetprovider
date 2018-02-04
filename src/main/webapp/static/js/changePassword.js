function send_form(event) {
    event.preventDefault();
    var form = $("#password_form");
    if(validate_form(form)) {
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
}

function validate_form(form) {
    if(form.find("input[name=newPassword]").val() !== form.find("input[name=confirmNewPassword]").val()) {
        $("#modal_message").html("Пароли не совпадают");
        show_modal();
        form.find("input[name=newPassword]").val("");
        form.find("input[name=confirmNewPassword]").val("");
        return false;
    }
    return true;
}