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
    if(checkbox.checked) {
        redirect("/tariff-plan?archived=true");
    } else {
        redirect("/tariff-plan?archived=false");
    }
}