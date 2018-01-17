function remove(discount_id, event) {
    if(event !== undefined) {
        event.stopPropagation();
    }
    $.ajax({
        type: "POST",
        url: "/discount/" + discount_id + "/remove",
        success: function () {
            redirect("/discount");
        },
        error: error_handler
    });
}

function send_form(event) {
    event.preventDefault();
    var form = $("#discount_form");
    $.ajax({
        type: form.attr("method"),
        url: form.attr("action"),
        data: form.serialize(),
        success: function (result) {
            if(result === "") {
                window.location.reload(true);
            } else {
                redirect("/discount/" + result + "/edit");
            }
        },
        error: error_handler
    });
}