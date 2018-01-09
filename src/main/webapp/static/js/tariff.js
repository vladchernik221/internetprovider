function change_archived(tariff_plan_id, event) {
    if(event !== undefined) {
        event.stopPropagation();
    }
    $.ajax({
        type: "POST",
        url: "/tariff-plan/" + tariff_plan_id + "/archive",
        success: function (result, status, xhr) {
            if(xhr.getResponseHeader("content-type").toLowerCase() === "text/html;charset=utf-8") {
                document.write(result);
            } else {
                window.location.reload(true); //TODO не работает
            }
        },
        error: function (xhr) {
            $("#modal_message").html("Ошибка " + xhr.status + ": " + xhr.responseText);
            show_modal();
        }
    });
}