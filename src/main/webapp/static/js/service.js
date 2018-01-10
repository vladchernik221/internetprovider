function change_archived(service_id, event) {
    if(event !== undefined) {
        event.stopPropagation();
    }
    $.ajax({
        type: "POST",
        url: "/service/" + service_id + "/archive",
        success: function () {
            window.location.reload(true);
        },
        error: error_handler
    });
}

function show_archived(checkbox) {
    if(checkbox.checked) {
        redirect("/service?archived=true");
    } else {
        redirect("/service?archived=false");
    }
}

function send_form(event) {
    event.preventDefault();
    var form = $("#service_form");
    $.ajax({
        type: form.attr("method"),
        url: form.attr("action"),
        data: form.serialize(),
        success: function (result) {
            if(result === "") {
                window.location.reload(true);
            } else {
                redirect("/service/" + result + "/edit");
            }
        },
        error: error_handler
    });
}