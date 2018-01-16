function change_blocked(user_id, event) {
    if(event !== undefined) {
        event.stopPropagation();
    }
    $.ajax({
        type: "POST",
        url: "/user/" + user_id + "/block",
        success: function () {
            window.location.reload(true);
        },
        error: error_handler
    });
}

function show_with_role(radio) {
    if(radio.value === "ALL") {
        redirect(delete_param_from_url("role"));
    } else {
        redirect(add_param_to_url([["role", radio.value]]));
    }
}

function send_form(event) {
    event.preventDefault();
    var form = $("#user_form");
    $.ajax({
        type: form.attr("method"),
        url: form.attr("action"),
        data: form.serialize(),
        success: function (result) {
            if(result === "") {
                window.location.reload(true);
            } else {
                redirect("/user");
            }
        },
        error: error_handler
    });
}