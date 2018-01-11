function change_archived(tariff_plan_id, event) {
    if(event !== undefined) {
        event.stopPropagation();
    }
    $.ajax({
        type: "POST",
        url: "/tariff-plan/" + tariff_plan_id + "/archive",
        success: function () {
            window.location.reload(true);
        },
        error: error_handler
    });
}

function show_archived(checkbox) {
    var new_status = checkbox.checked ? "true" : "false";
    redirect(add_param_to_url([["archived", new_status], ["page", 1]]));
}