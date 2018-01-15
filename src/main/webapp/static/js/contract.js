function dissolve(contract_id) {
    $.ajax({
        type: "POST",
        url: "/contract/" + contract_id + "/dissolve",
        success: function () {
            window.location.reload(true);
        },
        error: error_handler
    });
}